package tn.esprit.spring.entities;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.spring.enumerations.Job;
import tn.esprit.spring.enumerations.Role;
@Getter
@Setter

@AllArgsConstructor
@Entity
@Table(name = "cv")
public class CvInfo {
	  @Id
	  @GeneratedValue(generator = "uuid")
	  @GenericGenerator(name = "uuid", strategy = "uuid")
	  private String id;
	  private String name;
	  private String type;
	  @JsonIgnore
	  @Lob
	  private byte[] data;
	  
	  @OneToOne
		User candidate;

	  public CvInfo() {
	  }
	  public CvInfo(String name, String type, byte[] data) {
	    this.name = name;
	    this.type = type;
	    this.data = data;
	  }
	  public String getId() {
	    return id;
	  }
	  public String getName() {
	    return name;
	  }
	  public void setName(String name) {
	    this.name = name;
	  }
	  public String getType() {
	    return type;
	  }
	  public void setType(String type) {
	    this.type = type;
	  }
	  public byte[] getData() {
	    return data;
	  }
	  public void setData(byte[] data) {
	    this.data = data;
	  }
	
		
	
	  
	 
	}
