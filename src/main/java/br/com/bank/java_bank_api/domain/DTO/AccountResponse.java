package br.com.bank.java_bank_api.domain.DTO;

import java.math.BigDecimal;

public record AccountResponse(Long id, String pix, BigDecimal balance) {

}
