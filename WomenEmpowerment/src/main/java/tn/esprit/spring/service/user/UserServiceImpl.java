package tn.esprit.spring.service.user;


import org.apache.commons.lang3.StringUtils;
import org.cryptacular.bean.EncodingHashBean;
import org.cryptacular.spec.CodecSpec;
import org.cryptacular.spec.DigestSpec;
import org.passay.CharacterRule;
import org.passay.DigestHistoryRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.MessageResolver;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.PropertiesMessageResolver;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Friend;
import tn.esprit.spring.entities.Notification;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.enumerations.Role;
import tn.esprit.spring.exceptions.EmailExist;
import tn.esprit.spring.exceptions.FriendExist;
import tn.esprit.spring.exceptions.PasswordValidException;
import tn.esprit.spring.exceptions.UsernameExist;
import tn.esprit.spring.exceptions.UsernameNotExist;
import tn.esprit.spring.repository.CourseRepository;
import tn.esprit.spring.repository.FriendRepository;
import tn.esprit.spring.repository.MediaRepo;
import tn.esprit.spring.repository.NotificationRepository;
import tn.esprit.spring.repository.SubscriptionRepository;
import tn.esprit.spring.repository.UserRepository;
import tn.esprit.spring.serviceInterface.user.UserService;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import javax.mail.MessagingException;


@Slf4j
@Service
public class UserServiceImpl implements UserService
{
	
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    NotificationRepository notificationRepository;
    
    @Autowired
    CourseRepository courseRepository;
    
    @Autowired
    SubscriptionRepository subscriptionRepository;
    
    @Autowired
    ServiceAllEmail emailService;
    
    @Autowired
    FriendRepository friendRepository;
    
    @Autowired
    MediaRepo mediaRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(User user) throws UsernameNotExist, UsernameExist, EmailExist, MessagingException, io.jsonwebtoken.io.IOException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, TemplateException, IOException
    {
    	isvalidUsernameAndEmail(EMPTY, user.getUsername(), user.getEmail());
    	isValid(user.getPassword());
        //user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setLocked(false);
        user.setLoginAttempts(0);
        user.setRegistrationDate(new Date());
        User savedUser = userRepository.save(user);
        emailService.sendWelcomeMail(savedUser.getName(), savedUser.getEmail());
        return savedUser;
    }
    
	@Override
    public User updateUser(User user) {
    	return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username)
    { 
        return userRepository.findByUsername(username);
    }
    
    @Override
    public Optional<User> findByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional //Transactional is required when executing an update/delete query.
    public void changeRole(Role newRole, String username)
    {
        userRepository.updateUserRole(username, newRole);
    }

    @Override
    public List<User> findAllUsers()
    {
        return userRepository.findAll();
    }

	@Override
	@Transactional //Transactional is required when executing an update/delete query.
	public void makeAdmin(String username) {
		userRepository.makeAdmin(username);
		
	}
	
	@Override
	public void unlockUser(String username) {
		User u = userRepository.findByUsername(username).get();
		u.setLoginAttempts(0);
		u.setLocked(false);
		userRepository.save(u);
	}

	@Override
	public List<Notification> findNotificationsByUser(Long userId) {
		return notificationRepository.userNotification(userId);
	}

	@Override
	public Notification addNotification(Notification notification, String username) {
		User user = userRepository.findByUsername(username).get();
		notification.setRead(false);
		notification.setUser(user);
		return notificationRepository.save(notification);
	}

	@Override
	public void deleteNotification(Long notificationId) {
		Notification notif = notificationRepository.findById(notificationId).orElse(null);
		notificationRepository.delete(notif);
		
	}

	@Override
	public List<Notification> findAllNotifications() {
		// TODO Auto-generated method stub
		return notificationRepository.findAll();
	}

	@Override
	public List<Course> findCoursesBetweenDates(Date startDate, Date endDate) {
		List<Course> courses = courseRepository.findAll();
		List<Course> availableCourses = new ArrayList<>();
		for (Course c : courses) {
			if (c.getStartDate().compareTo(startDate) < 0 && c.getStartDate().compareTo(endDate) > 0)
				availableCourses.add(c);
		}
		return availableCourses;
	}

