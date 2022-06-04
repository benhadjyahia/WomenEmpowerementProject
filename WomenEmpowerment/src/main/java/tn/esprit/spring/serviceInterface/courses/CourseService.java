package tn.esprit.spring.serviceInterface.courses;

import java.io.IOException;
import java.time.Period;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.nylas.RequestFailedException;

import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Quiz;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.exceptions.CourseNotExist;
import tn.esprit.spring.exceptions.CourseOwnerShip;
import tn.esprit.spring.exceptions.CoursesLimitReached;
@Service
public interface CourseService {
public Course addCourse(Course c);
public void affectCourseToUser(Long idUser,Course c) throws CoursesLimitReached, IOException, RequestFailedException;
public Course deleteCourse(Long idUser,Long idCourse) throws CourseNotExist, CourseOwnerShip;
public Course editCourse(Course c,Long courseId,Long userId) throws CourseNotExist,CourseOwnerShip;
public void createQuizz(Quiz Q, Long idCourse,Long idUser) throws CourseOwnerShip;
public List<Course> displayAllCourses();
public Course displayCourse(Long courseId);
public List<User> getAllParticipants(Long courseId);
public User getParticipant(Long courseId);
public void coursesStatus();
public void coursesEnded();
public boolean courseVerificator(Long userId);
public int userjoinCourseVerificator(Long userId,Long courseId);
public Period diffCalculator(String date1,String date2);

}
