package br.com.bank.java_bank.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bank.java_bank.domain.model.InvestmentWallet;

public interface InvestmentRepository extends JpaRepository<InvestmentWallet, Long> {

}
