package br.com.bank.java_bank.utils;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;


@Component
public class JwtTokenValidator {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenValidator.class);

    @Value("${jwt.secret}")
    private String secret;

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = getUserIdFromToken(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (JWTVerificationException e) {
            logger.warn("Falha na validação do token: {}", e.getMessage());
            return false;
        }
    }

    public String getUserIdFromToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException e) {
            logger.warn("Erro ao extrair username do token: {}", e.getMessage());
            return null;
        }
    }

    private boolean isTokenExpired(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
        return decodedJWT.getExpiresAt().before(new Date());
    }
}
