package br.com.bank.java_bank.services;

import java.util.List;

import br.com.bank.java_bank.domain.DTO.AccountResponse;
import br.com.bank.java_bank.domain.DTO.CreateAccountRequest;
import br.com.bank.java_bank.domain.DTO.DepositRequest;
import br.com.bank.java_bank.domain.DTO.TransferRequest;
import br.com.bank.java_bank.domain.DTO.WithdrawRequest;

public interface AccountWalletService {
    List<AccountResponse> getAllAccounts();
    
    AccountResponse getAccountByPix(String pix);

    AccountResponse createAccount(CreateAccountRequest request);

    void deposit(DepositRequest request);

    void withdraw(WithdrawRequest request);

    void transfer(TransferRequest request);
}
