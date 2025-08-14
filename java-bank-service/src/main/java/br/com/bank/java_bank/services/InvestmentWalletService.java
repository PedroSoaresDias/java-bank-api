package br.com.bank.java_bank.services;

import br.com.bank.java_bank.domain.DTO.CreateInvestmentWalletRequest;
import br.com.bank.java_bank.domain.DTO.InvestmentResponse;
import br.com.bank.java_bank.domain.DTO.TransferPixRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InvestmentWalletService {
    Flux<InvestmentResponse> findAllMyInvestments();

    Mono<InvestmentResponse> findInvestmentByPix(String pix);

    Mono<Void> create(CreateInvestmentWalletRequest request);

    Mono<Void> invest(TransferPixRequest request);

    Mono<Void> withdraw(TransferPixRequest request);

    Mono<Void> updateYield();
}
