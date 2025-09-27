package br.com.bank.java_bank_api.domain.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.bank.java_bank_api.domain.enums.TransactionType;

public record TransactionResponse(BigDecimal amount, TransactionType type, String description,LocalDateTime timestamp) {

}
