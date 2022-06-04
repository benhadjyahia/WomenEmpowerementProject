package tn.esprit.spring.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.enumerations.Domain;
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
	@Query(nativeQuery = true,value = "SELECT u.user_id from users u INNER JOIN certificate ON u.user_id = certificate.user_user_id INNER JOIN course ON certificate.course_course_id = course.course_id WHERE course_id=:param")
	public Long findUserById(@Param("param") Long courseId);
	@Query(nativeQuery=true,value="SELECT certificate_id,course_id,user_user_id,start_date,domain FROM certificate c INNER JOIN course ON course.course_id=c.course_course_id WHERE user_user_id=:param AND course.domain=:domain")
	public List<String> getUserJoinedCourses(@Param("param")Long idUser,@Param("domain")String domain);
	@Query(nativeQuery=true,value="SELECT * FROM course where course.domain=:domain")
	public Set<Course> getCoursesByDomain(@Param("domain")String domain);
	@Query(nativeQuery=true,value="SELECT COUNT(*) FROM certificate WHERE certificate.is_aquired=1")
	public int getAquiredCertificates();
	@Query(nativeQuery=true,value="SELECT * FROM course WHERE DATEDIFF(NOW(),end_date)<=0 ")
	public Set<Course> getOnGoingCourses();
	@Query(nativeQuery=true,value="SELECT * FROM course WHERE DATEDIFF(NOW(),end_date)>0 ")
	public Set<Course> getEndedCourses();
}
/*
SELECT * from certificate c INNER JOIN course ON c.course_course_id= course.course_id INNER JOIN course_quiz ON course.course_id= course_quiz.course_course_id INNER JOIN quiz ON quiz.quiz_id = course_quiz.quiz_quiz_id INNER JOIN quiz_questions ON quiz_questions.quiz_quiz_id = quiz.quiz_id INNER JOIN quiz_question ON quiz_question.question_id = quiz_questions.questions_question_id INNER JOIN quiz_question_answers ON quiz_question_answers.quiz_question_question_id = quiz_question.question_id INNER JOIN answer ON answer.answer_id = quiz_question_answers.answers_answer_id;
*/