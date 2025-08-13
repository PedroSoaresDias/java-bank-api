package br.com.bank.java_bank.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import br.com.bank.java_bank.domain.model.InvestmentWallet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InvestmentRepository extends ReactiveCrudRepository<InvestmentWallet, Long> {    
    Mono<InvestmentWallet> findByPixContaining(String pix);

    Flux<InvestmentWallet> findAllByUserId(Long userId);

    Mono<Boolean> existsByPix(String pix);
}
