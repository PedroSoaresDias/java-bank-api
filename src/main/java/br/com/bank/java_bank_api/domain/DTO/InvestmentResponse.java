package br.com.bank.java_bank_api.domain.DTO;

import java.math.BigDecimal;

public record InvestmentResponse(Long id, String pix, BigDecimal balance, BigDecimal tax) {

}
