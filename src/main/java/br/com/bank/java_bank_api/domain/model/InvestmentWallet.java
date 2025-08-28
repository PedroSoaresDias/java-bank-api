package br.com.bank.java_bank_api.domain.model;

import java.math.BigDecimal;


import br.com.bank.java_bank_api.exceptions.NoFundsEnoughException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "investment_wallet")
@Getter
@Setter
public class InvestmentWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    @NotBlank(message = "A chave pix não pode está em branco")
    @NotNull(message = "A chave pix não pode ser nula")
    @NotEmpty(message = "A chave pix não pode está vazia")
    private String pix;

    @Positive(message = "O valor do saldo deve ser positivo")
    private BigDecimal balance;

    @Positive(message = "O valor da taxa deve ser positivo")
    private BigDecimal tax;

    @Positive(message = "O valor do depósito inicial deve ser positivo")
    private BigDecimal initialDeposit;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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
