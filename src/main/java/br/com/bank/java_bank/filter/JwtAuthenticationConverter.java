package br.com.bank.java_bank.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;

import br.com.bank.java_bank.utils.JwtTokenValidator;
import reactor.core.publisher.Mono;

public class JwtAuthenticationConverter implements ServerAuthenticationConverter {
    private final JwtTokenValidator jwtValidator;

    public JwtAuthenticationConverter(JwtTokenValidator jwtValidator) {
        this.jwtValidator = jwtValidator;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.empty();
        }

        String token = authHeader.substring(7);

        if (!jwtValidator.validateToken(token)) {
            return Mono.empty();
        }

        String userId = jwtValidator.getUserIdFromToken(token);
        Authentication auth = new UsernamePasswordAuthenticationToken(userId, token);
        return Mono.just(auth);
    }

}
