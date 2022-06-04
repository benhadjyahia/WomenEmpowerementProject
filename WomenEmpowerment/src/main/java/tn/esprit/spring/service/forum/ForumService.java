package tn.esprit.spring.service.forum;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import sun.net.www.content.text.plain;
import tn.esprit.spring.entities.*;

import tn.esprit.spring.repository.*;
import tn.esprit.spring.service.event.CloudinaryService;
import tn.esprit.spring.service.event.MediaService;
import tn.esprit.spring.service.user.ServiceAllEmail;
import tn.esprit.spring.serviceInterface.user.UserService;

@Service
public class ForumService {
	
	@Autowired
	MediaService mediaService;
	
	
	@Autowired
	CategoryAdverRepo categoryAdvrepo;
	
	@Autowired
	CloudinaryService cloudImage;
	
	@Autowired
	CategoryAdverRepo categoryAdverRepo; 
	@Autowired
	UserDataLoadRepo userDataLoadRepo;
	
	@Autowired
	UserService userService;
	
	@Autowired
	AdvertisingRepo advertisingRepo;
	
	@Autowired
	PostRepo postRepo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	PostCommentRepo postCommentRepo;

	@Autowired
	PostLikeRepo postLikeRepo;

	@Autowired
	PostDislikeRepo postDislikeRepo;

	@Autowired
	ServiceAllEmail emailService;
	
	@Autowired
	CommentLikeRepo commentLikeRepo;

	@Autowired
	BadWordRepo badWordRepo;

	public ResponseEntity<?> addPost(Post post, Long IdUser) {

		User u = userRepo.findById(IdUser).orElse(null);
		DetctaDataLoad(post.getBody(),IdUser);
		DetctaDataLoad(post.getPostTitle(),IdUser);
		if (Filtrage_bad_word(post.getBody()) == 0 && Filtrage_bad_word(post.getPostTitle()) == 0) {
			post.setUser(u);
			u.getPosts().add(post);
			postRepo.save(post);
			return ResponseEntity.ok().body(post);
		} else
			return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("Bads Word Detected");
	}
	
	public ResponseEntity<?> addAdvertising(Advertising a, Long IdUser,Long idCategory) {
		CategoryAdve c =  categoryAdvrepo.findById(idCategory).orElse(null);
				User u = userRepo.findById(IdUser).orElse(null);
a.setCategoryadv(c);
		if (Filtrage_bad_word(a.getName()) == 0) {
			a.setUser(u);
			
			advertisingRepo.save(a);
			return ResponseEntity.ok().body(a);
		} else
			return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("Bads Word Detected");
	}
	
	public BadWord addBadWord(BadWord b ) {
		
		return badWordRepo.save(b);
	}
	public List<Advertising> get_all_adversting(){
		return advertisingRepo.findAll();
	}
	public ResponseEntity<?> addComment_to_Post(PostComment postComment, Long idPost, Long idUser) {
		Post p = postRepo.findById(idPost).orElse(null);
		User u = userRepo.findById(idUser).orElse(null);
		DetctaDataLoad(postComment.getCommentBody(),idUser);
		if (Filtrage_bad_word(postComment.getCommentBody()) == 0) {
			postComment.setUser(u);
			postComment.setPost(p);

			postCommentRepo.save(postComment);
			return ResponseEntity.ok().body(postComment);      }else
			/*
			 * Set<PostComment> pc = p.getPostComments(); pc.add(postComment);
			 * p.setPostComments(pc); postRepo.save(p);
			 * 
			 * Set<PostComment> pu = u.getPostComments(); pu.add(postComment);
			 * u.setPostComments(pu); userRepo.save(u);
			 * 
			 * 
			 */
			//}
		return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("Bads Word Detected");
	}

		public PostLike addLike_to_Post(PostLike postLike, Long idPost, Long idUser) {
			int x=0;
			boolean y =false;
			Post p = postRepo.findById(idPost).orElse(null);
			User u = userRepo.findById(idUser).orElse(null);
			for (PostLike l : postLikeRepo.findAll()) {
				if(l.getPost().getPostId() == idPost && l.getUser().getUserId() == idUser)
				{	
					x=1;
					y=l.getIsLiked();
					postLikeRepo.delete(l);
					}	
				
			}
				if (x ==0 || (x == 1 && y!=postLike.getIsLiked()	)) {
			DetctaDataLoad(p.getBody(),idUser);
			postLike.setUser(u);
			postLike.setPost(p);
			 postLikeRepo.save(postLike);}
				return postLike;
		}

