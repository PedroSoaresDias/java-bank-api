package br.com.bank.account_service.domain.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import br.com.bank.account_service.exceptions.NoFundsEnoughException;

import lombok.Getter;
import lombok.Setter;

@Table(name = "account_wallet")
@Getter
@Setter
public class AccountWallet {
    @Id
    private Long id;
    private String pix;
    private BigDecimal balance = BigDecimal.ZERO;
    private Long userId;

    public void deposit(BigDecimal amount) {
        this.balance = balance.add(amount);
    }
    
    public void withdraw(BigDecimal amount) {
        if (this.balance.compareTo(amount) < 0)
            throw new NoFundsEnoughException("Saldo insuficiente para realizar o saque.");

        this.balance = balance.subtract(amount);
    }
}
