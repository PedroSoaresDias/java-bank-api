package br.com.bank.java_bank.services;

import br.com.bank.java_bank.domain.DTO.CreateUserRequest;
import br.com.bank.java_bank.domain.DTO.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Flux<UserResponse> getAllUsers();

    Mono<UserResponse> getUserById(Long id);

    Mono<Void> createUser(CreateUserRequest request);

    Mono<Void> updateUser(Long id, CreateUserRequest request);

    Mono<Void> deleteUser(Long id);
}
