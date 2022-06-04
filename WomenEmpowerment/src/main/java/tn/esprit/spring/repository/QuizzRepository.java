package tn.esprit.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.esprit.spring.entities.Quiz;
@Repository
public interface QuizzRepository extends JpaRepository<Quiz, Long> {
	/*jointure pour avoir user id ,questionid,answerid,score,quizid*/
@Query(nativeQuery = true,value = "SELECT users_user_id,quiz_question_question_id,quiz_question_answers.answers_answer_id,score ,quiz_quiz_id FROM users_answers INNER JOIN quiz_question_answers ON users_answers.answers_answer_id = quiz_question_answers.answers_answer_id INNER JOIN answer ON answer.answer_id=quiz_question_answers.answers_answer_id INNER JOIN quiz_question ON quiz_question.question_id=quiz_question_answers.quiz_question_question_id INNER JOIN quiz_questions ON quiz_question.question_id= quiz_questions.questions_question_id where answer.is_correct=true AND users_user_id=:param")
public List<String> getUserScore(@Param("param")Long idUser);
	

}
/*SELECT user_user_id,quiz_question.score,answer.is_correct,quiz.quiz_id from certificate c INNER JOIN course ON c.course_course_id= course.course_id INNER JOIN course_quiz ON course.course_id= course_quiz.course_course_id INNER JOIN quiz ON quiz.quiz_id = course_quiz.quiz_quiz_id INNER JOIN quiz_questions ON quiz_questions.quiz_quiz_id = quiz.quiz_id INNER JOIN quiz_question ON quiz_question.question_id = quiz_questions.questions_question_id INNER JOIN quiz_question_answers ON quiz_question_answers.quiz_question_question_id = quiz_question.question_id INNER JOIN answer ON answer.answer_id = quiz_question_answers.answers_answer_id where course.course_id=:param*/