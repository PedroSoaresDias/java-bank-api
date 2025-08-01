package br.com.bank.java_bank.domain.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "account_wallet")
@Getter
@Setter
public class AccountWallet extends Wallet {

    @ElementCollection
    @CollectionTable(name = "pix_keys")
    @JoinColumn(name = "wallet_id")
    @Column(name = "pix_key")
    private List<String> pix = new ArrayList<>();
}
