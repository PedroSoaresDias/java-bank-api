package br.com.bank.java_bank.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bank.java_bank.domain.model.Money;

public interface MoneyRepository extends JpaRepository<Money, Long> {

}
