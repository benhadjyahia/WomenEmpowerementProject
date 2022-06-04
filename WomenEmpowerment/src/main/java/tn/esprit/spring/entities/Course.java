package tn.esprit.spring.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import tn.esprit.spring.enumerations.Domain;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@ToString
public class Course implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long courseId;
	
	String courseName;
	
	int nbHours;
	
	@Temporal(TemporalType.DATE)
	Date startDate;
	
	@Temporal(TemporalType.DATE)
	Date endDate;
	
	@OneToMany( mappedBy = "course",fetch = FetchType.EAGER)
	Set<Certificate> certificates;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)

	Set<Quiz> quiz; // Quizzes related to course (Unidirectionnelel)
	
	boolean onGoing;
	@Enumerated(EnumType.STRING)
	Domain domain;
	String channelId;
	String calendarId;
	@OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	Set<FileInfo> files;
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	Set<User> buser;
	@JsonIgnore
	@OneToMany(mappedBy = "course")
	Set<CourseCalEvent> courseEvents;
	String streamKey;
}
