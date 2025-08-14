package br.com.bank.java_bank.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
// import org.springframework.security.core.context.SecurityContextHolder;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class SecurityUtil {
    public static Mono<Long> getAuthenticatedUserId() {
        return ReactiveSecurityContextHolder.getContext()
        .map(securityContext -> securityContext.getAuthentication())
        .filter(Authentication::isAuthenticated)
                .map(auth -> Long.valueOf(auth.getName()));
    }
}
