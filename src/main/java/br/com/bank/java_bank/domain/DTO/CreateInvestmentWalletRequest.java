package br.com.bank.java_bank.domain.DTO;

// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.NotEmpty;
// import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateInvestmentWalletRequest(
        // @NotBlank(message = "A chave pix não pode está em branco")
        // @NotNull(message = "A chave pix não pode ser nula")
        // @NotEmpty(message = "A chave pix não pode está vazia")
        // String pix,
                
        @Positive(message = "O valor da taxa deve ser positivo")
        long tax,
                
        @Positive(message = "O valor em dinheiro deve ser positivo")
        long amount) {

}
