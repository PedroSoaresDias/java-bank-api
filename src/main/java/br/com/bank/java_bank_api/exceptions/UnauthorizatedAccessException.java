package br.com.bank.java_bank_api.exceptions;

public class UnauthorizatedAccessException extends RuntimeException {
    public UnauthorizatedAccessException(String message) {
        super(message);
    }
}
