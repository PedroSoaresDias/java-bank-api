package br.com.bank.java_bank.services.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.bank.java_bank.domain.DTO.CreateUserRequest;
import br.com.bank.java_bank.domain.DTO.UserResponse;
import br.com.bank.java_bank.domain.model.User;
import br.com.bank.java_bank.domain.repository.UserRepository;
import br.com.bank.java_bank.exceptions.UserNotFoundException;
import br.com.bank.java_bank.services.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Flux<UserResponse> getAllUsers() {
        return userRepository.findAll().map(this::toDTO);
    }

    @Override
    public Mono<UserResponse> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::toDTO)
                .switchIfEmpty(Mono.error(new UserNotFoundException("Usuário não encontrado")));
    }

    @Override
    public Mono<Void> createUser(CreateUserRequest request) {
        User user = new User();

        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        return userRepository.save(user).then();
    }

    @Override
    public Mono<Void> updateUser(Long id, CreateUserRequest request) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException("Usuário não encontrado")))
                .flatMap(user -> {
                    user.setName(request.name());
                    user.setEmail(request.email());
                    if (request.password() != null) {
                        user.setPassword(passwordEncoder.encode(request.password()));
                    }
                    return userRepository.save(user);
                }).then();
    }

    @Override
    public Mono<Void> deleteUser(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException("Usuário não encontrado")))
                .flatMap(userRepository::delete)
                .then();
    }

    private UserResponse toDTO(User user) {
        return new UserResponse(user.getId(), user.getName());
    }
}
