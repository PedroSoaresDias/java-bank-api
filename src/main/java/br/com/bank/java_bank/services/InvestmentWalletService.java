package br.com.bank.java_bank.services;

import java.util.List;

import br.com.bank.java_bank.domain.DTO.CreateInvestmentWalletRequest;
import br.com.bank.java_bank.domain.DTO.InvestmentResponse;
import br.com.bank.java_bank.domain.DTO.TransferPixRequest;
import br.com.bank.java_bank.domain.model.InvestmentWallet;

public interface InvestmentWalletService {
    List<InvestmentResponse> findAllMyInvestments();

    InvestmentResponse findInvestmentByPix(String pix);

    InvestmentWallet create(CreateInvestmentWalletRequest request);

    void invest(TransferPixRequest request);

    void withdraw(TransferPixRequest request);

    void updateYield();
}
