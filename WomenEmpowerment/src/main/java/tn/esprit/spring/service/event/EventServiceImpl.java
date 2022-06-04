package tn.esprit.spring.service.event;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;

import tn.esprit.spring.config.TwilioConfiguration;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Donation;
import tn.esprit.spring.entities.Event;
import tn.esprit.spring.entities.Media;
import tn.esprit.spring.entities.Post;
import tn.esprit.spring.entities.PostComment;
import tn.esprit.spring.entities.PostLike;
import tn.esprit.spring.entities.SmsRequest;

import tn.esprit.spring.entities.User;
import tn.esprit.spring.entities.eventComment;
import tn.esprit.spring.enumerations.EventType;
import tn.esprit.spring.exceptions.CourseNotExist;
import tn.esprit.spring.exceptions.CourseOwnerShip;
import tn.esprit.spring.repository.DonationRepo;
import tn.esprit.spring.repository.EventRepo;
import tn.esprit.spring.repository.MediaRepo;
import tn.esprit.spring.repository.UserRepository;
import tn.esprit.spring.repository.eventcommentRepo;
import tn.esprit.spring.service.user.ServiceAllEmail;
import tn.esprit.spring.serviceInterface.EventService;
import tn.esprit.spring.serviceInterface.SmsSender;

@Service
public class EventServiceImpl implements EventService {

	private final SmsSender smsSender;

	@Autowired
	EventRepo eventRepo;
	@Autowired
	CloudinaryService cloudImage;
	@Autowired
	MediaRepo mediaRepo;
	@Autowired
	MediaService mediaService;
	@Autowired
	UserRepository userRepo;
	@Autowired
	ServiceAllEmail emailService;
	@Autowired
	eventcommentRepo eventcomRepo;
	
	@Autowired
	DonationRepo donationRepo;

	@Autowired
	public EventServiceImpl(@Qualifier("twilio") TwilioSmsSender smsSender) {
		this.smsSender = smsSender;

	}

	/*
	 * @Override public void createEventbyUser(Long idUser , Event event) throws
	 * MessagingException{ Set<Event> eventList = new HashSet<Event>();
	 * 
	 * eventList.add(event); User user = userRepo.findById(idUser).orElse(null);
	 * 
	 * user.setCreatedEvents(eventList); userRepo.save(user); invite_participants
	 * (event); emailService.sendNewEventCreatedByUser(event.getEventName(),
	 * user.getEmail());
	 * 
	 * 
	 * 
	 * }
	 */
	
	
	@Override
	public ResponseEntity<?> create(Long iduser, Event event) throws IOException {
		Set<Event> eventList = new HashSet<Event>();
		User user = userRepo.findById(iduser).orElse(null);
		eventList.add(event);
		user.setCreatedEvents(eventList);
		event.setCreateurEvent(user);
		
		eventRepo.save(event);
		return new ResponseEntity(event, HttpStatus.OK);
		
		
			

	}

	@Override
	public ResponseEntity<?> createEventbyUser(Long idUser, MultipartFile multipartFile, String EventName,
			String description, Date createAt,Date StartAt ,Date endAt, int maxPlace, float targetDonation,
			String address,String latitude,String lang) throws MessagingException, IOException {
		Set<User> usersList = new HashSet<User>();
		Set<Event> eventList = new HashSet<Event>();
		User user = userRepo.findById(idUser).orElse(null);
		BufferedImage bi = ImageIO.read(multipartFile.getInputStream());

		Map result = cloudImage.upload(multipartFile);
		Event event = new Event();
		event.setEventName(EventName);
	
		event.setEndAt(endAt);

		event.setTargetDonation(targetDonation);
	
		event.setDescription(description);
		event.setLang(lang);
		event.setLatitude(latitude);

		event.setCreateurEvent(user);

		Media media = new Media((String) 
				result.get("original_filename")
				, (String) result.get("url"),
				(String) result.get("public_id"));

		media.setEvents(event);
		eventList = user.getCreatedEvents();
		eventList.add(event);
		eventRepo.save(event);
		user.setCreatedEvents(eventList);

		userRepo.save(user);
		mediaService.save(media);
		invite_participants(event);
		emailService.sendNewEventCreatedByUser(event.getEventName(), user.getEmail());

		return new ResponseEntity(" the event was created with succeed ", HttpStatus.OK);
	}

	

