package br.com.bank.java_bank.domain.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransferPixRequest(
        @Schema(description = "Chave Pix da conta de origem", example = "1234abc")
        @NotBlank(message = "A chave pix de origem não pode está em branco") 
        @NotNull(message = "A chave pix de origem não pode ser nula") 
        @NotEmpty(message = "A chave pix de origem não pode está vazia") 
        String fromPix,
                        
        @Schema(description = "Chave Pix da conta de destino", example = "5678def")
        @NotBlank(message = "A chave pix de destino não pode está em branco") 
        @NotNull(message = "A chave pix de destino não pode ser nula") 
        @NotEmpty(message = "A chave pix de destino não pode está vazia") 
        String toPix,
                
        @Schema(description = "Valor da transferência em centavos", example = "10000")
        @Positive(message = "O valor da transferência deve ser positivo")
        long amount) {

}
