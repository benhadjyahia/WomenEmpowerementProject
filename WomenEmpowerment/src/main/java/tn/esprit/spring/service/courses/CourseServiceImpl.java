package tn.esprit.spring.service.courses;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.nylas.RequestFailedException;
import com.sun.xml.bind.v2.runtime.reflect.Lister.CollectionLister;

import tn.esprit.spring.entities.Answer;
import tn.esprit.spring.entities.Certificate;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Notification;
import tn.esprit.spring.entities.Quiz;
import tn.esprit.spring.entities.QuizQuestion;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.enumerations.Role;
import tn.esprit.spring.exceptions.CourseNotExist;
import tn.esprit.spring.exceptions.CourseOwnerShip;
import tn.esprit.spring.exceptions.CoursesLimitReached;
import tn.esprit.spring.repository.CourseRepository;
import tn.esprit.spring.repository.NotificationRepository;
import tn.esprit.spring.repository.QuizzRepository;
import tn.esprit.spring.repository.UserRepository;
import tn.esprit.spring.serviceInterface.courses.CourseService;
@Service
public class CourseServiceImpl implements CourseService {
@Autowired
CourseRepository courseRepository;
@Autowired
UserRepository userRepository;
@Autowired
QuizzRepository quizzRepository; 
@Autowired
CourseCalendarServiceImpl courseCalendarServiceImpl;
@Autowired
NotificationRepository notificationRepository;
	@Override
	public Course addCourse(Course c) {
		
		return courseRepository.save(c);
	}
	@Override
	public Course editCourse(Course c,Long courseId,Long userId) throws CourseNotExist,CourseOwnerShip {
		User user = userRepository.findById(userId).get();
		Course course = courseRepository.findById(courseId).orElse(null);
		if(!user.getCreatedCourses().contains(course)) {
			throw new CourseOwnerShip("You aren't the owner of this course");
		}
		if(course==null) {
			throw new CourseNotExist("This course does not exist");
			
		}
		else {
		course.setCourseName(c.getCourseName());
		course.setEndDate(c.getEndDate());
		course.setNbHours(c.getNbHours());
		course.setStartDate(c.getStartDate());
		course.setOnGoing(c.isOnGoing());
		course.setDomain(c.getDomain());
		courseRepository.flush();
		return c;
		}
		
	}

	@Override
	public void affectCourseToUser(Long idUser, Course c) throws CoursesLimitReached {
		if(courseVerificator(idUser)==true) {
		
		User usr = userRepository.findById(idUser).get();
		Set<Course> courses = usr.getCreatedCourses();
		courses.add(c);
		userRepository.save(usr);
		


				Notification notif = new Notification();
	            notif.setCreatedAt(new Date());
	            notif.setMessage(c.getCourseName() +" has been successfully created !");
	            notif.setRead(false);
	            notif.setUser(usr);
	            notificationRepository.save(notif);
		
		}
		else {
		throw new CoursesLimitReached("Limit reached : The maximum ongoing courses is 2 ");
	}
	}
	@Scheduled(cron= "0/10 * * * * *")
 public void verifyCourseCalendar() throws IOException, RequestFailedException {
	 List<Course> courses = courseRepository.findAll();
	 for (Course course : courses) {
		if(course.getCalendarId()==null) {
			courseCalendarServiceImpl.createCal(course.getCourseId());
		}
	}
 }
	@Override
	public Course deleteCourse(Long idUser, Long idCourse) throws CourseNotExist, CourseOwnerShip {
		    User usr = userRepository.findById(idUser).get();
			Course c = courseRepository.findById(idCourse).orElse(null);
			if(c==null) {
				throw new CourseNotExist("This course does not exist");
			}
			if(usr.getCreatedCourses().contains(c)==false) {
				throw new CourseOwnerShip("You aren't the owner of this course");
			}
			usr.getCreatedCourses().remove(c);
			courseRepository.delete(c);
			return c ;
		
	}
	@Override
	public void createQuizz(Quiz Q, Long idCourse,Long idUser) throws CourseOwnerShip {
		Course c = courseRepository.findById(idCourse).get();
		User usr = userRepository.findById(idUser).get();
		if(usr.getCreatedCourses().contains(c)==false) {
			throw new CourseOwnerShip("You aren't the owner of this course");
		}
		Set<Quiz> quiz = new HashSet<>();
		quiz.add(Q);
		c.getQuiz().add(Q);
	
		courseRepository.flush();
		quizzRepository.save(Q);
		
	}
	@Override
	public List<Course> displayAllCourses() {
		return courseRepository.findAll();
	}
	@Override
	public Course displayCourse(Long courseId) {
		return courseRepository.findById(courseId).get();
	}
	@Override
	public List<User> getAllParticipants(Long courseId) {
		Course course = courseRepository.findById(courseId).get();
		List<User> users = new ArrayList<>();
		Set<Certificate> c = course.getCertificates();
		for (Certificate certificate : c) {
			users.add(certificate.getUser());
		}
		if(users.size()==0) {
			return null;
		}
		else
		{
		return users;
		}
	}
	@Override
	public User getParticipant(Long userId) {
		Long id = courseRepository.findUserById(userId);
		if(id==null) {
			return null;
		}
		else {
			return userRepository.findById(id).get();
		}
		
		
		
	}
	
