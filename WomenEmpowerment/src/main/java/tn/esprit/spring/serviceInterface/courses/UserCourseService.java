package tn.esprit.spring.serviceInterface.courses;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.exceptions.CoursesLimitReached;

@Service
public interface UserCourseService {
	public void joinCourse(Long idUser,Long idCourse) throws CoursesLimitReached;
	public void leaveCourse(Long certificateId);
	public List<User> participants(Long courseId);
	public Set<User> getBannedusers(Long courseId);
	public Set<Course> getCreatedCourses(Long idUser);

}
