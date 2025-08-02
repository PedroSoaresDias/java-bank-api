package br.com.bank.java_bank.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bank.java_bank.domain.model.AccountWallet;

@Repository
public interface AccountRepository extends JpaRepository<AccountWallet, Long> {
    Optional<AccountWallet> findByPixContaining(String pix);
}
