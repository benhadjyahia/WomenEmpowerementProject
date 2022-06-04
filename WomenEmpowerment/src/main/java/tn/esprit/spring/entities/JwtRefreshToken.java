package tn.esprit.spring.entities;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.Data;

@Data
@Entity
@Table(name = "jwt_refresh_token")
public class JwtRefreshToken {
	
	@Id
    @Column(name = "token_id", nullable = false)
    private String tokenId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

}
