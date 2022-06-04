package tn.esprit.spring.websocketproject;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
public class Message implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long messageId;
	
	String body;
	
	@JsonIgnore
	@ManyToOne
	Chatroom chat;
}
