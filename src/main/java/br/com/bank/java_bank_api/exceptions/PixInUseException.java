package br.com.bank.java_bank_api.exceptions;

public class PixInUseException extends RuntimeException {
    public PixInUseException(String message) {
        super(message);
    }
}
