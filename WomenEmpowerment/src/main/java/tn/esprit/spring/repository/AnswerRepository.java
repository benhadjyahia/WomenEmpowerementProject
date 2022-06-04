package tn.esprit.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tn.esprit.spring.entities.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
