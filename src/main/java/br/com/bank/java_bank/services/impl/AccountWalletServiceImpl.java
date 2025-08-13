package br.com.bank.java_bank.services.impl;

import org.springframework.stereotype.Service;

import br.com.bank.java_bank.domain.DTO.AccountResponse;
import br.com.bank.java_bank.domain.DTO.CreateAccountRequest;
import br.com.bank.java_bank.domain.DTO.DepositRequest;
import br.com.bank.java_bank.domain.DTO.TransferPixRequest;
import br.com.bank.java_bank.domain.DTO.WithdrawRequest;
import br.com.bank.java_bank.domain.model.AccountWallet;
import br.com.bank.java_bank.domain.repository.AccountRepository;
import br.com.bank.java_bank.domain.repository.UserRepository;
import br.com.bank.java_bank.exceptions.AccountNotFoundException;
import br.com.bank.java_bank.exceptions.AccountWithInvestmentException;
import br.com.bank.java_bank.exceptions.UnauthorizatedAccessException;
import br.com.bank.java_bank.exceptions.UserNotFoundException;
import br.com.bank.java_bank.services.AccountWalletService;
import br.com.bank.java_bank.utils.SecurityUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountWalletServiceImpl implements AccountWalletService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountWalletServiceImpl(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Flux<AccountResponse> getAllMyAccounts() {
        return getAuthenticatedUserIdOrError()
                .flatMapMany(userId -> {
                    System.out.println("Buscando contas para o usuário: " + userId);
                    return accountRepository.findAccountsByUserId(userId)
                            .doOnNext(wallet -> System.out.println("Conta encontrada: " + wallet.getPix()))
                            .map(this::convertToDTO);
                });

    }

    @Override
    public Mono<AccountResponse> getAccountByPix(String pix) {
        return getAuthenticatedUserIdOrError()
                .flatMap(userId -> {
                    return accountRepository.findByPixContaining(pix)
                            .filter(wallet -> wallet.getUserId().equals(userId))
                            .switchIfEmpty(Mono.error(new AccountNotFoundException("Conta não encontrada")))
                            .map(this::convertToDTO);
                });
    }

    @Override
    public Mono<Void> createAccount(CreateAccountRequest request) {
        return getAuthenticatedUserIdOrError()
                .flatMap(userId -> userRepository.findById(userId)
                        .switchIfEmpty(Mono.error(new UserNotFoundException("Conta não encontrada.")))
                        .flatMap(user -> accountRepository.existsByPix(request.pix())
                                .flatMap(exists -> {
                                    if (exists) {
                                        return Mono.error(
                                                new AccountWithInvestmentException(
                                                        "Já existe uma conta com essa chave Pix."));
                                    }

                                    AccountWallet wallet = new AccountWallet();
                                    wallet.setPix(request.pix());
                                    wallet.setBalance(request.balance());
                                    wallet.setUserId(userId);
                                    return accountRepository.save(wallet).then();
                                })));
    }

    @Override
    public Mono<Void> deposit(DepositRequest request) {
        return getAuthenticatedUserIdOrError()
                .flatMap(userId -> accountRepository.findByPixContaining(request.pix())
                        .switchIfEmpty(Mono.error(new AccountNotFoundException("Conta não encontrada")))
                        .flatMap(wallet -> {
                            if (!wallet.getUserId().equals(userId)) {
                                return Mono.error(new UnauthorizatedAccessException("Sem permissão para depositar."));
                            }

                            wallet.deposit(request.amount());
                            return accountRepository.save(wallet).then();
                        }));
    }

    @Override
    public Mono<Void> withdraw(WithdrawRequest request) {
        return getAuthenticatedUserIdOrError().flatMap(userId -> accountRepository.findByPixContaining(request.pix())
                .switchIfEmpty(Mono.error(new AccountNotFoundException("Conta não encontrada")))
                .flatMap(wallet -> {
                    if (!wallet.getUserId().equals(userId)) {
                        return Mono.error(new UnauthorizatedAccessException("Sem permissão para depositar."));
                    }

                    wallet.withdraw(request.amount());
                    return accountRepository.save(wallet).then();
                }));
    }

    @Override
    public Mono<Void> transfer(TransferPixRequest request) {
        return getAuthenticatedUserIdOrError()
                .flatMap(userId -> {
                    Mono<AccountWallet> sourceMono = accountRepository.findByPixContaining(request.fromPix())
                            .switchIfEmpty(
                                    Mono.error(new AccountNotFoundException("Conta de origem não foi encontrada")));

                    Mono<AccountWallet> targetMono = accountRepository.findByPixContaining(request.toPix())
                            .switchIfEmpty(
                                    Mono.error(new AccountNotFoundException("Conta de destino não foi encontrada")));

                    return Mono.zip(sourceMono, targetMono)
                            .flatMap(tuple -> {
                                AccountWallet source = tuple.getT1();
                                AccountWallet target = tuple.getT2();

                                if (!source.getUserId().equals(userId)) {
                                    return Mono.error(new UnauthorizatedAccessException(
                                            "Você não tem permissão para transferir."));
                                }

                                source.withdraw(request.amount());
                                target.deposit(request.amount());

                                return accountRepository.save(source)
                                        .then(accountRepository.save(target))
                                        .then();
                            });
                });
    }

    private Mono<Long> getAuthenticatedUserIdOrError() {
        Long userId = SecurityUtil.getAuthenticatedUserId();
        System.out.println("Usuário autenticado: " + userId);
        if (userId == null) {
            return Mono.error(new UnauthorizatedAccessException("Usuário não autenticado."));
        }
        return Mono.just(userId);
    }

    private AccountResponse convertToDTO(AccountWallet account) {
        return new AccountResponse(account.getId(), account.getPix(), account.getBalance());
    }

}
