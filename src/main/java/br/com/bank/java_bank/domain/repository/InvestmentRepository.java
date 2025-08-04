package br.com.bank.java_bank.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bank.java_bank.domain.model.InvestmentWallet;

@Repository
public interface InvestmentRepository extends JpaRepository<InvestmentWallet, Long> {
    Optional<InvestmentWallet> findByUserId(Long userId);
}
