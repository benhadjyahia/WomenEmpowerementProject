package tn.esprit.spring.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tn.esprit.spring.entities.Donation;
import tn.esprit.spring.entities.Event;
import tn.esprit.spring.entities.Post;
import tn.esprit.spring.entities.PostComment;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.enumerations.Role;
import tn.esprit.spring.repository.DonationRepo;
import tn.esprit.spring.repository.PostCommentRepo;
import tn.esprit.spring.repository.PostRepo;
import tn.esprit.spring.security.UserPrincipal;
import tn.esprit.spring.serviceInterface.EventService;
import tn.esprit.spring.serviceInterface.user.UserService;


@RestController
@RequestMapping("api/admin")//pre-path
public class AdminController
{
    @Autowired
    private UserService userService;
    
	@Autowired
	DonationRepo donationRepo;
	
	@Autowired
	PostCommentRepo postCommentRepo;
	
	@Autowired
	PostRepo postRepo;

    @GetMapping("all")//api/admin/all
    public ResponseEntity<?> findAllUsers()
    {
        return ResponseEntity.ok(userService.findAllUsers());
    }
    
    @PutMapping("change/{role}")//api/user/change/{role}
    public ResponseEntity<?> changeRole(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Role role)
    {
        userService.changeRole(role, userPrincipal.getUsername());

        return ResponseEntity.ok(true);
    }
    
    @PutMapping("makeAdmin/{username}")
    public ResponseEntity<?> makeAdmin(@PathVariable(value="username") String username) {
    	userService.makeAdmin(username);
    	return ResponseEntity.ok(true);
    }
    
    @PutMapping("/lock")
    public ResponseEntity<?> lockUser(@RequestBody String username) {
    	userService.lockUser(username);
    	return ResponseEntity.ok(true);
    }
    
    @PutMapping("/unlock")
    public ResponseEntity<?> unlockUser(@RequestBody String username) {
    	userService.unlockUser(username);
    	return ResponseEntity.ok(true);
    }
    
    
    @GetMapping("/subscribed")
    public List<User> findSubscribedUsers() {
    	return userService.findSubscribedUsers();
    }
    
    @GetMapping("/usersByMonth")
    public List<User> usersNumberByMonh(@RequestParam int id){
    	return userService.usersNumberJanuary(id);
    }
    
    @GetMapping("/subscribedUsersByMonth")
    public List<User> subscribedUsersNumberByMonh(@RequestParam int id){
    	return userService.subscribedUsersNumberMonth(id);
    }
    
    @GetMapping("/countries")
    public List<String> getRegistredCountries(){
    	return userService.getRegistredCountries();
    }
    
    @GetMapping("numberByCountry")
	public List<Long> numberRegistrationByCountry(){
    	return userService.numberRegistrationByCountry();
    }
    
    @GetMapping("/admins")
    public List<User> allAdmins(){
    	return userService.allAdmins();
    }
    
    @GetMapping("/transactionsNumber")
    public List<Donation> getAllDonations(){
    	return donationRepo.findAll();
    }
    
    @GetMapping("/allPosts")
    public List<Post> getAllPosts(){
    	return postRepo.findAll();
    }
    
    @GetMapping("/allComments")
    public List<PostComment> getAllComments(){
    	return postCommentRepo.findAll();
    }
 
}
