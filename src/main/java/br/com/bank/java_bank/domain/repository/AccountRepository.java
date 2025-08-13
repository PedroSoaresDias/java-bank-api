package br.com.bank.java_bank.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import br.com.bank.java_bank.domain.model.AccountWallet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveCrudRepository<AccountWallet, Long> {
    Mono<AccountWallet> findByPix(String pix);
    
    Flux<AccountWallet> findAccountsByUserId(Long userId);

    Mono<Boolean> existsByPix(String pix);
}
