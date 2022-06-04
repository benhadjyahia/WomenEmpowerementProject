package tn.esprit.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tn.esprit.spring.entities.JwtRefreshToken;


public interface JwtRefreshTokenRepository extends JpaRepository<JwtRefreshToken, String>
{
}
