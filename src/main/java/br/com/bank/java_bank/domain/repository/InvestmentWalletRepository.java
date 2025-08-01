package br.com.bank.java_bank.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bank.java_bank.domain.model.InvestmentWallet;

public interface InvestmentWalletRepository extends JpaRepository<InvestmentWallet, Long> {
    Optional<InvestmentWallet> findByAccountPixKeysContaining(String pix);
}
