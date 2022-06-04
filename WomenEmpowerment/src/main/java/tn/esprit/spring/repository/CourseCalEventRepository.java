package tn.esprit.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.esprit.spring.entities.CourseCalEvent;
@Repository
public interface CourseCalEventRepository extends JpaRepository<CourseCalEvent, Long> {

}
