package br.com.bank.java_bank_api.domain.DTO;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateAccountRequest(
        @Schema(description = "Chave Pix para a conta corrent", example = "1234abc")
        @NotBlank(message = "A chave pix não pode está em branco")
        @NotNull(message = "A chave pix não pode ser nula")
        @NotEmpty(message = "A chave pix não pode está vazia")
        String pix,
                        
        @Schema(description = "Valor do saldo em centavos", example = "100.00")
        @Positive(message = "O valor do saldo deve ser positivo")
        BigDecimal balance) {

}
