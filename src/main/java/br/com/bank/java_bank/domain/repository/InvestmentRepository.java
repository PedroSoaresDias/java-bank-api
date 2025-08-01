package br.com.bank.java_bank.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bank.java_bank.domain.model.Investment;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {

}
