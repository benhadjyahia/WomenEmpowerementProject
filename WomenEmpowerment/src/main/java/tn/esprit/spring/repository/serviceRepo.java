package tn.esprit.spring.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import tn.esprit.spring.entities.Service;
@Repository
public interface serviceRepo extends JpaRepository<Service, Long> {
	@Query(nativeQuery = true ,value = "SELECT * from service s  WHERE s.job like :keyword ")
	public List<tn.esprit.spring.entities.Service> recherche(@Param("keyword") String keyword);
	@Query( nativeQuery = true , value = "SELECT * FROM service s WHERE s.start_date  BETWEEN :startDate AND :endDate")
	List<Service> getAllBetweenDates(@Param("startDate") Date startDate,
	                                    @Param("endDate")  Date endDate);
}
