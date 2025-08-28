package br.com.bank.java_bank_api.services;

import java.util.List;

import br.com.bank.java_bank_api.domain.DTO.CreateUserRequest;
import br.com.bank.java_bank_api.domain.DTO.UserResponse;

public interface UserService {
    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    void createUser(CreateUserRequest request);

    void updateUser(Long id, CreateUserRequest request);

    void deleteUser(Long id);
}
