package tn.esprit.spring.serviceInterface.user;



import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.mail.MessagingException;

import org.springframework.web.multipart.MultipartFile;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import io.jsonwebtoken.io.IOException;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Notification;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.enumerations.Role;
import tn.esprit.spring.exceptions.EmailExist;
import tn.esprit.spring.exceptions.FriendExist;
import tn.esprit.spring.exceptions.UsernameExist;
import tn.esprit.spring.exceptions.UsernameNotExist;


public interface UserService
{
    User saveUser(User user) throws UsernameNotExist, UsernameExist, EmailExist, MessagingException, IOException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, TemplateException, java.io.IOException;

    Optional<User> findByUsername(String username);
    
    User getUser(Long userId);
    
	Optional<User> findByEmail(String email);

    void changeRole(Role newRole, String username);
    
    void makeAdmin(String username);

    List<User> findAllUsers();
    
    List<User> findSubscribedUsers();
    
    List<Notification> findNotificationsByUser(Long userId);
    
    Notification addNotification(Notification notification, String username);
    
    void deleteNotification(Long notificationId);
    
    List<Notification> findAllNotifications();
    
    List<Course> findCoursesBetweenDates(Date startDate, Date endDate);
    
    Subscription addSubscription(String username);
    
    //void extendSubscription(String username, int nbMonths);
    
    void removeSubcription(String username);

	void saveFriend(String username1, String username2) throws FriendExist;

	List<User> getMyFriends(User u);

	void unlockUser(String username);
	
	void lockUser(String username);
	
	void markNotifAsRead(Long idNotif);
	
	void markNotifAsUnRead(Long idNotif);

	User updateUser(User user);
	
	String getUserProfilPic(Long userId);
	
	Set<User> getSuggestedUsers(User u);
	
	Set<User> getSuggestedUsers2(User u);
	
	List<User> FriendsInCommon(Long userId1, Long userId2);

	void deleteFriend(String username1, String username2);

	List<User> usersNumberJanuary(int id);

	List<User> subscribedUsersNumberMonth(int id);

	List<String> getRegistredCountries();

	List<Long> numberRegistrationByCountry();
	
	public List<User> allAdmins();



    
}
