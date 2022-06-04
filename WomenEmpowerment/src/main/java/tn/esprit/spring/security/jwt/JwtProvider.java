package tn.esprit.spring.security.jwt;

import org.springframework.security.core.Authentication;

import tn.esprit.spring.security.UserPrincipal;

import javax.servlet.http.HttpServletRequest;

public interface JwtProvider
{
    String generateToken(UserPrincipal auth);

    Authentication getAuthentication(HttpServletRequest request);

    boolean isTokenValid(HttpServletRequest request);
}
