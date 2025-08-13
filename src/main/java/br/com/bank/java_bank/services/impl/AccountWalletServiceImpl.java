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
        Long userId = SecurityUtil.getAuthenticatedUserId();

        return accountRepository.findAccountsByUserId(userId)
                .filter(w -> w.getUserId().equals(userId))
                .switchIfEmpty(Mono
                        .error(new UnauthorizatedAccessException("Você não tem permissão para acessar essa conta.")))
                .map(this::convertToDTO);

    }

    @Override
    public Mono<AccountResponse> getAccountByPix(String pix) {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        return accountRepository.findByPixContaining(pix)
                .filter(wallet -> wallet.getUserId().equals(userId))
                .switchIfEmpty(Mono.error(new AccountNotFoundException("Conta não encontrada.")))
                .map(this::convertToDTO);

        // AccountWallet wallet = accountRepository.findByPixContaining(pix)
        // .filter(w -> w.getUser().getId().equals(userId))
        // .orElseThrow(() -> new AccountNotFoundException("Conta não encontrada."));

        // if (!wallet.getUser().getId().equals(userId)) {
        // throw new UnauthorizatedAccessException("Você não tem permissão para acessar
        // essa conta.");
        // }

        // AccountResponse account = convertToDTO(wallet);
        // return account;
    }

    @Override
    public Mono<Void> createAccount(CreateAccountRequest request) {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new UserNotFoundException("Conta não encontrada.")))
                .flatMap(user -> {
                    accountRepository.existsByPix(request.pix())
                            .flatMap(exists -> {
                                if (exists) {
                                    return Mono.error(new AccountWithInvestmentException(
                                            "Já existe uma conta corrente com essa chave Pix."));
                                }

                                return Mono.empty();
                            });

                    AccountWallet wallet = new AccountWallet();
                    wallet.setPix(request.pix());
                    wallet.setBalance(request.balance());
                    wallet.setUserId(userId);
                    return accountRepository.save(wallet).then();
                });

        // User user = userRepository.findById(userId)
        // .orElseThrow(() -> new AccountNotFoundException());

        // if (accountRepository.existsByPix(request.pix())) {
        // throw new AccountWithInvestmentException("Já existe uma conta corrente com
        // essa chave Pix.");
        // }

        // AccountWallet wallet = new AccountWallet();
        // wallet.setPix(request.pix());
        // wallet.setBalance(request.balance());
        // wallet.setUser(user);

        // AccountWallet walletSaved = accountRepository.save(wallet);
        // return convertToDTO(walletSaved);
    }

    @Override
    public Mono<Void> deposit(DepositRequest request) {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        return accountRepository.findByPixContaining(request.pix())
                .switchIfEmpty(Mono.error(new AccountNotFoundException("Conta não encontrada")))
                .flatMap(wallet -> {
                    if (!wallet.getUserId().equals(userId)) {
                        return Mono.error(new UnauthorizatedAccessException("Sem permissão para depositar."));
                    }

                    wallet.deposit(request.amount());
                    return accountRepository.save(wallet).then();
                });
        // AccountWallet wallet = accountRepository.findByPixContaining(request.pix())
        // .orElseThrow(() -> new AccountNotFoundException("Conta não encontrada para o
        // Pix informado."));

        // if (!wallet.getUser().getId().equals(userId)) {
        // throw new UnauthorizatedAccessException("Você não tem permissão para
        // depositar nesta conta.");
        // }

        // wallet.deposit(request.amount());
        // accountRepository.save(wallet);
    }

    @Override
    public Mono<Void> withdraw(WithdrawRequest request) {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        return accountRepository.findByPixContaining(request.pix())
                .switchIfEmpty(Mono.error(new AccountNotFoundException("Conta não encontrada")))
                .flatMap(wallet -> {
                    if (!wallet.getUserId().equals(userId)) {
                        return Mono.error(new UnauthorizatedAccessException("Sem permissão para depositar."));
                    }

                    wallet.deposit(request.amount());
                    return accountRepository.save(wallet).then();
                });
    }

    @Override
    public Mono<Void> transfer(TransferPixRequest request) {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        Mono<AccountWallet> sourceMono = accountRepository.findByPixContaining(request.fromPix())
                .switchIfEmpty(Mono.error(new AccountNotFoundException("Conta de origem não foi encontrada")));
        Mono<AccountWallet> targetMono = accountRepository.findByPixContaining(request.toPix())
                .switchIfEmpty(Mono.error(new AccountNotFoundException("Conta de destino não foi encontrada")));

        return Mono.zip(sourceMono, targetMono)
                .flatMap(tuple -> {
                    AccountWallet source = tuple.getT1();
                    AccountWallet target = tuple.getT2();

                    if (!source.getUserId().equals(userId)) {
                        return Mono.error(new UnauthorizatedAccessException(
                                "Você não tem permissão para fazer transferência entre contas."));
                    }

                    source.withdraw(request.amount());
                    target.deposit(request.amount());

                    return accountRepository.save(source)
                            .then(accountRepository.save(target))
                            .then();
                });

        // if (!source.getUser().getId().equals(userId)) {
        // throw new UnauthorizatedAccessException("Você não tem permissão para fazer
        // transferência entre contas.");
        // }

        // source.withdraw(request.amount());
        // target.deposit(request.amount());

        // accountRepository.save(source);
        // accountRepository.save(target);
    }

    private AccountResponse convertToDTO(AccountWallet account) {
        return new AccountResponse(account.getId(), account.getPix(), account.getBalance());
    }

}
