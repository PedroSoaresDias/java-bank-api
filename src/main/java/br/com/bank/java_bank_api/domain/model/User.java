package br.com.bank.java_bank_api.domain.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "tb_user")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres")
    @NotBlank(message = "O nome não pode está em branco")
    @NotNull(message = "O nome não pode ser nulo")
    private String name;

    @Email(message = "Coloque um email válido")
    @Column(unique = true)
    @NotBlank(message = "O email não pode está em branco")
    @NotNull(message = "O email não pode ser nulo")
    private String email;

    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    @NotBlank(message = "A senha não pode está em branco")
    @NotNull(message = "A senha não pode ser nula")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AccountWallet> accountWallet;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<InvestmentWallet> investmentWallet;
}