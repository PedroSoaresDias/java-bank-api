package br.com.bank.java_bank.domain.DTO;

public record CreateInvestmentWalletRequest(String pix, long tax, long amount) {

}
