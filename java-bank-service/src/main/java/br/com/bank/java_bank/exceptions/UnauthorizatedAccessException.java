package br.com.bank.java_bank.exceptions;

public class UnauthorizatedAccessException extends RuntimeException {
    public UnauthorizatedAccessException(String message) {
        super(message);
    }
}
