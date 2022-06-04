package tn.esprit.spring.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
public class PostLike implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long postLikeId;
	
	Date likedAt;
	
	Boolean isLiked ;
	
	
	@ManyToOne
	User user; // The user who clicked Like
	
	@JsonIgnore
	@ManyToOne
	Post post; // The post to like
	

}
