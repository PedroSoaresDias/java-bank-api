package br.com.bank.java_bank.services;

import br.com.bank.java_bank.domain.DTO.AccountResponse;
import br.com.bank.java_bank.domain.DTO.CreateAccountRequest;
import br.com.bank.java_bank.domain.DTO.DepositRequest;
import br.com.bank.java_bank.domain.DTO.TransferPixRequest;
import br.com.bank.java_bank.domain.DTO.WithdrawRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountWalletService {
    Flux<AccountResponse> getAllMyAccounts();
    
    Mono<AccountResponse> getAccountByPix(String pix);

    Mono<Void> createAccount(CreateAccountRequest request);

    Mono<Void> deposit(DepositRequest request);

    Mono<Void> withdraw(WithdrawRequest request);

    Mono<Void> transfer(TransferPixRequest request);
}
