package br.com.bank.account_service.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import br.com.bank.account_service.domain.model.AccountWallet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveCrudRepository<AccountWallet, Long> {
    Flux<AccountWallet> findAllByUserId(Long id);

    Mono<AccountWallet> findByPixAndUserId(String pix, Long userId);

    Mono<AccountWallet> findByPix(String pix);

    Mono<Boolean> existsByPix(String pix);
}
