package br.com.bank.java_bank_api.domain.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthRequest(
        @Schema(description = "Login com o email do usuário", example = "email@email.com")
        String email,
                
        @Schema(description = "Login com a senha do usuário", example = "password")
        String password) {

}
