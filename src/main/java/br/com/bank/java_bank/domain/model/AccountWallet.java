package br.com.bank.java_bank.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import br.com.bank.java_bank.exceptions.NoFundsEnoughException;

import lombok.Getter;
import lombok.Setter;

@Table(name = "account_wallet")
@Getter
@Setter
public class AccountWallet {
    @Id
    private Long id;
    private String pix;
    private long balance;

    @Column("user_id")
    private Long userId;

    public void deposit(long amount) {
        this.balance += amount;
    }
    
    public void withdraw(long amount) {
        if (balance < amount)
            throw new NoFundsEnoughException("Saldo insuficiente para realizar o saque.");

        this.balance -= amount;
    }
}
