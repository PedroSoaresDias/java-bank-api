package br.com.bank.java_bank.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import br.com.bank.java_bank.exceptions.NoFundsEnoughException;
import lombok.Getter;
import lombok.Setter;

@Table(name = "investment_wallet")
@Getter
@Setter
public class InvestmentWallet {
    @Id
    private Long id;
    private String pix;
    private long balance;
    private long tax;
    private long initialDeposit;
    private Long userId;

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
