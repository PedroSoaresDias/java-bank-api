package br.com.bank.investment_service.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import br.com.bank.investment_service.domain.model.InvestmentWallet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InvestmentRepository extends ReactiveCrudRepository<InvestmentWallet, Long> {    
    Mono<InvestmentWallet> findByPix(String pix);

    Flux<InvestmentWallet> findAllByUserId(Long userId);

    Mono<InvestmentWallet> findByPixAndUserId(String pix, Long userId);

    Mono<Boolean> existsByPix(String pix);
}