	@Override
	@Scheduled(cron= "0/10 * * * * *")
	public void coursesStatus() {
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		  Date date = new Date(); 
		  
		List<Course> courses = courseRepository.findAll();
		
		for (Course course : courses) {
			if(course.getStartDate().toString().equals(formatter.format(date))) {
				course.setOnGoing(true);
				courseRepository.save(course);
			}
			
		}
		
	}
	@Override
	@Scheduled(cron= "0/10 * * * * *")
	public void coursesEnded() {
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		  Date date = new Date(); 
		  
		  
		List<Course> courses = courseRepository.findAll();
		
		for (Course course : courses) {
			if(course.getEndDate().toString().equals(formatter.format(date)) || course.getEndDate().before(date)) {
				course.setOnGoing(false);
				courseRepository.save(course);
				
				
			}
			
		}
		
	}
	@Override
	public boolean courseVerificator(Long userId) {
		int counter=0;
		User user = userRepository.findById(userId).get();
		Set<Course> userCourses = user.getCreatedCourses();
		for (Course course : userCourses) {
			if(course.isOnGoing()==true) {
				counter = counter +1 ;
			}
		    
		}
		if(counter < 2) return true;
		else return false;
	}
	@Override
	public int userjoinCourseVerificator(Long userId,Long courseId) {
		int counter=0;
		String date ="";
		Course c = courseRepository.findById(courseId).get();
		List<String> bothdates = new ArrayList<String>() ;
		List<String> userJoinedCourses = courseRepository.getUserJoinedCourses(userId,c.getDomain().toString());
		System.err.println(userJoinedCourses);
		if(userJoinedCourses.isEmpty() || userJoinedCourses.size()<2) {
			return 100;
		}
		else {
		for (String userj : userJoinedCourses) {
			String [] dato = userj.split(",");
			date= dato[3];
			bothdates.add(date);
			System.err.println(date);
		}
		System.err.println(bothdates);
		  Period diff = diffCalculator(bothdates.get(0), bothdates.get(1));
		  Period diff1 = diffCalculator(bothdates.get(1),c.getStartDate().toString());
			if(diff.getYears()!=0 || diff1.getYears()!=0) {
				return 100;
			}
			else {
			counter= counter + diff.getMonths();
			counter= counter + diff1.getMonths();
			System.err.println(counter);
			
		return counter;
			}
	}
	}
	@Override
	public Period diffCalculator(String date1,String date2) {
		return Period.between(
	            LocalDate.parse(date1).withDayOfMonth(1),
	            LocalDate.parse(date2).withDayOfMonth(1));
	}
	
	public int getFormersNb() {
		List<User> users = userRepository.findAll();
		int count = 0;
		for (User user : users) {
			if (user.getRole() == Role.FORMER ){
				count++;
			}
		}
		return count;
	}
	
	
	
	
	
	
}
