package br.com.bank.java_bank.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bank.java_bank.domain.model.AccountWallet;

public interface AccountRepository extends JpaRepository<AccountWallet, Long> {
    Optional<AccountWallet> findByPixKeysContaining(String pix);
}
