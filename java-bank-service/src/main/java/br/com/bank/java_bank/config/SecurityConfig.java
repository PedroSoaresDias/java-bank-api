package br.com.bank.java_bank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

import br.com.bank.java_bank.filter.JwtAuthenticationConverter;
import br.com.bank.java_bank.services.impl.CustomUserDetailsService;
import br.com.bank.java_bank.utils.JwtTokenValidator;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {
    private final JwtTokenValidator jwtValidator;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(JwtTokenValidator jwtValidator, CustomUserDetailsService customUserDetailsService) {
        this.jwtValidator = jwtValidator;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        AuthenticationWebFilter jwtFilter = new AuthenticationWebFilter(authenticationManager(customUserDetailsService));
        jwtFilter.setServerAuthenticationConverter(new JwtAuthenticationConverter(jwtValidator));
        jwtFilter.setAuthenticationSuccessHandler((webExchange, authentication) -> Mono.empty());

        return http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/auth/**", "/swagger-ui/**", "/api-docs/**", "/users/**").permitAll()
                        .anyExchange().authenticated())
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager(CustomUserDetailsService userDetailsService) {
        return authentication -> {
            String token = authentication.getCredentials().toString();
            String userId = jwtValidator.getUserIdFromToken(token);

            return userDetailsService.findByUsername(userId)
                    .filter(userDetails -> jwtValidator.validateToken(token))
                    .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails, null, null));
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
