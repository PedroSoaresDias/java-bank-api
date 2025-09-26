package br.com.bank.java_bank_api.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bank.java_bank_api.domain.model.AccountWallet;

@Repository
public interface AccountRepository extends JpaRepository<AccountWallet, Long> {
    Optional<AccountWallet> findByPixContaining(String pix);

    Optional<AccountWallet> findByUserId(Long userId);

    Page<AccountWallet> findAccountsByUserId(Pageable pageable, Long userId);

    boolean existsByPix(String pix);
}