package tn.esprit.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tn.esprit.spring.entities.Offer;
@Repository
public interface IOfferRepository extends JpaRepository<Offer, Long> {
	  
	@Query(  "SELECT p FROM Offer p WHERE p.title LIKE %?1%"
            + " OR p.description LIKE %?1%"
            + " OR p.location LIKE %?1%"		     )
public List<Offer> search(String keyword);


	 
}