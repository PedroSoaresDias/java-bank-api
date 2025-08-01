package br.com.bank.java_bank.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bank.java_bank.domain.model.MoneyAudit;

public interface MoneyAuditRepository extends JpaRepository<MoneyAudit, UUID> {

}
