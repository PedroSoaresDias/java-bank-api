package br.com.bank.java_bank_api.domain.model;

import java.math.BigDecimal;
import java.util.List;

import br.com.bank.java_bank_api.exceptions.NoFundsEnoughException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "account_wallet")
@Getter
@Setter
public class AccountWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "A chave pix não pode está em branco")
    @NotNull(message = "A chave pix não pode ser nula")
    @NotEmpty(message = "A chave pix não pode está vazia")
    private String pix;

    @PositiveOrZero(message = "O valor do saldo deve ser positivo")
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    public void deposit(BigDecimal amount) {
        this.balance = balance.add(amount);
    }
    
    public void withdraw(BigDecimal amount) {
        if (this.balance.compareTo(amount) < 0)
            throw new NoFundsEnoughException("Saldo insuficiente para realizar o saque.");

        this.balance = balance.subtract(amount);
    }
}
