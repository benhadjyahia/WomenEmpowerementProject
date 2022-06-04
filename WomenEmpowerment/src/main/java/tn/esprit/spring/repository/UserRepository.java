package tn.esprit.spring.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.esprit.spring.entities.User;
import tn.esprit.spring.enumerations.Role;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);

    @Modifying
    @Query("update User set role = :role where username = :username")
    void updateUserRole(@Param("username") String username, @Param("role")Role role);
    
    @Modifying
    @Query("update User set role = 'ADMIN' where username = :username")
    void makeAdmin(@Param("username") String username);
    
    @Query("select u from User u where u.subscription is not null")
    public List<User> subscribedUsers();
    
   
    
}
