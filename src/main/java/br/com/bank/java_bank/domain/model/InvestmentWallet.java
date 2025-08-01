package br.com.bank.java_bank.domain.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "investment_wallet")
@Getter
@Setter
public class InvestmentWallet extends Wallet {
    @Embedded
    private Investment investment;

    @OneToOne
    @JoinColumn(name = "account_wallet_id")
    private AccountWallet account;

}
