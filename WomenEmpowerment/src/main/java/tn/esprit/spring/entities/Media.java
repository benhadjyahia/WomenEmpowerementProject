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
public class Media implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long mediaId;
	
	
	
	private String name;
	
    private String imagenUrl;
    
    private String codeImage;

    
	@JsonIgnore
    @ManyToOne
    Event events;
   
    
    @JsonIgnore
    @ManyToOne
    Post post;
    
    @ManyToOne
    Advertising advertising;
    
    
	public Media(String name, String imagenUrl, String imagencode) {
	
		this.name = name;
		this.imagenUrl = imagenUrl;
		this.codeImage = imagencode;
	}
    
    
    
    
    
    

}
