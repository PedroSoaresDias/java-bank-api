package br.com.bank.java_bank_api.services;

import java.util.List;

import br.com.bank.java_bank_api.domain.DTO.AccountResponse;
import br.com.bank.java_bank_api.domain.DTO.CreateAccountRequest;
import br.com.bank.java_bank_api.domain.DTO.DepositRequest;
import br.com.bank.java_bank_api.domain.DTO.TransferPixRequest;
import br.com.bank.java_bank_api.domain.DTO.WithdrawRequest;

public interface AccountWalletService {
    List<AccountResponse> getAllMyAccounts();

    AccountResponse getAccountByPix(String pix);

    AccountResponse createAccount(CreateAccountRequest request);

    void deposit(DepositRequest request);

    void withdraw(WithdrawRequest request);

    void transfer(TransferPixRequest request);
}
