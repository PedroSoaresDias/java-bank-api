package br.com.bank.java_bank.domain.model;

import jakarta.persistence.Embeddable;

@Embeddable
public record Investment(long id, long tax, long initialFunds) {
}
