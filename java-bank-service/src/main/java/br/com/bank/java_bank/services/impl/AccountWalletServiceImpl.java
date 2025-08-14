package br.com.bank.java_bank.services.impl;

import org.springframework.stereotype.Service;

import br.com.bank.java_bank.domain.DTO.AccountResponse;
// import br.com.bank.java_bank.domain.DTO.AccountWithUserResponse;
import br.com.bank.java_bank.domain.DTO.CreateAccountRequest;
import br.com.bank.java_bank.domain.DTO.DepositRequest;
import br.com.bank.java_bank.domain.DTO.TransferPixRequest;
import br.com.bank.java_bank.domain.DTO.WithdrawRequest;
import br.com.bank.java_bank.domain.model.AccountWallet;
import br.com.bank.java_bank.domain.repository.AccountRepository;
// import br.com.bank.java_bank.domain.repository.AccountRepositoryCustom;
// import br.com.bank.java_bank.domain.repository.UserRepository;
import br.com.bank.java_bank.exceptions.AccountNotFoundException;
import br.com.bank.java_bank.exceptions.UnauthorizatedAccessException;
import br.com.bank.java_bank.services.AccountWalletService;
import br.com.bank.java_bank.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class AccountWalletServiceImpl implements AccountWalletService {

    private final AccountRepository accountRepository;
    // private final SecurityUtil securityUtil;

    public AccountWalletServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        // this.securityUtil = securityUtil;
    }

    @Override
    public Flux<AccountResponse> getAllMyAccounts() {
        return SecurityUtil.getAuthenticatedUserId()
                .flatMapMany(userId -> accountRepository.findAllByUserId(userId)
                        .filter(w -> w.getUserId().equals(userId))
                        .switchIfEmpty(Mono
                                .error(new UnauthorizatedAccessException(
                                        "Você não tem permissão para acessar essa conta.")))
                        .map(this::convertToDTO));
    }

    @Override
    public Mono<AccountResponse> getAccountByPix(String pix) {
        log.debug("Solicitação de busca por Pix: {}", pix);
        return SecurityUtil.getAuthenticatedUserId()
                .flatMap(userId -> accountRepository.findByPixAndUserId(pix, userId)
                        .switchIfEmpty(Mono.error(new AccountNotFoundException("Conta não encontrada.")))
                        .map(this::convertToDTO));
    }

    @Override
    public Mono<Void> createAccount(CreateAccountRequest request) {
        log.debug("Solicitação de criação de conta: {}", request);
        return SecurityUtil.getAuthenticatedUserId()
                .flatMap(userId -> {
                    AccountWallet account = new AccountWallet();
                    account.setPix(request.pix());
                    account.setBalance(request.balance());
                    account.setUserId(userId);

                    return accountRepository.save(account).then();
                });
    }

    @Override
    public Mono<Void> deposit(DepositRequest request) {
        log.debug("Solicitação de depósito: {}", request);
        return SecurityUtil.getAuthenticatedUserId()
                .flatMap(userId -> accountRepository.findByPixAndUserId(request.pix(), userId)
                        .switchIfEmpty(Mono.error(new AccountNotFoundException("Conta não encontrada.")))
                        .flatMap(account -> {
                            account.deposit(request.amount());
                            return accountRepository.save(account);
                        }))
                .then();
    }

    @Override
    public Mono<Void> withdraw(WithdrawRequest request) {
        log.debug("Solicitação de saque: {}", request);
        return SecurityUtil.getAuthenticatedUserId()
                .flatMap(userId -> accountRepository.findByPixAndUserId(request.pix(), userId)
                        .switchIfEmpty(Mono.error(new AccountNotFoundException("Conta não encontrada.")))
                        .flatMap(account -> {
                            account.withdraw(request.amount());
                            return accountRepository.save(account);
                        }))
                .then();
    }

    @Override
    public Mono<Void> transfer(TransferPixRequest request) {
        log.debug("Solicitação de transferência: {}", request);
        return SecurityUtil.getAuthenticatedUserId()
                .flatMap(userId -> {
                    Mono<AccountWallet> sourceMono = accountRepository.findByPix(request.fromPix())
                            .switchIfEmpty(Mono.defer(() -> {
                                log.warn("Conta de origem não encontrada: {}", request.fromPix());
                                return Mono.error(new AccountNotFoundException("Conta de origem não foi encontrada"));
                            }));

                    Mono<AccountWallet> targetMono = accountRepository.findByPix(request.toPix())
                            .switchIfEmpty(Mono.defer(() -> {
                                log.warn("Conta de destino não encontrada: {}", request.toPix());
                                return Mono.error(new AccountNotFoundException("Conta de destino não foi encontrada"));
                            }));

                    return Mono.zip(sourceMono, targetMono)
                            .flatMap(tuple -> {
                                AccountWallet source = tuple.getT1();
                                AccountWallet target = tuple.getT2();

                                if (!source.getUserId().equals(userId)) {
                                    log.warn("Usuário {} tentou transferir de conta que não lhe pertence: {}", userId,
                                            request.fromPix());
                                    return Mono.error(new UnauthorizatedAccessException(
                                            "Você não tem permissão para transferir."));
                                }

                                source.withdraw(request.amount());
                                target.deposit(request.amount());

                                log.info("Transferência de {} de {} para {} iniciada", request.amount(),
                                        request.fromPix(), request.toPix());
                                return accountRepository.save(source)
                                        .then(accountRepository.save(target))
                                        .doOnSuccess(v -> log.info("Transferência concluída com sucesso"))
                                        .then();
                            });
                });
    }

    // private Mono<AccountWallet> verifyOwnership(AccountWallet account, Long
    // userId) {
    // if (!account.getUserId().equals(userId)) {
    // log.warn("Acesso não autorizado à conta Pix {} pelo usuário {}",
    // account.getPix(), userId);
    // return Mono.error(new UnauthorizatedAccessException("Acesso não autorizado à
    // conta."));
    // }
    // return Mono.just(account);
    // }

    // private Mono<Long> getAuthenticatedUserIdOrError() {
    // Long userId = SecurityUtil.getAuthenticatedUserId();
    // log.debug("Verificando autenticação do usuário...");
    // if (userId == null) {
    // log.error("Usuário não autenticado");
    // return Mono.error(new UnauthorizatedAccessException("Usuário não
    // autenticado."));
    // }
    // log.debug("Usuário autenticado com ID: {}", userId);
    // return Mono.just(userId);
    // }

    private AccountResponse convertToDTO(AccountWallet account) {
        return new AccountResponse(account.getId(), account.getPix(),
                account.getBalance());
    }

}
