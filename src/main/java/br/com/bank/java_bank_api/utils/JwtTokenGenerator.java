package br.com.bank.java_bank_api.utils;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

@Component
public class JwtTokenGenerator {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenGenerator.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public String generateToken(Long id) {
        try {
            return JWT.create()
            .withSubject(id.toString())
            .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                    .sign(Algorithm.HMAC256(secret));
        } catch (JWTCreationException e) {
            logger.warn("Falha na criação do token: {}", e.getMessage());
            return null;
        }
    }
}