	/*
	 * public ResponseEntity<?> addDisLike_to_Post(PostDislike postDisLike, Long
	 * idPost, Long idUser) { Post p = postRepo.findById(idPost).orElse(null); User
	 * u = userRepo.findById(idUser).orElse(null);
	 * 
	 * Delete_Like(get_like_exist(idUser, idPost).getPostLikeId(), idUser);
	 * 
	 * postDisLike.setUser(u); postDisLike.setPost(p);
	 * postDislikeRepo.save(postDisLike);
	 * 
	 * return ResponseEntity.ok().body(get_like_exist(idUser,
	 * idPost).getPostLikeId()); }
	 */
	public CommentLike addLike_to_Comment(CommentLike commentLike, Long idComment, Long idUser) {
		User u = userRepo.findById(idUser).orElse(null);
		PostComment p = postCommentRepo.findById(idComment).orElse(null);
		DetctaDataLoad(p.getCommentBody(),idUser);
		commentLike.setUser(u);
		commentLike.setPostComment(p);
		return commentLikeRepo.save(commentLike);
	}

	public ResponseEntity<?> Update_post(Post post, Long idPost, Long idUser) {
		if (postRepo.existsById(idPost)) {
			Post post1 = postRepo.findById(idPost).orElseThrow(() -> new EntityNotFoundException("post not found"));
			//User user = userRepo.findById(idUser).orElseThrow(() -> new EntityNotFoundException("User not found"));
			if (Filtrage_bad_word(post.getBody()) == 0 && Filtrage_bad_word(post.getPostTitle()) == 0) {
				if(post.getPostTitle().equals("") == false)	
				post1.setPostTitle(post.getPostTitle());
				if(post.getBody().equals("") == false)	
				post1.setBody(post.getBody());
				postRepo.saveAndFlush(post1);
				return ResponseEntity.ok().body(post);
			
		} 
			else
				return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("Bads Word Detected");}

			else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("post Not Founf");
		}
	}
	
	public ResponseEntity<?> Update_Adversting(Advertising a, Long idPost) {
		if (postRepo.existsById(idPost)) {
			Advertising a1 = advertisingRepo.findById(idPost).orElseThrow(() -> new EntityNotFoundException("adversting not found"));
			//User user = userRepo.findById(idUser).orElseThrow(() -> new EntityNotFoundException("User not found"));
			

				a1.setName(a.getName());
				//a1.setCanal(a.getCanal());
				a1.setPrice(a.getPrice());
				a1.setEndDate(a.getEndDate());
				a1.setStartDate(a.getStartDate());
				
				advertisingRepo.saveAndFlush(a1);
				return ResponseEntity.ok().body(a1);
			
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("adversting Not Founf");
		}
	}

	public ResponseEntity<?> Update_Comment(PostComment postComment, Long idPostCom, Long idUser) {
		if (postCommentRepo.existsById(idPostCom)) {
			PostComment postCom1 = postCommentRepo.findById(idPostCom)
					.orElseThrow(() -> new EntityNotFoundException("Comment not found"));
			//User user = userRepo.findById(idUser).orElseThrow(() -> new EntityNotFoundException("User not found"));
			//if (postCom1.getUser().equals(user)) {

				postCom1.setCommentBody(postComment.getCommentBody());
				postCommentRepo.save(postCom1);
				return ResponseEntity.ok().body(postCom1);
			//} else {
			//	return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("No permission to delete this post ");
		//	}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment Not Founf");
		}
	}

	public ResponseEntity<?> Delete_post(Long idPost, Long idUser) {
		if (postRepo.existsById(idPost)) {
			Post post1 = postRepo.findById(idPost).orElseThrow(() -> new EntityNotFoundException("post not found"));
			User user = userRepo.findById(post1.getUser().getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
			//if (post1.getUser().equals(user)) {
			post1.setReportedby(null);
			postRepo.save(post1);
			Set<Post> p = user.getPosts();
			p.remove(post1);
			user.setPosts(p);
			userRepo.save(user);
				postRepo.delete(post1);
				return ResponseEntity.ok().body("Delete success");
		//	} else {
			//	return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("No permission to delete this post");
			//}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("post Not Founf");
		}

	}
	
	public ResponseEntity<?> Delete_Adversting(Long idadv) {
		if (advertisingRepo.existsById(idadv)) {
			Advertising a1 = advertisingRepo.findById(idadv).orElseThrow(() -> new EntityNotFoundException("adv not found"));
			//User user = userRepo.findById(idUser).orElseThrow(() -> new EntityNotFoundException("User not found"));
		
			advertisingRepo.delete(a1);
				return ResponseEntity.ok().body("Delete success");
			
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("adv Not Founf");
		}

	}

	public List<Post> Get_all_post() {
		List<Post> findAll = postRepo.findAll();
		return findAll;

	}

	public Set<Post> Get_post_by_User(Long idUser) {

		User user = userRepo.findById(idUser).orElseThrow(() -> new EntityNotFoundException("User not found"));

		return user.getPosts();
	}

	public int Filtrage_bad_word(String ch) {
		int x = 0;
		List<BadWord> l1 = (List<BadWord>) badWordRepo.findAll();
		for (BadWord badWord : l1) {
			// if (badWord.getWord().contains(ch))
			if (ch.contains(badWord.getWord()) == true)
				x = 1;
		}
		return x;

	}

	public ResponseEntity<?> Delete_Like(Long idLike, Long idUser) {
		if (postLikeRepo.existsById(idLike)) {
			PostLike postLike = postLikeRepo.findById(idLike)
					.orElseThrow(() -> new EntityNotFoundException("Like Not Found"));
			User user = userRepo.findById(idUser).orElseThrow(() -> new EntityNotFoundException("User not found"));
			if (postLike.getUser().equals(user)) {
				postLikeRepo.delete(postLike);
				return ResponseEntity.ok().body("Delete success");
			} else {
				return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("No permission to delete this post");
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Like Not Found");
		}
	}

	public ResponseEntity<?> Delete_DisLike(Long idDisLike, Long idUser) {
		if (postDislikeRepo.existsById(idDisLike)) {
			PostDislike postDisLike = postDislikeRepo.findById(idDisLike)
					.orElseThrow(() -> new EntityNotFoundException("Like Not Found"));
			User user = userRepo.findById(idUser).orElseThrow(() -> new EntityNotFoundException("User not found"));
			if (postDisLike.getUser().equals(user)) {
				postDislikeRepo.delete(postDisLike);
				return ResponseEntity.ok().body("Delete success");
			} else {
				return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("No permission to delete this post");
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Like Not Found");
		}
	}

	public Set<PostLike> Get_post_Likes(Long idPost) {
		Post post1 = postRepo.findById(idPost).orElseThrow(() -> new EntityNotFoundException("post not found"));
		Set<PostLike> pp = post1.getPostLikes();
		for (PostLike postLike : pp) {
			if (postLike.getIsLiked() == false) {
				pp.remove(postLike);
			}
		}
		return pp;
	}

	public Set<PostLike> Get_post_DisLikes(Long idPost) {
		Post post1 = postRepo.findById(idPost).orElseThrow(() -> new EntityNotFoundException("post not found"));
		Set<PostLike> pp = post1.getPostLikes();
		for (PostLike postLike : pp) {
			if (postLike.getIsLiked() == true) {
				pp.remove(postLike);
			}
		}
		return pp;
	}

	public ResponseEntity<?> Delete_PostCom(Long idPostCom, Long idUser) {
		if (postCommentRepo.existsById(idPostCom)) {
			PostComment postCom1 = postCommentRepo.findById(idPostCom)
					.orElseThrow(() -> new EntityNotFoundException("post not found"));
			User user = userRepo.findById(idUser).orElseThrow(() -> new EntityNotFoundException("User not found"));
			if (postCom1.getUser().equals(user)) {
				postCommentRepo.delete(postCom1);
				return ResponseEntity.ok().body("Delete success");
			} else {
				return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("No permission to delete this post");
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("post Not Founf");
		}
	}

	public ResponseEntity<?> add_Com_to_Com(PostComment postComment, Long idUser, Long idCom) {
		PostComment p = postCommentRepo.findById(idCom).orElse(null);
		User u = userRepo.findById(idUser).orElse(null);
		if (Filtrage_bad_word(postComment.getCommentBody()) == 0) {
			postComment.setUser(u);
		//	postComment.setPost(p.getPost());
		//	p.getPostComments().add(postComment);
			postComment.setPostCo(p);
			postCommentRepo.save(postComment);
			return ResponseEntity.ok().body(postComment);
			/*
			 * Set<PostComment> pc = p.getPostComments(); pc.add(postComment);
			 * p.setPostComments(pc); postRepo.save(p);
			 * 
			 * Set<PostComment> pu = u.getPostComments(); pu.add(postComment);
			 * u.setPostComments(pu); userRepo.save(u);
			 * 
			 * 
			 */
		}
		return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("Bads Word Detected");
	}

	public ResponseEntity<?> Swap_like_dislike(Long idLike) {
		PostLike p = postLikeRepo.findById(idLike).orElse(null);
		p.setIsLiked(!p.getIsLiked());
		postLikeRepo.saveAndFlush(p);
		return ResponseEntity.ok().body(p);
	}
	
	
//@Scheduled(cron = "*/30 * * * * *")
		public void delete_sujet_sans_Int() {
			for (Post p : postRepo.findAll()) {
				if (postRepo.diffrence_entre_date(p.getCreatedAt())>30) {
					if (p.getPostLikes().size() == 0) {
						Delete_post(p.getPostId(), p.getUser().getUserId());
						System.out.println("Post with id = "+ p.getPostId() +" deleted");
					}
				}
			}
			System.out.println("Testing for post with no interraction");
		}
	
		public boolean date_comp(Date d) {
			if (LocalDate.now().getMonthValue() - d.getMonth() > 2) {
				return true;
			}
			if (LocalDate.now().getMonthValue() - d.getMonth() > 1) {
				if (LocalDate.now().getDayOfMonth() >= d.getDate()) {
					return true;
				}
	
				else {
					if (LocalDate.now().getDayOfMonth() - d.getDate() == 30) {
						return true;
					}
				}
	
			}
	
			return false;
		}

	public Post Get_best_Post() throws MessagingException {
		Post p1 = null;
		int x = 0;
		for (Post p : postRepo.findAll()) {
			if (postRepo.diffrence_entre_date(p.getCreatedAt()) <= 7) {
				if (p.getPostLikes().size() > x) {
					p1 = p;
					x = p.getPostLikes().size();
				}
				/*
				 * else if (p.getPostLikes().size() == x) { if
				 * (postRepo.diffrence_entre_date(p.getCreatedAt())<postRepo.
				 * diffrence_entre_date(p1.getCreatedAt())) { p1 = p;} }
				 */
			}
		}
		emailService.sendAllertReport("Congrates Your Post : "+p1.getPostTitle()+" is the best post for week  \n", p1.getUser().getEmail());
		return p1;
	}

	public Set<PostComment> Get_post_Comm(Long idPost) {
		Post p = postRepo.findById(idPost).orElse(null);
		return p.getPostComments();
	}

	public Set<PostComment> Get_comm_Comm(Long idComment) {
		PostComment p = postCommentRepo.findById(idComment).orElse(null);
		return p.getPostComments();
	}

	public Post Give_Etoile_Post(Long idPost, int nb_etouile) {
		Post post1 = postRepo.findById(idPost).orElseThrow(() -> new EntityNotFoundException("post not found"));

		post1.setNb_etoil(nb_etouile);
		return postRepo.save(post1);

	}

	public ResponseEntity<?> Report_User(Long idPost,Long iduser) throws MessagingException {
		Post post1 = postRepo.findById(idPost).orElseThrow(() -> new EntityNotFoundException("post not found"));
		int x =0;
		for (User u : post1.getReportedby()) {
			if(u.getUserId() == iduser)
				x=1;
		}
		if (x ==0) {
		User u = userRepo.findById(iduser).orElse(null);
		post1.setNb_Signal(post1.getNb_Signal() + 1);
		Set<User> ur = post1.getReportedby();
		ur.add(u);
		post1.setReportedby(ur);
		if (post1.getNb_Signal()>7)
			emailService.sendAllertReport("Your Post : "+post1.getPostTitle()+ " have More than "+ post1.getNb_Signal() +" reports \n", post1.getUser().getEmail());
		 postRepo.save(post1);
			return ResponseEntity.status(HttpStatus.OK).body("Post : "+idPost+" reported ");
			}
		else return ResponseEntity.status(HttpStatus.OK).body("U are already report this post ");
			
	}
	
	
//Delete Reported Post when they get more then 10 report
	//@Scheduled(cron = "*/30 * * * * *")
	public void delete_reported_post () throws MessagingException {
		for (Post p : postRepo.findAll()) {
			if (p.getNb_Signal() >= 9) {
				Delete_post(p.getPostId(), p.getUser().getUserId());
				emailService.sendAllertReport("Your Post : "+p.getPostTitle()+" is deleted  \n", p.getUser().getEmail());
			}
			
		}
	}

	public Set<Object> Get_more_likers_user() {
		return postLikeRepo.USer_order_by_Like();
		
	}



	
	
	public ResponseEntity<?> addCategoryAdv(CategoryAdve a) {

		categoryAdverRepo.save(a);
			return ResponseEntity.ok().body(a);
		
			
	}
	
// gets Friends Post	
	public Set<Post> get_Frinds_post(Long id) {
		User u = userRepo.findById(id).orElse(null);
		Set<Post> friendsPost = null;
		for (User friends : userService.getMyFriends(u)) {
			for (Post post : friends.getPosts()) {
				friendsPost.add(post);
				
			}
			
		}
		
		return friendsPost;
		
	}
// detection des champ por ajouter dataUseradv
	public Boolean existDataForUser(String ch,Long IdUser) {
		Boolean x = false;
		for (UserDataLoad userDataLoad : userDataLoadRepo.findAll()) {
			if (userDataLoad.getCategorieData().equals(ch) && userDataLoad.getUser().getUserId() == IdUser) {
				 x = true;
			}
		} return x;
	}
	public UserDataLoad getData(String ch,Long IdUser) {
		UserDataLoad x = null;
		for (UserDataLoad userDataLoad : userDataLoadRepo.findAll()) {
			if (userDataLoad.getCategorieData().equals(ch) && userDataLoad.getUser().getUserId() == IdUser) {
				 x = userDataLoad;
			}
		} return x;
	}
	public void DetctaDataLoad (String ch , Long idUser) {

		List<UserDataLoad> ul = userDataLoadRepo.findAll();
		User u = userRepo.findById(idUser).orElse(null);
		for (CategoryAdve string : categoryAdverRepo.findAll()) {
			if (ch.contains(string.getNameCategory())) {
				if (existDataForUser(string.getNameCategory(),idUser) == true) {
					UserDataLoad l = getData(string.getNameCategory(),idUser);
					l.setNbrsRequet(l.getNbrsRequet()+1);
					userDataLoadRepo.save(l);
				}
				else {
					UserDataLoad l1 = new UserDataLoad();
					l1.setCategorieData(string.getNameCategory());
					l1.setUser(u);
					l1.setNbrsRequet(1);
					userDataLoadRepo.save(l1);
					
				}
			}
		}
	}
	
// get adversting for uUser with DataLoads && age cible
	public List<Advertising> getAdverByUserData(Long idUser){
		UserDataLoad dataus = new UserDataLoad();
		List<Advertising> ll = new ArrayList<>();
		int x = 0 ;
		for (UserDataLoad data : userDataLoadRepo.findAll()) {
			
			if (data.getUser().getUserId() == idUser) {
				if (data.getNbrsRequet()>=x) {
					x= data.getNbrsRequet();
					dataus = data;
			}}}
		List<Advertising> aa = advertisingRepo.findAll();
	for (Advertising advertising : aa) {
		if(advertising.getCategoryadv().getNameCategory().equals(dataus.getCategorieData()) && advertising.getMinage()<=getuserage(idUser) && advertising.getMaxage()>=getuserage(idUser))
			ll.add(advertising);
	}	
		return ll;
	}
	
// recherche post 
	public List<Post> Searchpost(String ch,Long id){
		List<Post> ll = new ArrayList<>();
		for (Post post : postRepo.findAll()) {
			if (post.getBody().contains(ch) || post.getPostTitle().contains(ch))
			ll.add(post);
		}
		DetctaDataLoad(ch,id);
		return ll;
	}
//afficher la list des user report post
	public Set<User>  reportuser(Long id){
		Post p =  postRepo.findById(id).orElse(null);
		return p.getReportedby();
		
	}
	
//get user age
public int getuserage(Long idUser) {
	User u = userRepo.findById(idUser).orElse(null);
	
	int x = postRepo.diffrence_entre_date(u.getBirthDate());
	
	 return x/365;
	
	
}
// ocr add image

public ResponseEntity<?> addimagepost(MultipartFile image,Long idpost) throws IOException {
	Post p = postRepo.findById(idpost).orElse(null);
	String ch = DoOCR(image);
	BufferedImage bi = ImageIO.read(image.getInputStream());
	if (Filtrage_bad_word(ch) == 0 ) {
	Map result = cloudImage.upload(image);
	
	Media media = new Media((String) 
			result.get("original_filename")
			, (String) result.get("url"),
			(String) result.get("public_id"));
	media.setPost(p);
	mediaService.save(media);
	/*
	Set<Media> lp = p.getMedias();
	lp.add(media);
	p.setMedias(lp);
	*/
	//postRepo.save(p);
	return ResponseEntity.status(HttpStatus.OK).body("Image added to post");
	}
	else return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("U r Image Content interdit word");

}


public ResponseEntity<?> addimageAdverstingt(MultipartFile image,Long idadv) throws IOException {
	Advertising p = advertisingRepo.findById(idadv).orElse(null);
	String ch = DoOCR(image);
	BufferedImage bi = ImageIO.read(image.getInputStream());
	if (Filtrage_bad_word(ch) == 0 ) {
	Map result = cloudImage.upload(image);
	
	Media media = new Media((String) 
			result.get("original_filename")
			, (String) result.get("url"),
			(String) result.get("public_id"));
	media.setAdvertising(p);
	mediaService.save(media);
	/*
	Set<Media> lp = p.getMedias();
	lp.add(media);
	p.setMedias(lp);
	advertisingRepo.save(p);
	*/
	return ResponseEntity.status(HttpStatus.OK).body("Image added to adversting");
	}
	else return ResponseEntity.status(HttpStatus.OK).body("U r Image Content interdit word");

}

public String DoOCR(
		MultipartFile image) throws IOException {

	
	OcrModel request = new OcrModel();
	request.setDestinationLanguage("eng");
	request.setImage(image);
	
	ITesseract instance = new Tesseract();

	try {
		
		BufferedImage in = ImageIO.read(convert(image));

		BufferedImage newImage = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
        
		Graphics2D g = newImage.createGraphics();
		g.drawImage(in, 0, 0, null);
		g.dispose();
        
		instance.setLanguage(request.getDestinationLanguage());
		instance.setDatapath("..\\WomenEmpowerment\\tessdata");

		String result = instance.doOCR(newImage);

		return result;

	} catch (TesseractException | IOException e) {
		System.err.println(e.getMessage());
		return "Error while reading image";
	}

}
public static File convert(MultipartFile file) throws IOException {
    File convFile = new File(file.getOriginalFilename());
    convFile.createNewFile();
    FileOutputStream fos = new FileOutputStream(convFile);
    fos.write(file.getBytes());
    fos.close();
    return convFile;
}


public int PostLikeFromUser(Long isUser,Long Idpost) {
	int x =0;
	for (PostLike l : postLikeRepo.findAll()) {
		if (l.getPost().getPostId()== Idpost && l.getUser().getUserId()== isUser) {
			if (l.getIsLiked() == true) {x= 1;}	
			else {x=0;}
		}
		
	}
	return x;
}
public Post getPostById (Long id) {
	Post p = postRepo.findById(id).orElse(null);
	return p;
}
}