	@Override
	public List<User> findSubscribedUsers() {
		return userRepository.subscribedUsers();
	}

	@Override
	public Subscription addSubscription(String username) {
		Subscription s = new Subscription();
		s.setSubscriptionDate(new Date());
		Subscription sub =  subscriptionRepository.save(s);
		User u = userRepository.findByUsername(username).orElse(null);
		u.setSubscription(sub);
		userRepository.save(u);
		return sub;
	}



	@Override
	public void removeSubcription(String username) {
		User u = userRepository.findByUsername(username).orElse(null);
		Subscription s = u.getSubscription();
		u.setSubscription(null);
		subscriptionRepository.delete(s);
	}
	
	
	
	
	 private User isvalidUsernameAndEmail(String currentUsername, String newUsername, String newEmail) 
			 throws UsernameNotExist, UsernameExist, EmailExist {
	        User userByNewUsername = findByUsername(newUsername).orElse(null);
	        User userByNewEmail = findByEmail(newEmail).orElse(null);
	        if(StringUtils.isNotBlank(currentUsername)) {
	            User currentUser = findByUsername(currentUsername).orElse(null);
	            if(currentUser == null) {
	                throw new UsernameNotExist("No user found by username: " + currentUsername);
	            }
	            if(userByNewUsername != null && !currentUser.getUserId().equals(userByNewUsername.getUserId())) {
	                throw new UsernameExist("Username already exists");
	            }
	            if(userByNewEmail != null && !currentUser.getUserId().equals(userByNewEmail.getUserId())) {
	                throw new EmailExist("Email are already exists");
	            }
	            return currentUser;
	        } else {
	            if(userByNewUsername != null) {
	                throw new UsernameExist("Username already exists");
	            }
	            if(userByNewEmail != null) {
	                throw new EmailExist("Email are already exists");
	            }
	            return null;
	        }
	    }
	 
	 @SneakyThrows
	 public boolean isValid(String password) {
		 String messageTemplate = null;
		 Properties props = new Properties();
		 InputStream inputStream = getClass().getClassLoader().getResourceAsStream("passay.properties");
		 try {
			 props.load(inputStream);
		 } catch (IOException e) {
			 e.printStackTrace();
	     	}
		 MessageResolver resolver = new PropertiesMessageResolver(props);


		 List<PasswordData.Reference> history = Arrays.asList(
				 // Password=P@ssword1
				 new PasswordData.HistoricalReference(
	                        "SHA256",
	                        "j93vuQDT5ZpZ5L9FxSfeh87zznS3CM8govlLNHU8GRWG/9LjUhtbFp7Jp1Z4yS7t"),

	                // Password=P@ssword2
				 new PasswordData.HistoricalReference(
	                        "SHA256",
	                        "mhR+BHzcQXt2fOUWCy4f903AHA6LzNYKlSOQ7r9np02G/9LjUhtbFp7Jp1Z4yS7t"),

	                // Password=P@ssword3
				 new PasswordData.HistoricalReference(
	                        "SHA256",
	                        "BDr/pEo1eMmJoeP6gRKh6QMmiGAyGcddvfAHH+VJ05iG/9LjUhtbFp7Jp1Z4yS7t")
	        );
	        EncodingHashBean hasher = new EncodingHashBean(
	                new CodecSpec("Base64"), // Handles base64 encoding
	                new DigestSpec("SHA256"), // Digest algorithm
	                1, // Number of hash rounds
	                false); // Salted hash == false

	        PasswordValidator validator = new PasswordValidator(resolver, Arrays.asList(

	                // length between 8 and 16 characters
	                new LengthRule(8, 16),

	                // at least one upper-case character
	                new CharacterRule(EnglishCharacterData.UpperCase, 1),

	                // at least one lower-case character
	                new CharacterRule(EnglishCharacterData.LowerCase, 1),

	                // at least one digit character
	                new CharacterRule(EnglishCharacterData.Digit, 1),

	                // at least one symbol (special character)
	                new CharacterRule(EnglishCharacterData.Special, 1),


	                // no whitespace
	                new WhitespaceRule(),

	                // rejects passwords that contain a sequence of >= 3 characters alphabetical  (e.g. abc, ABC )
	                new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 3, false),
	                // rejects passwords that contain a sequence of >= 3 characters numerical   (e.g. 123)
	                new IllegalSequenceRule(EnglishSequenceData.Numerical, 3, false)

	                ,new DigestHistoryRule(hasher)

	               ));

