package br.com.bank.java_bank.domain.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransferRequest(
        // @NotBlank(message = "A chave pix de origem não pode está em branco") 
        // @NotNull(message = "A chave pix de origem não pode ser nula") 
        // @NotEmpty(message = "A chave pix de origem não pode está vazia") 
        // String fromPix,
                
        @NotBlank(message = "A chave pix de destino não pode está em branco") 
        @NotNull(message = "A chave pix de destino não pode ser nula") 
        @NotEmpty(message = "A chave pix de destino não pode está vazia") 
        String toPix,

        @Positive(message = "O valor do depósito deve ser positivo")
        long amount) {

}
