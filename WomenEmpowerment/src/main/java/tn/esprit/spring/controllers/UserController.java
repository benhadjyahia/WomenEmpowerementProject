package tn.esprit.spring.controllers;



import tn.esprit.spring.entities.Candidacy;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Event;
import tn.esprit.spring.entities.Notification;
import tn.esprit.spring.entities.Offer;
import tn.esprit.spring.entities.Post;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.exceptions.FriendExist;
import tn.esprit.spring.repository.UserRepository;
import tn.esprit.spring.security.UserPrincipal;
import tn.esprit.spring.service.forum.ForumService;
import tn.esprit.spring.serviceInterface.EventService;
import tn.esprit.spring.serviceInterface.courses.CourseService;
import tn.esprit.spring.serviceInterface.offer.IOfferService;
import tn.esprit.spring.serviceInterface.user.UserService;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;


@RestController
@RequestMapping("api/user")//pre-path
public class UserController
{
		
    @Autowired
    private UserService userService;
    
    @Autowired 
    UserRepository userRepository;
    
    @Autowired
    ForumService forumService;
    
    @Autowired
    CourseService courseService;
    
    @Autowired
    EventService eventService;
    
    @Autowired
    IOfferService OfferService;
    
    
    @PutMapping("/update")
    public User updateUser(@RequestBody User user) {
    	return userService.updateUser(user);
    }
    
    @GetMapping("/all")
    public List<User> findAllUsers()
    {
        return userService.findAllUsers();
    }
    
    
    @GetMapping("/notifications")
    public List<Notification> findNotificationsByUser(@ApiIgnore @AuthenticationPrincipal UserPrincipal u) {
    	return userService.findNotificationsByUser(u.getId());
    }
    
    @PostMapping("/notification/save/{username}")
    public Notification addNotification(@RequestBody Notification notification,@PathVariable(name="username") String username) {
    	return userService.addNotification(notification, username);
    }
    
    @DeleteMapping("/notification/delete/{notificationId}")
    public void deleteNotification(@PathVariable(name="notificationId") Long notificationId) {
    	userService.deleteNotification(notificationId);
    }
    
    @GetMapping("/notifications/all")
    public List<Notification> findAllNotifications() {
    	return userService.findAllNotifications();
    }
    
    @GetMapping("/courses/{startDate}/{endDate}")
    public List<Course> findCoursesBetweenDates(@PathVariable(name="startDate") @DateTimeFormat(pattern = "yyyy-MM-dd")Date startDate,
    		@PathVariable(name="endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
    	return userService.findCoursesBetweenDates(startDate, endDate);
    }
    
    @PostMapping("/subscription/save")
    public Subscription addSubscription(@ApiIgnore @AuthenticationPrincipal UserPrincipal u) {
    	return userService.addSubscription(u.getUsername());
    }
    
  /*  @PutMapping("/subscription/extend/{username}/{nbMonths}")
    public void extendSubscription(@PathVariable(name="username") String username,@PathVariable(name="nbMonths") int nbMonths) {
    	userService.extendSubscription(username, nbMonths);
    }*/
    
    @DeleteMapping("/subscription/remove")
    public void removeSubcription(String username) {
    	userService.removeSubcription(username);
    }
    
    @GetMapping("/subscribed/all")
    public List<User> findSubscribedUsers() {
    	return userService.findSubscribedUsers();
    }
    
    @PostMapping("/friend/follow/{username2}")
    public void saveFriend(@PathVariable(value="username2") String username2,@ApiIgnore @AuthenticationPrincipal UserPrincipal u) throws FriendExist{
    	userService.saveFriend(u.getUsername(), username2);
    }
    
    @DeleteMapping("/friend/unfollow/{username2}")
    public void deleteFriend(@PathVariable(value="username2") String username2,@ApiIgnore @AuthenticationPrincipal UserPrincipal u){
    	userService.deleteFriend(u.getUsername(), username2);
    }

    
    
    @GetMapping("/friends")
    public List<User> getMyFriends(@ApiIgnore User u,@ApiIgnore @AuthenticationPrincipal UserPrincipal user){
    	u = userService.findByUsername(user.getUsername()).orElse(null);
    	return userService.getMyFriends(u);
    }
    
    @GetMapping("/friends2")
    public List<User> getMyFriends2(@RequestParam Long userId){
    	User u = userRepository.findById(userId).orElse(null);
    	return userService.getMyFriends(u);
    }
    
    @PutMapping("/notification/read")
    public void markNotifAsRead(@RequestBody Long idNotif) {
    	userService.markNotifAsRead(idNotif);
    }
    
    @PutMapping("/notification/unread")
    public void markNotifAsUnRead(@RequestBody Long idNotif) {
    	userService.markNotifAsUnRead(idNotif);
    }
    
    @GetMapping("/picture")
    public String getUserProfilPic(@ApiIgnore @AuthenticationPrincipal UserPrincipal user) {
    	return userService.getUserProfilPic(user.getId());
    }
    
    @GetMapping("/picture2")
    public String getUserProfilPic2(@RequestParam Long userId) {
    	return userService.getUserProfilPic(userId);
    }
    
    @GetMapping("/{userId}")
    public User getUser(@PathVariable(value="userId") Long userId) {
    	return userService.getUser(userId);
    }
    
    @GetMapping("/suggestions")
    public Set<User> getSuggestedUsers(@ApiIgnore @AuthenticationPrincipal UserPrincipal user) {
    	User u = userService.getUser(user.getId());
    	return userService.getSuggestedUsers(u);
    }
    
    @GetMapping("/suggestions2")
    public Set<User> getSuggestedUsers2(@ApiIgnore @AuthenticationPrincipal UserPrincipal user) {
    	User u = userService.getUser(user.getId());
    	return userService.getSuggestedUsers2(u);
    }
    
    @GetMapping("/common-friends")
    public List<User> FriendsInCommon(@ApiIgnore @AuthenticationPrincipal UserPrincipal user,@RequestParam Long userId2) {
    	Long userId1 = user.getId();
    	return userService.FriendsInCommon(userId1, userId2);
    }
    
	@GetMapping("/myPosts")
	public Set<Post> Get_post_by_User(@AuthenticationPrincipal UserPrincipal user){
		return forumService.Get_post_by_User(user.getId());
	}
	
	@GetMapping("/posts")
	public List<Post> Get_all_post(){
		return forumService.Get_all_post();
	}
	
	@GetMapping(path="/courses")
	public List<Course> getAllCourses(){
		return courseService.displayAllCourses();
	}
	
	@GetMapping("/events")
	public List<Event> Get_all_Event(){
		return eventService.Get_all_Event();
	}
	
	@GetMapping("/offers")
	public List<Offer> getOffers() {
		return OfferService.getAllOffers();
	}
    



    
}