	        RuleResult result = validator.validate(new PasswordData(password));


	        PasswordData data = new PasswordData("P@ssword1", password);//"P@ssword1");
	        data.setPasswordReferences(history);
	        RuleResult result2 = validator.validate(data);


	        if (result.isValid() ) {
	            return true;
	        }
	        try {
	            if (result.isValid()==false) {
	                List<String> messages = validator.getMessages(result);

	                messageTemplate = String.join(",", messages);

	                System.out.println("Invalid Password: " + validator.getMessages(result));
	                }
	               } finally {
	            throw new PasswordValidException(messageTemplate);

	        }

	    }
	 
	 
	 @Override
	 public void saveFriend(String username1, String username2) throws FriendExist{


	        Friend friend = new Friend();
	        User user1 = userRepository.findByUsername(username1).orElse(null);
	        User user2 = userRepository.findByUsername(username2).orElse(null);
	        User firstuser = user1;
	        User seconduser = user2;
	        if( !(friendRepository.existsBySenderAndReceiver(firstuser,seconduser)) && (user1.getUserId() != user2.getUserId()) && (user2 != null) ){
	            friend.setCreatedAt(new Date());
	            friend.setSender(firstuser);
	            friend.setReceiver(seconduser);
	            friendRepository.save(friend);
	            Notification notif = new Notification();
	            notif.setCreatedAt(new Date());
	            notif.setMessage(firstuser.getName() +  " Started following you !");
	            notif.setRead(false);
	            notif.setUser(seconduser);
	            notificationRepository.save(notif);
	        }
	        else {
	        	throw new FriendExist("Error processing friend request !");
	        }
	    }
	 

	 @Override
	 public void deleteFriend(String username1, String username2){
	        User user1 = userRepository.findByUsername(username1).orElse(null);
	        User user2 = userRepository.findByUsername(username2).orElse(null);
	        Friend friend = new Friend();
	        List<Friend> myFriends = getMyFriends2(user1);
	        for (Friend f : myFriends) {
	        	if (f.getSender().getUserId() == user1.getUserId() && (f.getReceiver().getUserId() == user2.getUserId()) ) {
	        		friend = f;
	        	}
	        }
	        friendRepository.delete(friend);
	    }
	 
	 @Override
	 public List<User> getMyFriends(User u){
		 List<Friend> allFriends = friendRepository.findAll();
		 Set<User> myFriends = new HashSet<>();
		 for (Friend f : allFriends) {
			 if (f.getSender().getUserId() == u.getUserId() ) {
				 myFriends.add(f.getReceiver());
			 }
		 }
		 List<User> friends = new ArrayList<>(myFriends);
		 return friends;
	 }
	 

	 public List<Friend> getMyFriends2(User u){
		 List<Friend> allFriends = friendRepository.findAll();
		 List<Friend> myFriends = new ArrayList<>();
		 for (Friend f : allFriends) {
			 if (f.getSender().getUserId() == u.getUserId() ) {
				 myFriends.add(f);
			 }
		 }
		 return myFriends;
	 }

	@Override
	public void lockUser(String username) {
		User u = userRepository.findByUsername(username).get();
		u.setLoginAttempts(0);
		u.setLocked(true);
		userRepository.save(u);
		
	}

	@Override
	public void markNotifAsRead(Long  idNotif) {
		Notification notification = notificationRepository.findById(idNotif).orElse(null);
		notification.setRead(true);
		notificationRepository.save(notification);
		
	}

	@Override
	public void markNotifAsUnRead(Long idNotif) {
		Notification notification = notificationRepository.findById(idNotif).orElse(null);
		notification.setRead(false);
		notificationRepository.save(notification);
		
	}

	@Override
	public String getUserProfilPic(Long userId) {
		User user = userRepository.findById(userId).orElse(null);
		return user.getProfilPicture().getImagenUrl();
	}

	@Override
	public User getUser(Long userId) {
		User user = userRepository.findById(userId).orElse(null);
		return user;
	}

	@Override
	public Set<User> getSuggestedUsers(User u) {
		List<Friend> allFriends = friendRepository.findAll();
		List<User> myFriends = getMyFriends(u);
		Set<User> suggestedFriends = new HashSet<>();
		
		
		for (Friend f : allFriends) {
			for (User myFriend : myFriends) {
				if ( (f.getSender().getUserId() == myFriend.getUserId()) && (f.getReceiver().getUserId() != u.getUserId()) && (!(myFriends.contains(f.getReceiver()))) ) {
					suggestedFriends.add(f.getReceiver());
				}
			}	
		}
			
		return suggestedFriends;
	}

	@Override
	public Set<User> getSuggestedUsers2(User u) {
		List<Friend> allFriends = friendRepository.findAll();
		List<User> myFriends = getMyFriends(u);
		Set<User> suggestedFriends = new HashSet<>();
		int count = 0;
		
		
		for (Friend f : allFriends) {
			for (User myFriend : myFriends) {
				if ( (f.getSender().getUserId() == myFriend.getUserId()) && (f.getReceiver().getUserId() != u.getUserId()) && (!(myFriends.contains(f.getReceiver()))) && (count != 8) ) {
					suggestedFriends.add(f.getReceiver());
					count++;
				}
			}	
		}
			
		return suggestedFriends;
	}

	@Override
	public List<User> FriendsInCommon(Long userId1, Long userId2) {
		User u1 = userRepository.findById(userId1).orElse(null);
		User u2 = userRepository.findById(userId1).orElse(null);
		List<Friend> myFriendList = getMyFriends2(u1);
		Set<User> FriendsInCommon = new HashSet<>();
		
		for (Friend f : myFriendList) {
			List<Friend> externalList = getMyFriends2(f.getReceiver());
			
			for (Friend f2: externalList) {
				System.err.println("Sender:" + f2.getSender().getUserId() + "----Receiver:" +f2.getReceiver().getUserId()+"\n------------------\n" );
				if (f2.getReceiver().getUserId() == userId2) {
					FriendsInCommon.add(f2.getSender());
				}
			}
		}	
		List<User> commonFriends = new ArrayList<>(FriendsInCommon);
		return commonFriends;
	}

	@Override
	public List<User> usersNumberJanuary(int id){
		List<User> users = userRepository.findAll();
		List<User> result = new ArrayList<>();
		for (User u : users) {
			if (u.getRegistrationDate().getMonth() == id) {
				result.add(u);
			}
		}
		return result;
	}
	
	@Override
	public List<User> subscribedUsersNumberMonth(int id){
		List<User> users = userRepository.subscribedUsers();
		List<User> result = new ArrayList<>();
		for (User u : users) {
			if (u.getRegistrationDate().getMonth() == id) {
				result.add(u);
			}
		}
		return result;
	}
	
	@Override
	public List<String> getRegistredCountries(){
		List<User> users = userRepository.findAll();
		Set<String> countries = new HashSet<>();
		for (User u : users) {
			countries.add(u.getCountry());
		}
		List<String> result = new ArrayList<>(countries);
		return result;
	}
	
	@Override
	public List<Long> numberRegistrationByCountry(){
		List<String> countries = getRegistredCountries();
		List<User> users = userRepository.findAll();
		List<Long> result = new ArrayList<>();
		for (String country : countries){ 
			Long i = 0L;
			for (User u : users) {
				if (country.equals(u.getCountry())) {
					i++;
				}
			}
			result.add(i);
		}
		return result;
		
	}
	
	@Override
	public List<User> allAdmins(){
		List<User> users = userRepository.findAll();
		List<User> result = new ArrayList<>();
		for (User u : users) {
			if (u.getRole().name().equals("ADMIN")) {
				result.add(u);
			}
		}
		return result;
	}
	
	
	 
	 


}
