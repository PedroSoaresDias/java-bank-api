package br.com.bank.java_bank_api.services.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.bank.java_bank_api.domain.DTO.CreateUserRequest;
import br.com.bank.java_bank_api.domain.DTO.UserResponse;
import br.com.bank.java_bank_api.domain.model.User;
import br.com.bank.java_bank_api.domain.repository.UserRepository;
import br.com.bank.java_bank_api.exceptions.UserNotFoundException;
import br.com.bank.java_bank_api.services.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserResponse> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        Page<UserResponse> responses = users.map(this::toDTO);

        return responses.toList();
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        return toDTO(user);
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        User user = new User();

        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        User savedUser = userRepository.save(user);
        return toDTO(savedUser);
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