	@Override
	public ResponseEntity<?> updateImageForEvent(Long idmedia, MultipartFile multipartFile) throws IOException {
		Set<Media> mediaList = new HashSet<Media>();

		Media media = mediaRepo.findById(idmedia).orElse(null);

		BufferedImage bi = ImageIO.read(multipartFile.getInputStream());
		if (bi == null) {
			return new ResponseEntity("invalid image", HttpStatus.BAD_REQUEST);
		}
		Map result = cloudImage.upload(multipartFile);
		media = new Media((String) result.get("original_filename"), (String) result.get("url"),
				(String) result.get("imagencode"));

		mediaRepo.flush();

		return new ResponseEntity(media, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> deleteImageForEvent(Long idMedia) throws IOException {
		// Media media = mediaRepo.findById(idMedia).orElse(null);
		if (!mediaService.exists(idMedia))
			return new ResponseEntity("does not exist", HttpStatus.NOT_FOUND);
		Media media = mediaService.getOne(idMedia).get();
		Map result = cloudImage.delete(media.getCodeImage());
		mediaService.delete(idMedia);
		return new ResponseEntity("imagen eliminada", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> addImageForDocumentOfEvent(Long idEvent, Set<MultipartFile> multipartFile)
			throws IOException {
		return null;

	}

	public void invite_participants(Event event) throws MessagingException {

		 SmsRequest smsrequest = new SmsRequest(null, null);
		List<Long> CampanyList = eventRepo.GET_LIST_CAMPANY();

		System.out.println("user campany");
		if (eventRepo.GET_LIST_CAMPANY() != null) {

			for (Long user : CampanyList) {
				User u = userRepo.findById(user).orElse(null);

				emailService.sendNewEventCreatedByUser(event.getEventName(), u.getEmail());
				 
				System.out.println("email envoye a" + u.getName());
			}
			
			
		}
		/*
		 * List<Long> ID_d = eventRepo.GET_ID_BEST_DONNER();
		 * System.out.println("user plus donneur"); if(eventRepo.GET_ID_BEST_DONNER() !=
		 * null) { for (Long id : ID_d) {
		 * 
		 * System.out.println("no userbestdonnor");
		 * 
		 * User u = userRepo.findById(id).orElse(null);
		 * 
		 * emailService.sendNewEventCreatedByUser(event.getEventName(), u.getEmail());
		 * 
		 * System.out.println("email envoye a"+ u.getName()); } }
		 */
	}

	@Override
	public Long findUserDonationsById(Long id) {

		return eventRepo.findUserDonationsById(id);
	}

	@Override
	public ResponseEntity<?> Participer_event(Long userid, Long eventId) throws MessagingException, IOException, InterruptedException {
		SmsRequest smsrequest = new SmsRequest(null, null);
		User u = userRepo.findById(userid).orElse(null);
		Event event = eventRepo.findById(eventId).orElse(null);
		if (userid != event.getCreateurEvent().getUserId()) {

			Set<Event> ev = u.getJoinedEvents();
			ev.add(event);
			u.setJoinedEvents(ev);
			userRepo.save(u);

			Set<User> user = event.getParticipants();
			user.add(u);
			event.setParticipants(user);
			
			eventRepo.save(event);
			
			//sendSms(smsrequest, u.getPhoneNumber(), event.getEventName() + " : Participation avec succes");
			//emailService.sendNewEventCreatedByUser(event.getEventName(), u.getEmail());


		//	emailService.sendEmailForParticipationInEvent(event.getEventName()+  addressMapss(event.getEventId()) , u.getEmail());
			return new ResponseEntity(" participate success ", HttpStatus.OK);

		}

		else {

//			sendSms(smsrequest, u.getPhoneNumber(), event.getEventName()  + " : il y a plus de place");
			// System.out.println("Place complets");
	
			
			//return new ResponseEntity(" u are the creator of event or nbr of places are limited ", HttpStatus.OK);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("particpation non succes");
		}
	}

	@Override
	public void cancelparticipation(Long userid, Long eventId) {
		SmsRequest smsrequest = new SmsRequest(null, null);
		User user = userRepo.findById(userid).orElse(null);
		Event event = eventRepo.findById(eventId).orElse(null);
		user.getJoinedEvents().remove(event);
		userRepo.flush();
		//sendSms(smsrequest, user.getPhoneNumber() +  " : your invitation has been canceled", "");

	}
	// arefaire

	@Override
	public String TargetAtrbut(Long eventId) {
		float s = 0;
		Event e = eventRepo.findById(eventId).orElse(null);

		for (Donation d : e.getDonations()) {
			s = s + d.getAmountForEvent();

		}
		if (s >= e.getTargetDonation())
			return "Objectif attente";
		else
			return "object pas encore attendre";

	}

	public Event affecte_place_event_byavie(Long id_event) {
		List<String> avie = new ArrayList();
		avie.add("sousse");
		avie.add("monastir");
		avie.add("monastir");
		avie.add("monastir");
		avie.add("mahdia");
		avie.add("mahdia");
		avie.add("mahdia");
		avie.add("mahdia");
		String a = null;
		int x = 0;
		int y = 0;
		for (String string : avie) {
			y = 0;
			for (String string2 : avie) {
				if (string2.equals(string))
					y++;
			}
			if (y > x) {
				x = y;
				a = string;
			}
		}
		Event e = eventRepo.findById(id_event).orElse(null);
		// e.setPlace(a);

		return eventRepo.save(e);
	}

	@Override
	public void sendSms(SmsRequest smsRequest, String numberPhone, String msg) {
		smsRequest.setPhoneNumber(numberPhone);
		smsRequest.setMessage(msg);
		smsSender.sendSms(smsRequest);

	}

//-----------------------------pagination--------------------------------------------------------------------------//

	@Override
	public Page<Event> findEventWithPaginationAndSorting(int offset, int pageSize, String field) {
		Page<Event> events = eventRepo.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(field)));
		return events;
	}

	// --------------------------------crud simple non validé


	@Override
	public void removeEvent(Long IdEvent) {
		eventRepo.deleteById(IdEvent);

	}

	@Override
	public List<Event> Get_all_Event() {
		return eventRepo.findAll();

	}
	
	

	@Override
	public Event displayEvent(Long idEvent) {
		
		return eventRepo.findById(idEvent).get();
	}




	

	

	@Override
	public ResponseEntity<?> addComment_to_Event(eventComment eventcomment, Long idEvent, Long idUser) {
		Event event = eventRepo.findById(idEvent).orElse(null);
		User user = userRepo.findById(idUser).orElse(null);
	//	DetctaDataLoad(postComment.getCommentBody(),idUser);
	//	if (Filtrage_bad_word(postComment.getCommentBody()) == 0) {
			//postComment.setUser(u);
			//postComment.setPost(p);
		eventcomment.setUser(user);
		eventcomment.setEventt(event);
		eventcomRepo.save(eventcomment);
		//	postCommentRepo.save(postComment);
			return ResponseEntity.ok().body(eventcomment);
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
	//	return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("Bads Word Detected");
	}

	@Override
	public ResponseEntity<?> Update_Comment(eventComment eventcomment, Long idEventCom, Long idUser) {
		if (eventcomRepo.existsById(idEventCom)) {
			eventComment eventCom1 = eventcomRepo.findById(idEventCom)
					.orElseThrow(() -> new EntityNotFoundException("Comment not found"));
			//User user = userRepo.findById(idUser).orElseThrow(() -> new EntityNotFoundException("User not found"));
			//if (postCom1.getUser().equals(user)) {

			eventCom1.setCommentBody(eventcomment.getCommentBody());
			eventcomRepo.save(eventCom1);
				return ResponseEntity.ok().body(eventCom1);
			//} else {
			//	return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("No permission to delete this post ");
		//	}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment Not Founf");
		}
	}

	@Override
	public ResponseEntity<?> Delete_eventCom(Long idEventCom, Long idUser) {
		if (eventcomRepo.existsById(idEventCom)) {
			eventComment eventCom1 = eventcomRepo.findById(idEventCom)
					.orElseThrow(() -> new EntityNotFoundException("post not found"));
			User user = userRepo.findById(idUser).orElseThrow(() -> new EntityNotFoundException("User not found"));
			if (eventCom1.getUser().equals(user)) {
				eventcomRepo.delete(eventCom1);
				return ResponseEntity.ok().body("Delete success");
			} else {
				return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("No permission to delete this post");
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("post Not");
		}
	
	}

	@Override
	public Event deletEvent(Long idUser, Long idEvent) throws CourseNotExist, CourseOwnerShip {
		  User usr = userRepo.findById(idUser).get();
			Event e = eventRepo.findById(idEvent).orElse(null);
			if(e==null) {
				throw new CourseNotExist("This event does not exist");
			}
			if(e.getCreateurEvent().equals(usr)) {
				usr.getCreatedEvents().remove(e);
				eventRepo.delete(e);	
			}else {
				throw new CourseOwnerShip("You aren't the owner of this event");
			}
			
			return e ;
	}
	
	

	@Override
	public Event editEvent(Event e, Long idEvent, Long idUser) throws CourseNotExist, CourseOwnerShip {
	
		 User usr = userRepo.findById(idUser).get();
			Event event = eventRepo.findById(idEvent).orElse(null);
			if(event==null) {
				throw new CourseNotExist("This event does not exist");
			}
			
		 
				event.setEventName(e.getEventName());
			
			
				
				event.setBigDescription(e.getBigDescription());
			
			
				event.setDescription(e.getDescription());
			
				
				event.setStartAt(e.getStartAt());
				
			
				event.setEndAt(e.getEndAt());
				
				
				eventRepo.saveAndFlush(event);
		
		
			
			return e ;
	}


	@Override
	public List<Long> findEventYear() {
		return 	eventRepo.findEventYear();
	}

	@Override
	public ResponseEntity<?> getAllComment() {
	
		 return new ResponseEntity(	eventcomRepo.findAll(), HttpStatus.OK);
	}

	@Override
	public Donation addDonation_to_Event(Long idEvent, Long idUser, Donation donation) {
		Set<Event> eventList = new HashSet<Event>();
		Set<Donation> donationList = new HashSet<Donation>();

		User user = userRepo.findById(idUser).orElse(null);
		Event event = eventRepo.findById(idEvent).orElse(null);
		donationList.add(donation);
		user.setDonations(donationList);
		event.setDonations(donationList);
	
		return donationRepo.save(donation);
	}

	@Override
	public ResponseEntity<?> addImageForEvent2002(MultipartFile image, Long idEvent) throws IOException {
		Event e = eventRepo.findById(idEvent).orElse(null);
		BufferedImage bi = ImageIO.read(image.getInputStream());
		
		Map result = cloudImage.upload(image);
		
		Media media = new Media((String) 
				result.get("original_filename")
				, (String) result.get("url"),
				(String) result.get("public_id"));
		media.setEvents(e);
		mediaService.save(media);
	
		return ResponseEntity.status(HttpStatus.OK).body("Image added to adversting");
	
	}

	@Override
	public ResponseEntity<?> addressMapss(Long idEvent) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}






	

	// ------------------------------------------------------------------------------------------------------

}
