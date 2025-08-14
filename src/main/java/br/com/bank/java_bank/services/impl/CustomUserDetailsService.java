package br.com.bank.java_bank.services.impl;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import br.com.bank.java_bank.domain.repository.UserRepository;
import br.com.bank.java_bank.exceptions.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String id) {
        Long userId = Long.parseLong(id);

        log.debug("Carregando usuário por ID: {}", userId);

        return userRepository.findById(userId)
        .doOnNext(user -> log.debug("Usuário carregado com sucesso: {}", user.getId()))
                .switchIfEmpty(Mono.error(new UserNotFoundException("Usuário não encontrado")))
                .doOnError(error -> log.error("Erro ao carregar usuário: {}", error.getMessage()))
                .map(user -> org.springframework.security.core.userdetails.User
                        .withUsername(user.getId().toString())
                        .password(user.getPassword())
                        .build());
    }

}
