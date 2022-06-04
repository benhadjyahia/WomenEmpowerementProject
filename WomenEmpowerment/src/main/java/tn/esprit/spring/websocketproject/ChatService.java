package tn.esprit.spring.websocketproject;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import tn.esprit.spring.entities.User;
import tn.esprit.spring.repository.UserRepository;

@Service
public class ChatService {
	@Autowired
	ChatroomRepo chatroomRepo;
	@Autowired
	UserRepository ur;
	@Autowired
	MessageRepository mr;
	public Chatroom findchat(Long idsender , Long idreciver) {
		int x = 0;
		Chatroom cht =  new Chatroom();
		for (Chatroom ch : chatroomRepo.findAll()) {
			if (ch.getReciver().getUserId() == idreciver && ch.getSender().getUserId() == idsender || ch.getReciver().getUserId() == idsender && ch.getSender().getUserId() == idreciver) {
			x= 1;
			cht=ch;}
			
		}
		if (x== 1) { return cht;}
		else {
			Chatroom newc =  new Chatroom();
			newc.setSender(null);
			User s = ur.findById(idsender).orElse(null);
			User r = ur.findById(idreciver).orElse(null);
			newc.setReciver(r);
			newc.setSender(s);
			
			return chatroomRepo.save(newc);

		}
	}
	
	
/*	public void sendmessage(Message m , Long idchatroom) {
		Chatroom ch = chatroomRepo.findById(idchatroom).orElse(null);
		m.setChat(ch);
		mr.save(m);
		Set<Message> l = ch.getMessages();
		l.add(m);
		ch.setMessages(l);
		chatroomRepo.save(ch);
		
	}*/
	
	public Chatroom getConv(Long idchatroom) {
		Chatroom ch = chatroomRepo.findById(idchatroom).orElse(null);
		return ch;
		
	}
	public void changecolor(Long id, String s){
		Chatroom ch = chatroomRepo.findById(id).orElse(null);
		ch.setColor(s);
		chatroomRepo.save(ch);
	}

}
