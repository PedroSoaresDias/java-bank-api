package br.com.bank.java_bank.domain.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateInvestmentWalletRequest(
        @Schema(description = "Chave Pix para a conta de investimento", example = "1234abc")
        @NotBlank(message = "A chave pix não pode está em branco")
        @NotNull(message = "A chave pix não pode ser nula")
        @NotEmpty(message = "A chave pix não pode está vazia")
        String pix,
                        
        @Schema(description = "Valor da taxa de rendimento", example = "1")
        @Positive(message = "O valor da taxa deve ser positivo")
        long tax,
                        
        @Schema(description = "Valor do depósito inicial e do saldo em centavos", example = "10000")
        @Positive(message = "O valor em dinheiro deve ser positivo")
        long amount) {

}
