package br.com.bank.java_bank.services.impl;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.bank.java_bank.domain.DTO.AuthRequest;
import br.com.bank.java_bank.domain.DTO.AuthResponse;
import br.com.bank.java_bank.services.AuthService;
import br.com.bank.java_bank.domain.repository.UserRepository;
import br.com.bank.java_bank.exceptions.AuthenticationException;
import br.com.bank.java_bank.utils.JwtTokenGenerator;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenGenerator jwtGenerator;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
            JwtTokenGenerator jwtGenerator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @Override
    public Mono<AuthResponse> authenticate(AuthRequest request) {
        return userRepository.findByEmail(request.email())
        .doOnNext(user -> log.debug("Tentando autenticar usuário com email: {}", user.getEmail()))
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Usuário não encontrado.")))
                .flatMap(user -> {
                    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
                        log.warn("Senha inválida para usuário: {}", request.password());
                        throw new AuthenticationException("Credenciais inválidas");
                    }

                    String token = jwtGenerator.generateToken(user.getId());
                    return Mono.just(new AuthResponse(token))
                    .doOnNext(response -> log.debug("Usuário autenticado: {}", response));
                });
        // User user = userRepository.findByEmail(request.email())
        // .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        // if (!passwordEncoder.matches(request.password(), user.getPassword())) {
        // throw new AuthenticationException("Credenciais inválidas");
        // }

        // String token = jwtGenerator.generateToken(user.getId());
        // return new AuthResponse(token);
    }

}
