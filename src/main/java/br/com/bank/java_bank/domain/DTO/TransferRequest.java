package br.com.bank.java_bank.domain.DTO;

public record TransferRequest(String fromPix, String toPix, long amount) {

}
