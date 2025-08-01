package br.com.bank.java_bank.services;

import java.util.List;

import br.com.bank.java_bank.domain.model.InvestmentWallet;

public interface InvestmentWalletService {
    List<InvestmentWallet> findAll();

    InvestmentWallet investFromAccount(String pix, long investmentId);

    InvestmentWallet deposit(String pix, long amount);

    InvestmentWallet withdraw(String pix, long amount);
}
