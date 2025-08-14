package br.com.bank.java_bank.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import br.com.bank.java_bank.domain.model.User;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    Mono<User> findByEmail(String email);
}
