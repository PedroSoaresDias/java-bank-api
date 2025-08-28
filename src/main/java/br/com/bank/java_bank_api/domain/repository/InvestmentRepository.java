package br.com.bank.java_bank_api.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bank.java_bank_api.domain.model.InvestmentWallet;

@Repository

public interface InvestmentRepository extends JpaRepository<InvestmentWallet, Long> {    
    Optional<InvestmentWallet> findByPixContaining(String pix);

    List<InvestmentWallet> findAllByUserId(Long userId);

    boolean existsByPix(String pix);
}
