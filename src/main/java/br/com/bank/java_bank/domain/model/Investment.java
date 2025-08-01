package br.com.bank.java_bank.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Investment {
    private long id;
    private long tax;
    private long initialFunds;
}
