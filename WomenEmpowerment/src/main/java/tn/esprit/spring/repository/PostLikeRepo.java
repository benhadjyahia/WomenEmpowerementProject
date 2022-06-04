package tn.esprit.spring.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tn.esprit.spring.entities.*;
@Repository
public interface PostLikeRepo extends JpaRepository<PostLike, Long>{

	
	@Query(value =" SELECT * from users u  INNER JOIN post_like p ON p.user_user_id = u.user_id ORDER BY count(*)",nativeQuery=true)
			public Set<Object> USer_order_by_Like ();
}
