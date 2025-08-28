package br.com.bank.java_bank_api.services;

import java.util.List;

import br.com.bank.java_bank_api.domain.DTO.CreateInvestmentWalletRequest;
import br.com.bank.java_bank_api.domain.DTO.InvestmentResponse;
import br.com.bank.java_bank_api.domain.DTO.TransferPixRequest;
import br.com.bank.java_bank_api.domain.model.InvestmentWallet;

public interface InvestmentWalletService {
    List<InvestmentResponse> findAllMyInvestments();

    InvestmentResponse findInvestmentByPix(String pix);

    InvestmentWallet create(CreateInvestmentWalletRequest request);

    void invest(TransferPixRequest request);

    void withdraw(TransferPixRequest request);

    void updateYield();
}