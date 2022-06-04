package tn.esprit.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tn.esprit.spring.entities.PostComment;
import tn.esprit.spring.entities.eventComment;

public interface eventcommentRepo  extends JpaRepository<eventComment, Long> {

}
