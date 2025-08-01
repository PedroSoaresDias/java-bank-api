package br.com.bank.java_bank.services;

import java.util.List;

import br.com.bank.java_bank.domain.model.Investment;

public interface InvestmentService {
    List<Investment> findAll();
    
    Investment createInvestment(long tax, long initialFunds);

    Investment findById(long id);
}
