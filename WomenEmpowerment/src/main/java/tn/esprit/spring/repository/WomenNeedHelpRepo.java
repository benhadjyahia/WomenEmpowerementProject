package tn.esprit.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.esprit.spring.entities.User;
import tn.esprit.spring.entities.WomenNeedDonation;

@Repository
public interface WomenNeedHelpRepo extends JpaRepository<WomenNeedDonation, Long>{

}
