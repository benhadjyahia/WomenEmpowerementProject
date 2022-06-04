package tn.esprit.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.esprit.spring.entities.Friend;
import tn.esprit.spring.entities.User;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
	
    boolean existsBySenderAndReceiver(User sender,User receiver);
    
    boolean existsByReceiverAndSender(User sender,User receiver);

    List<Friend> findBySender(User user);
    List<Friend> findByReceiver(User user);

}
