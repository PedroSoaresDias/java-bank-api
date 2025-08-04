package br.com.bank.java_bank.domain.DTO;

// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.NotEmpty;
// import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record InvestmentWithdrawRequest(
        // @NotBlank(message = "A chave pix não pode está em branco") 
        // @NotNull(message = "A chave pix não pode ser nula") 
        // @NotEmpty(message = "A chave pix não pode está vazia") 
        // String pix,

        @Positive(message = "O valor do saque deve ser positivo") 
        long amount) {

}
