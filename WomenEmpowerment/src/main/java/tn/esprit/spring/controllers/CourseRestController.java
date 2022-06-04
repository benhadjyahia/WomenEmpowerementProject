package tn.esprit.spring.controllers;


import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nylas.RequestFailedException;

import io.swagger.annotations.Api;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tn.esprit.spring.entities.Certificate;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.FileInfo;
import tn.esprit.spring.entities.Quiz;
import tn.esprit.spring.entities.QuizQuestion;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.enumerations.Domain;
import tn.esprit.spring.exceptions.CourseNotExist;
import tn.esprit.spring.exceptions.CourseOwnerShip;
import tn.esprit.spring.exceptions.CoursesLimitReached;
import tn.esprit.spring.repository.CourseRepository;
import tn.esprit.spring.security.UserPrincipal;
import tn.esprit.spring.service.courses.CertificateServiceImpl;
import tn.esprit.spring.service.courses.CourseCalendarServiceImpl;
import tn.esprit.spring.service.courses.CourseServiceImpl;
import tn.esprit.spring.service.courses.FileStorageServiceImpl;
import tn.esprit.spring.service.courses.QuizServiceImpl;
import tn.esprit.spring.service.courses.UserCourseServiceImpl;
import tn.esprit.spring.service.user.UserServiceImpl;
import tn.esprit.spring.serviceInterface.courses.UserCourseService;

@RestController
@EnableSwagger2
@Api(tags = "Courses Management")
@RequestMapping("/course")
public class CourseRestController {
@Autowired
CourseServiceImpl courseService;
@Autowired
UserCourseService userCourseService;
@Autowired
QuizServiceImpl quizService;
@Autowired
CourseCalendarServiceImpl courseCalendarServiceImpl;
@Autowired
CertificateServiceImpl certificateService;
@Autowired
CourseRepository courseRep;

/*******************COURSE *********************/
@PostMapping(path = "addCourse")
public ResponseEntity<Object> addCourse(@RequestBody Course c,@ApiIgnore @AuthenticationPrincipal UserPrincipal u) throws CoursesLimitReached, IOException, RequestFailedException {
	if(c.getStartDate().compareTo(c.getEndDate())>0) {
		return new ResponseEntity<>("Start date shouldn't be after end date",HttpStatus.EXPECTATION_FAILED);
	}
	else {
	courseService.affectCourseToUser(u.getId(), c);
	return new ResponseEntity<>(c,HttpStatus.OK);
	}
}
@DeleteMapping(path="removeCourse/{courseId}")
public void deleteCourse(@ApiIgnore @AuthenticationPrincipal UserPrincipal u ,@PathVariable("courseId")Long courseId) throws CourseNotExist, CourseOwnerShip {
	courseService.deleteCourse(u.getId(),courseId);
	
}

@PutMapping(path="editCourse/{courseId}")
public ResponseEntity<Object> editCourse(@RequestBody Course c,@PathVariable("courseId")Long courseId,@ApiIgnore @AuthenticationPrincipal UserPrincipal u ) throws CourseNotExist, CourseOwnerShip {
	if(c.getStartDate().compareTo(c.getEndDate())>0) {
		return new ResponseEntity<>("Start date shouldn't be after end date",HttpStatus.EXPECTATION_FAILED);
	}
	else {
	courseService.editCourse(c,courseId,u.getId());
	return new ResponseEntity<>(c,HttpStatus.OK);
	}
}
@GetMapping(path="getAllCourses")
public List<Course> getAllCourses(){
	return courseService.displayAllCourses();
}
@GetMapping(path="getCourse/{courseId}")
public Course getCourse(@PathVariable("courseId")Long courseId){
	return courseService.displayCourse(courseId);
}
/***************************** USER JOIN COURSE
 * @throws CoursesLimitReached **********************/
@PostMapping(path = "joinCourse/{courseid}")
public void joinCourse(@ApiIgnore @AuthenticationPrincipal UserPrincipal u ,@PathVariable("courseid")Long courseId) throws CoursesLimitReached {
	
	userCourseService.joinCourse(u.getId(), courseId);
	
}
@DeleteMapping(path="leaveCourse/{certificateId}")
public void leaveCourse(@PathVariable("certificateId")Long certificateId) {
   userCourseService.leaveCourse(certificateId);
	
}
/*@GetMapping(path="getParticipants/{courseId}")
public List<User> getParticipants(@PathVariable("courseId")Long courseId){
	quizService.participantPassed(courseId);
	return courseService.getAllParticipants(courseId);
}*/
@GetMapping(path="getParticipant/{userId}")
public User getParticipant(@PathVariable("userId")Long userId){
	return courseService.getParticipant(userId);
}

@GetMapping(path="verifyUserjoin/{userId}/{courseId}")
public int verificate(@PathVariable("userId")Long userId,@PathVariable("courseId")Long courseId) {
	return courseService.userjoinCourseVerificator(userId, courseId);
}
@GetMapping(path="getBannedParticipants/{courseId}")
public Set<User> getBannedusers(@PathVariable("courseId") Long courseId){
	return userCourseService.getBannedusers(courseId);
}
@GetMapping(path="getAllParticipants/{courseId}")
List<User> getParticipants(@PathVariable("courseId")Long courseId){
	return userCourseService.participants(courseId);
}
@GetMapping(path="getuserCertif/{courseId}")
public List<Certificate> userCertificate(@PathVariable("courseId") Long courseId) {
	return certificateService.userCertificate(courseId);
}
@GetMapping(path="getCreatedCourses/{userId}")
public Set<Course> userCertificate(@ApiIgnore @AuthenticationPrincipal UserPrincipal u  ) {
	return userCourseService.getCreatedCourses(u.getId());
}
@GetMapping(path="course/FormersNb")
public int getFormers() {
	return courseService.getFormersNb();
}
@GetMapping(path="getCoursesByDomain/{domain}")
public Set<Course> getCoursesByDomain(@PathVariable("domain") String domain){
	return courseRep.getCoursesByDomain(domain);
}
@GetMapping(path="getEndedCourses")
public Set<Course> getEndedCourses(){
	return courseRep.getEndedCourses();
}
@GetMapping(path="getOnGoingCourses")
public Set<Course> getonGoingCourses(){
	return courseRep.getOnGoingCourses();
}
@GetMapping(path="getAquiredCertifs")
public int getAquiredCertifs(){
	return courseRep.getAquiredCertificates();
}




}
