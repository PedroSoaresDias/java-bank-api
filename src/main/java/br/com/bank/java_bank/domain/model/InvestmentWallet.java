package br.com.bank.java_bank.domain.model;

import br.com.bank.java_bank.exceptions.NoFundsEnoughException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "investment_wallet")
@Getter
@Setter
public class InvestmentWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pix;

    private long balance;

    private long tax;
    
    private long initialDeposit;

    @OneToOne
    @JoinColumn(name = "account_id", unique = true)
    private AccountWallet wallet;

    public void deposit(long amount) {
        this.balance += amount;
    }

    public void withdraw(long amount) {
        if (balance < amount)
            throw new NoFundsEnoughException("Saldo insuficiente para realizar o saque.");

        this.balance -= amount;
    }

    public void updateYield() {
        long profit = (balance * tax) / 100;
        this.balance += profit;
    }
}
