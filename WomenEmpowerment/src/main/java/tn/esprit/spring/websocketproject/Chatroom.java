package tn.esprit.spring.websocketproject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import tn.esprit.spring.entities.Media;
import tn.esprit.spring.entities.Post;
import tn.esprit.spring.entities.PostComment;
import tn.esprit.spring.entities.PostDislike;
import tn.esprit.spring.entities.PostLike;
import tn.esprit.spring.entities.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@ToString
public class Chatroom implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long chatroomId;
	
	@JsonIgnore
	@ManyToOne 
	User sender;
	@JsonIgnore
	@ManyToOne 
	User reciver;
	
	
	String color = "#EC407A";

	
	@OneToMany(cascade = CascadeType.ALL , mappedBy = "chat")
	List<ChatMessage> messages;
}
