package tn.esprit.spring.controllers;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;
import tn.esprit.spring.entities.Post;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.repository.UserRepository;
import tn.esprit.spring.security.UserPrincipal;
import tn.esprit.spring.service.forum.ForumService;
import tn.esprit.spring.websocketproject.ChatService;
import tn.esprit.spring.websocketproject.Chatroom;
import tn.esprit.spring.websocketproject.ChatroomRepo;
import tn.esprit.spring.websocketproject.Message;

@RestController
@RequestMapping("/chat")
public class ChatControllerr {
	@Autowired
	ChatService cs ;
	@Autowired
	UserRepository ur ;
	
	@Autowired
	ChatroomRepo cr ;
	
	@GetMapping("/Chatroom/{Idsender}/{idreciver}")
	@ResponseBody
	public Chatroom chatfind(@PathVariable("Idsender") Long Idsender,@PathVariable("idreciver") Long idreciver) {
	return cs.findchat(Idsender, idreciver);
	}
	
	/*@PostMapping("/send/{idreciver}")
	@ResponseBody
	public void send(@RequestBody Message m,@PathVariable("idreciver") Long idreciver) {
	 cs.sendmessage(m, idreciver);
	}*/
	
	@PostMapping("/getc/{idreciver}")
	@ResponseBody
	public Chatroom getcon(@PathVariable("idreciver") Long idreciver) {
	 return cs.getConv(idreciver);
	}
	
	@GetMapping("/ListUser")
	@ResponseBody
	public List<User> getListUser() {
	 return ur.findAll();
	}
	
	@GetMapping("/allchat")
	@ResponseBody
	public List<Chatroom> getChat() {
	 return cr.findAll();
	}
	
	@PostMapping("/color/{id}")
	@ResponseBody
	public void color(@PathVariable("id") Long id ,@RequestBody String c) {
	 cs.changecolor(id, c);
	}
	
}
