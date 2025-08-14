package br.com.bank.java_bank.domain.repository;

// import org.springframework.data.r2dbc.repository.Query;
// import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

// import br.com.bank.java_bank.domain.DTO.AccountWithUserResponse;
import br.com.bank.java_bank.domain.model.AccountWallet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveCrudRepository<AccountWallet, Long> {

    // @Query("""
    //         SELECT aw.id AS account_id, aw.pix, aw.balance,
    //                 u.id AS user_id
    //                 FROM account_wallet aw
    //                 JOIN tb_user u ON aw.user_id = u.id
    //         WHERE aw.pix = :pix
    // """)
    // Mono<AccountWithUserResponse> findAccountWithUserByPix(@Param("pix") String pix);
    
    // @Query("""
    //         SELECT aw.id AS account_id, aw.pix, aw.balance,
    //                 u.id AS user_id
    //                 FROM account_wallet aw
    //                 JOIN tb_user u ON aw.user_id = u.id
    //         WHERE aw.user_id = :userId
    // """)
    // Flux<AccountWithUserResponse> findAccountsWithUserByUserId(@Param("userId") Long userId);

    Flux<AccountWallet> findAllByUserId(Long id);

    Mono<AccountWallet> findByPixAndUserId(String pix, Long userId);

    Mono<AccountWallet> findByPix(String pix);

    Mono<Boolean> existsByPix(String pix);
}
