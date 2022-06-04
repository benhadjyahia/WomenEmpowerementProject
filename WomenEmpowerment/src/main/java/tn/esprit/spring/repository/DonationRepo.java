package tn.esprit.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tn.esprit.spring.entities.Donation;

@Repository
public interface DonationRepo extends JpaRepository<Donation , Long> {
	@Query("SELECT u.name , max(d.amountForEvent),e.eventName FROM User u JOIN u.donations d JOIN d.event e GROUP BY e.eventName ORDER BY d.amountForEvent DESC  ")
	   public List<Object> bestDonner();
}

