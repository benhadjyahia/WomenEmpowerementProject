package tn.esprit.spring.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import tn.esprit.spring.enumerations.CandidacyState;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@ToString
public class PostComment implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long postCommentId;
	
	String commentBody;
	
	Date commentedAt;
	
	
	@ManyToOne
	User user; // The user who wants to comment
	
	@JsonIgnore
	@ManyToOne
	Post post; // The post to comment

	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "postCo")
	Set<PostComment> postComments; //Reflexive association : A comment can have multiple replies
	@JsonIgnore
	@ManyToOne
	PostComment postCo;
	

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "postComment")
	Set<CommentLike> commentLikes;

}
