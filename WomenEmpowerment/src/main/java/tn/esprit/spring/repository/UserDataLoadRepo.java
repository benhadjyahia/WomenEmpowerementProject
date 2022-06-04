package tn.esprit.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.esprit.spring.entities.UserDataLoad;

@Repository
public interface UserDataLoadRepo extends JpaRepository<UserDataLoad,Long>{

}
