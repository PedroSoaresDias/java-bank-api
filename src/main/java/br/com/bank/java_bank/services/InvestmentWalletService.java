package br.com.bank.java_bank.services;

import java.util.List;

import br.com.bank.java_bank.domain.DTO.CreateInvestmentWalletRequest;
import br.com.bank.java_bank.domain.DTO.InvestmentDepositRequest;
import br.com.bank.java_bank.domain.DTO.InvestmentResponse;
import br.com.bank.java_bank.domain.model.InvestmentWallet;

public interface InvestmentWalletService {
    List<InvestmentResponse> findAllInvestments();

    InvestmentResponse findInvestmentById(Long id);

    InvestmentWallet create(CreateInvestmentWalletRequest request);

    void invest(String email, InvestmentDepositRequest request);

    void withdraw(String email, InvestmentDepositRequest request);

    void updateYield();
}
