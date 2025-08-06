package br.com.bank.java_bank.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.bank.java_bank.domain.DTO.CreateUserRequest;
import br.com.bank.java_bank.domain.DTO.UserResponse;
import br.com.bank.java_bank.domain.model.User;
import br.com.bank.java_bank.domain.repository.UserRepository;
import br.com.bank.java_bank.exceptions.UserNotFoundException;
import br.com.bank.java_bank.services.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> responses = users.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        
        return responses;
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        UserResponse response = toDTO(user);
        return response;
    }

    @Override
    public void createUser(CreateUserRequest request) {
        User user = new User();

        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(user);
    }

    @Override
    public void updateUser(Long id, CreateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        user.setName(request.name());
        user.setEmail(request.email());

        if (request.password() != null) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        userRepository.delete(user);
    }

    private UserResponse toDTO(User user) {
        return new UserResponse(user.getId(), user.getName());
    }
}
