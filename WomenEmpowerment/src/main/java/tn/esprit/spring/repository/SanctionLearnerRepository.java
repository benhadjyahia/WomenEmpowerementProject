package tn.esprit.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.esprit.spring.entities.Certificate;
import tn.esprit.spring.entities.SanctionLearnner;
@Repository
public interface SanctionLearnerRepository extends JpaRepository<SanctionLearnner,Long> {
	@Query(nativeQuery = true,value="SELECT * from sanction_learner where certificate_certificate_id=:param")
	List<SanctionLearnner> findByCertificateId(@Param("param")Long certificateId);

}
