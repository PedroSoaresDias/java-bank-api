package br.com.bank.investment_service.domain.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import br.com.bank.investment_service.exceptions.NoFundsEnoughException;
import lombok.Getter;
import lombok.Setter;

@Table(name = "investment_wallet")
@Getter
@Setter
public class InvestmentWallet {
    @Id
    private Long id;
    private String pix;
    private BigDecimal balance = BigDecimal.ZERO;
    private BigDecimal tax;
    private BigDecimal initialDeposit;
    private Long userId;

    public void deposit(BigDecimal amount) {
        this.balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (this.balance.compareTo(amount) < 0)
            throw new NoFundsEnoughException("Saldo insuficiente para realizar o saque.");

        this.balance = balance.subtract(amount);
    }

    public void updateYield() {
        BigDecimal profit = balance.multiply(tax).divide(BigDecimal.valueOf(100));
        this.balance = balance.add(profit);
    }
}
