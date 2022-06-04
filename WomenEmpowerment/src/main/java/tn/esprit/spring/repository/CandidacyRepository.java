package tn.esprit.spring.repository;

import java.lang.annotation.Native;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tn.esprit.spring.entities.Candidacy;
import tn.esprit.spring.entities.Offer;
import tn.esprit.spring.entities.User;
@Repository
public interface CandidacyRepository extends JpaRepository<Candidacy, Long> {
	

	//Jointure pour Consulter l'historique de ses candidatures et leurs états
	@Query(nativeQuery = true, value = "SELECT o.offer_id, o.title, o.approx_salary, o.created_at, o.description, o.location,o.required_candidates,	c.candidacy_id, c.candidacy_state, c.is_bookmarked FROM offer o  Join candidacy c on o.offer_id=c.offer_offer_id where c.candidate_user_id=:param")
	 public List<String> search(@Param("param") String keyword);
	@Query(nativeQuery = true, value = "SELECT * from candidacy c where c.candidate_user_id=:param")
	 public List<Candidacy> mycand(@Param("param") String keyword);

	
	// Jointure Pour consulter ses Candidatures favoris et leurs états 
	@Query(nativeQuery=true, value ="	SELECT * from candidacy c where c.candidate_user_id=:param and c.is_bookmarked=true")
	 public List<Candidacy> searchFavorite(@Param("param") String keyword);
 
	//Jointure Pour récuperer le nom d'utilisateur a partir de sa candidature
	@Query (nativeQuery=true,value="select u.name FROM users u  Join candidacy c on candidate_user_id=user_id where candidacy_id=:param")
	public String getCandidateName(@Param("param") Long candidacy_id);
	
	
	//Jointure Pour récuperer l'email d'utilisateur a partir de sa candidature
	@Query (nativeQuery=true,value="select u.email FROM users u  Join candidacy c on candidate_user_id=user_id where candidacy_id=:param")
	public String getCandidateEmail(@Param("param") Long candidacy_id);
	

	//Jointure Pour récuperer le titre d'offre a partir d'une candidature
		@Query (nativeQuery=true,value="select o.title FROM offer o  Join candidacy c on offer_id=offer_offer_id where candidacy_id=:param")
		public String getOfferTitle(@Param("param") Long candidacy_id);
		
	//Jointure Pour récuperer la location d'offre a partir d'une candidature
				@Query (nativeQuery=true,value="select o.location FROM offer o  Join candidacy c on offer_id=offer_offer_id where candidacy_id=:param")
				public String getOfferLocation(@Param("param") Long candidacy_id);
				
	//Jointure Pour récuperer la location d'offre a partir d'une candidature
				@Query (nativeQuery=true,value="select o.description FROM offer o  Join candidacy c on offer_id=offer_offer_id where candidacy_id=:param")
		public String getOfferDescription(@Param("param") Long candidacy_id);
		
		
		
	//Jointure Pour récuperer l'offre a partir d'une candidature
		@Query (nativeQuery=true,value="select * FROM offer o  Join candidacy c on offer_offer_id=offer_id where candidacy_id=:param")
		public Offer getOffer(@Param("param") Long candidacy_id);
		
		////Jointure Pour récuperer l'ID de l'offre a partir d'une candidature
		@Query (nativeQuery=true,value="select o.offer_id FROM offer o  Join candidacy c on offer_id=offer_offer_id where candidacy_id=:param")
public int getOfferId(@Param("param") Long candidacy_id);

}	
