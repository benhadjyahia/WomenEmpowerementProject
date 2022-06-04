package tn.esprit.spring.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import tn.esprit.spring.enumerations.EventType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@ToString
public class Event implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long eventId;
	
	String eventName;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	
	Date createdAt;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	Date StartAt;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	Date endAt;
	
	String description;
	


	

	
	float TargetDonation;
	String address;

	String lang;
	String latitude;
	
	
	String bigDescription;
	
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "joinedEvents")
	Set<User> participants;
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
	Set<Donation> donations;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "events")
	Set<Media> medias;
	
	
	
	@ManyToOne
	User createurEvent;
	
	
	float montantCollecte;
	
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "eventt")
	Set<eventComment> eventcomment;

	
}
