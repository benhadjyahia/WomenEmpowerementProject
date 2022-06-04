package tn.esprit.spring.serviceInterface.user;



import tn.esprit.spring.entities.JwtRefreshToken;
import tn.esprit.spring.entities.User;


public interface JwtRefreshTokenService
{
    JwtRefreshToken createRefreshToken(Long userId);

    User generateAccessTokenFromRefreshToken(String refreshTokenId);
}
