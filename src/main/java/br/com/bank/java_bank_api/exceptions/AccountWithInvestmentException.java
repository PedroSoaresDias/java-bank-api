package br.com.bank.java_bank_api.exceptions;

public class AccountWithInvestmentException extends RuntimeException {
    public AccountWithInvestmentException(String message) {
        super(message);
    }
}
