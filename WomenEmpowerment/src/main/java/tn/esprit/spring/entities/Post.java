package tn.esprit.spring.entities;

import java.io.Serializable;
import java.util.Date;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@ToString
public class Post implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long postId;
	
	String postTitle;
	
	Date createdAt;
	
	String body;

	
	int nb_Signal;
	
	int nb_etoil;
	

	@ManyToOne 
	User user;
	
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
	Set<PostLike> postLikes;
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
	Set<PostDislike> postDislikes;
	

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
	Set<PostComment> postComments;
	
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	Set<User> reportedby;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
	Set<Media> medias;
}
