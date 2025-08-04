package br.com.bank.java_bank.domain.model;

import br.com.bank.java_bank.exceptions.NoFundsEnoughException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @Column(unique = true, nullable = false)
    @NotBlank(message = "A chave pix não pode está em branco")
    @NotNull(message = "A chave pix não pode ser nula")
    @NotEmpty(message = "A chave pix não pode está vazia")
    private String pix;

    @Positive(message = "O valor do saldo deve ser positivo")
    private long balance;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void deposit(long amount) {
        this.balance += amount;
    }
    
    public void withdraw(long amount) {
        if (balance < amount)
            throw new NoFundsEnoughException("Saldo insuficiente para realizar o saque.");

        this.balance -= amount;
    }
}
