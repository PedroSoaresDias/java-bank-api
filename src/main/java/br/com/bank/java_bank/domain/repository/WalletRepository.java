package br.com.bank.java_bank.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bank.java_bank.domain.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

}
