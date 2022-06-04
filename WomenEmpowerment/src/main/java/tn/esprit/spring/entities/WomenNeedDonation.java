package tn.esprit.spring.entities;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import tn.esprit.spring.enumerations.Job;
import tn.esprit.spring.enumerations.Role;
import tn.esprit.spring.enumerations.TypeHelpForDonation;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@ToString
@Table(name = "WomenNeedDonation")
public class WomenNeedDonation {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idWNDonation;
	
	private TypeHelpForDonation TypeHelp;
	
	private int priority;
	
       Boolean GetHelp;
	
	Float montantRecu;
	
	@Temporal(TemporalType.DATE)
	Date datePost;
	float montant_needed;
	@JsonIgnore
	@OneToOne
	private User user;
	
	

}
