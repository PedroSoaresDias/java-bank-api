package br.com.bank.java_bank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;

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
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http)
            throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .authorizeExchange(authz -> authz
                        .pathMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/users/**").permitAll()
                        .anyExchange().authenticated())
                .authenticationManager(jwtReactiveAuthenticationManager())
                .securityContextRepository(jwtSecurityContextRepository())
                .build();
    }
    
    @Bean
    public ReactiveAuthenticationManager jwtReactiveAuthenticationManager() {
        return authentication -> {
            String token = authentication.getCredentials().toString();
            String userId = jwtValidator.getUserIdFromToken(token);

            return customUserDetailsService.findByUsername(userId)
                    .filter(userDetails -> jwtValidator.validateToken(token, userDetails))
                    .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities()));
        };
    }

    @Bean
    public ServerSecurityContextRepository jwtSecurityContextRepository() {
        return new ServerSecurityContextRepository() {
            @Override
            public Mono<org.springframework.security.core.context.SecurityContext> load(ServerWebExchange exchange) {
                String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(token, token);
                    return jwtReactiveAuthenticationManager().authenticate(auth)
                        .map(SecurityContextImpl::new);
                }
                return Mono.empty();
            }

            @Override
            public Mono<Void> save(ServerWebExchange exchange, org.springframework.security.core.context.SecurityContext context) {
                return Mono.empty();
            }
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
