package br.com.bank.java_bank.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "money_audits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoneyAudit {
    @Id
    private UUID transactionId;

    @Enumerated
    private BankService service;

    private String description;
    private OffsetDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "money_id")
    private Money money;
}
