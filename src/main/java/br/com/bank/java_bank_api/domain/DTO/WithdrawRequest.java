package br.com.bank.java_bank_api.domain.DTO;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record WithdrawRequest(
        @Schema(description = "Chave Pix da conta corrente", example = "1234abc")
        @NotBlank(message = "A chave pix não pode está em branco") 
        @NotNull(message = "A chave pix não pode ser nula") 
        @NotEmpty(message = "A chave pix não pode está vazia") 
        String pix,
                
        @Schema(description = "Valor do saque em centavos", example = "10000")
        @Positive(message = "O valor do saque deve ser positivo") 
        BigDecimal amount) {

}
