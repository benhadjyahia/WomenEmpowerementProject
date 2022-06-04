package tn.esprit.spring.controllers;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.stax2.ri.typed.StringBase64Decoder;
import org.cryptacular.codec.Base64Decoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import springfox.documentation.annotations.ApiIgnore;
import tn.esprit.spring.entities.Answer;
import tn.esprit.spring.entities.Certificate;
import tn.esprit.spring.entities.Quiz;
import tn.esprit.spring.entities.QuizQuestion;
import tn.esprit.spring.exceptions.CourseOwnerShip;
import tn.esprit.spring.repository.CertificateRepository;
import tn.esprit.spring.security.UserPrincipal;
import tn.esprit.spring.service.courses.CourseServiceImpl;
import tn.esprit.spring.service.courses.QuizServiceImpl;
import tn.esprit.spring.serviceInterface.courses.UserCourseService;

@RestController
@Api(tags = "Quiz Management")
@RequestMapping("/quiz")
public class QuizRestController {
	@Autowired
	CourseServiceImpl courseService;
	@Autowired
	UserCourseService userCourseService;
	@Autowired
	QuizServiceImpl quizService;
	@Autowired
	CertificateRepository certificateRepository;
	@PostMapping(path = "createQuiz/{courseId}")
	public void createQuiz(@RequestBody Quiz q,@PathVariable("courseId")Long courseId,@ApiIgnore @AuthenticationPrincipal UserPrincipal u ) throws CourseOwnerShip {
		Long iduser = u.getId();
		courseService.createQuizz(q, courseId,iduser);
		
	}
	@PostMapping(path = "addQuestion/{quizId}")
	public void addQuestion(@RequestBody QuizQuestion quest,@PathVariable("quizId")Long quizId) {
		
		quizService.addQuestionToQuiz(quest, quizId);
		
	}
	@PostMapping(path = "addQuestions/{quizId}")
	public void addQuestions(@RequestBody Set<QuizQuestion> quest,@PathVariable("quizId")Long quizId) {
		
		quizService.addListQuestionsToQuiz(quest, quizId);
		
	}
	@DeleteMapping(path="deleteQuestion/{questionId}/{quizId}")
	public void deleteQuestion(@PathVariable("questionId")Long questionId,@PathVariable("quizId")Long quizId) {
	   quizService.removeQuestion(questionId,quizId);
		
	}
	@PutMapping(path="editQuestion/{questionId}")
	public void editQuestion(@RequestBody QuizQuestion q,@PathVariable("questionId")Long questionId) {
		quizService.editQuestion(q,questionId);
	}
	@PostMapping(path = "addAnswers/{questionId}")
	public void addAnswers(@RequestBody Set<Answer> answer,@PathVariable("questionId")Long questionId) {
		
		quizService.addAnswersToQuestion(answer, questionId);
		
	}
	@PutMapping(path="setCorrectAnswer/{answerId}")
	public void setCorrectAnswer(@PathVariable("answerId")Long answerId) {
		quizService.setCorrectAnswer(answerId) ;
	}
	@PostMapping(path="answerQuestion/{idAnswer}")
	public Answer userAnswerQuestion(@ApiIgnore @AuthenticationPrincipal UserPrincipal u,@PathVariable("idAnswer")Long idAnswer) {
		return quizService.answerQuizQuestion(u.getId(), idAnswer);
	}
	@GetMapping(path="calculUserScore/{userId}/{quizId}")
	public int userScoreCalcul(@PathVariable("userId")Long userId,@PathVariable("quizId")Long quizId) {
		return quizService.calculScore(userId, quizId);
		
	}
	@GetMapping(path="calculUserTotScore/{idUser}/{idCourse}")
	public int userCourseScore(@PathVariable("idUser")Long idUser,@PathVariable("idCourse") Long idCourse) {
		return quizService.userCourseScore(idUser, idCourse);
	}
	@GetMapping(path="didUserPass/{idUser}/{idCourse}")
	Boolean userPassed(@PathVariable("idUser")Long idUser,@PathVariable("idCourse") Long idCourse) {
		return quizService.userPassed(idUser, idCourse);
	}
	@GetMapping(path="getQuiz/{idQuiz}")
	public Quiz getQuiz(@PathVariable("idQuiz")Long idQuiz) {
		return quizService.getQuiz(idQuiz);
	}
	@GetMapping(path="getQuizez/{idCour}")
	public Set<Quiz> getQuizesByCourseId(@PathVariable("idCour")Long idCour) {
		return quizService.getQuizezByCourseId(idCour);
	}
	@GetMapping(path="getQuizQuestions/{idquiz}")
	public Set<QuizQuestion> getQuizQuestion(@PathVariable("idquiz")Long idQuiz) {
		return quizService.getQuestionsByQuizzId(idQuiz);
	}
	@GetMapping(path="getAnswers/{idQuestion}")
	public Set<Answer> getQuizAnswers(@PathVariable("idQuestion")Long idquestion) {
		return quizService.getAnswersByQuestionId(idquestion);
	}
}
















