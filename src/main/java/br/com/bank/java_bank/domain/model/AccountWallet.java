package br.com.bank.java_bank.domain.model;

import br.com.bank.java_bank.exceptions.NoFundsEnoughException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.GenerationType;

@Entity(name = "account_wallet")
@Getter
@Setter
public class AccountWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pix;
    private long balance;

    public void deposit(long amount) {
        this.balance += amount;
    }
    
    public void withdraw(long amount) {
        if (balance < amount)
            throw new NoFundsEnoughException("Saldo insuficiente para realizar o saque.");

        this.balance -= amount;
    }
}
