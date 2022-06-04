package tn.esprit.spring.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

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
public class Candidacy implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long candidacyId;
	
	boolean isBookmarked;
	
	@Enumerated(EnumType.STRING)
	CandidacyState candidacyState;
	
	@ManyToOne
	User candidate;

	
	@ManyToOne
	Offer offer; 
	
	@OneToOne
	Interview interview;
	
	@JsonIgnore
	@OneToOne
	CvInfo CV;
	
	
	public void setUser(User user) {
		// TODO Auto-generated method stub
		
	}

}
