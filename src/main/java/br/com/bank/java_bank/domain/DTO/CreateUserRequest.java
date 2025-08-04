package br.com.bank.java_bank.domain.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres")
        @NotBlank(message = "O nome não pode está em branco")
        @NotNull(message = "O nome não pode ser nulo")
        @NotEmpty(message = "O nome não pode está vazio")
        String name,

        @Email(message = "Coloque um email válido")
        @Column(unique = true)
        @NotBlank(message = "O email não pode está em branco")
        @NotNull(message = "O email não pode ser nulo")
        @NotEmpty(message = "O email não pode está vazio")
        String email,

        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres") 
        @NotBlank(message = "A senha não pode está em branco") 
        @NotNull(message = "A senha não pode ser nula")
        @NotEmpty(message = "A senha não pode está vazia")
        String password) {

}
