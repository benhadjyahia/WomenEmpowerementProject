package tn.esprit.spring.websocketproject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// The chat message-handling Controller
@RestController
@RequestMapping("/chatt")
public class ChatController {
	@Autowired
	ChatmessageRepo mr;
	@Autowired
	ChatroomRepo cr;
	
    // mapped to handle chat messages to the /sendmsg destination
    @MessageMapping("/sendmsg")
    // the return value is broadcast to all subscribers of /chat/messages
    @SendTo("/chat/messages")
    public ChatMessage chat(ChatMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        Chatroom ch = cr.findById(message.idchat).orElse(null);
        message.setChat(ch);
        mr.save(message);
        return new ChatMessage(message.messageId,message.getText(), message.getUsername(), message.getAvatar(),message.getSender(),message.idchat,message.chat);
    }
    
}
