package br.com.bank.java_bank.services.impl;

import org.springframework.stereotype.Service;

import br.com.bank.java_bank.domain.DTO.CreateInvestmentWalletRequest;
import br.com.bank.java_bank.domain.DTO.InvestmentResponse;
import br.com.bank.java_bank.domain.DTO.TransferPixRequest;
import br.com.bank.java_bank.domain.model.AccountWallet;
import br.com.bank.java_bank.domain.model.InvestmentWallet;
import br.com.bank.java_bank.domain.repository.AccountRepository;
import br.com.bank.java_bank.domain.repository.InvestmentRepository;
import br.com.bank.java_bank.domain.repository.UserRepository;
import br.com.bank.java_bank.exceptions.AccountNotFoundException;
import br.com.bank.java_bank.exceptions.AccountWithInvestmentException;
import br.com.bank.java_bank.exceptions.InvestmentNotFoundException;
import br.com.bank.java_bank.exceptions.UnauthorizatedAccessException;
import br.com.bank.java_bank.exceptions.UserNotFoundException;
import br.com.bank.java_bank.services.InvestmentWalletService;
import br.com.bank.java_bank.utils.SecurityUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class InvestmentWalletServiceImpl implements InvestmentWalletService {

    private final InvestmentRepository investmentRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public InvestmentWalletServiceImpl(
            InvestmentRepository investmentRepository,
            AccountRepository accountRepository,
            UserRepository userRepository) {
        this.investmentRepository = investmentRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Flux<InvestmentResponse> findAllMyInvestments() {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        return investmentRepository.findAllByUserId(userId)
                .filter(w -> w.getUserId().equals(userId))
                .switchIfEmpty(Mono
                        .error(new UnauthorizatedAccessException("Você não tem permissão para acessar essa conta.")))
                .map(this::toDTO);
    }

    @Override
    public Mono<InvestmentResponse> findInvestmentByPix(String pix) {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        return investmentRepository.findByPix(pix)
                .filter(wallet -> wallet.getUserId().equals(userId))
                .switchIfEmpty(Mono.error(new InvestmentNotFoundException("Conta de investimentos não encontrada.")))
                .map(this::toDTO);
    }

    @Override
    public Mono<Void> create(CreateInvestmentWalletRequest request) {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new UserNotFoundException("Conta não encontrada.")))
                .flatMap(user -> {
                    // if (investmentRepository.existsByPix(request.pix())) {
                    // throw new AccountWithInvestmentException("Já existe uma conta corrente com
                    // essa chave Pix.");
                    // }

                    investmentRepository.existsByPix(request.pix())
                            .flatMap(exists -> {
                                if (exists) {
                                    return Mono.error(new AccountWithInvestmentException(
                                            "Já existe uma conta corrente com essa chave Pix."));
                                }

                                return Mono.empty();
                            });
                    InvestmentWallet wallet = new InvestmentWallet();
                    wallet.setPix(request.pix());
                    wallet.setTax(request.tax());
                    wallet.setBalance(request.amount());
                    wallet.setInitialDeposit(request.amount());
                    wallet.setUserId(userId);
                    return investmentRepository.save(wallet).then();
                });
    }

    @Override
    public Mono<Void> invest(TransferPixRequest request) {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        Mono<AccountWallet> accountMono = accountRepository.findByPix(request.fromPix())
                .filter(w -> w.getUserId().equals(userId))
                .switchIfEmpty(Mono.error(new AccountNotFoundException("Conta de origem não foi encontrada")));
        Mono<InvestmentWallet> investmentMono = investmentRepository.findByPix(request.toPix())
                .filter(i -> i.getUserId().equals(userId))
                .switchIfEmpty(Mono.error(new InvestmentNotFoundException(
                        "Carteira de investimento não encontrada ou não pertence ao usuário.")));

        return Mono.zip(accountMono, investmentMono)
                .flatMap(tuple -> {
                    AccountWallet account = tuple.getT1();
                    InvestmentWallet investment = tuple.getT2();

                    if (!investment.getUserId().equals(userId)) {
                        throw new UnauthorizatedAccessException("Você não tem permissão para investir nesta conta.");
                    }

                    account.withdraw(request.amount());
                    investment.deposit(request.amount());

                    return accountRepository.save(account)
                            .then(investmentRepository.save(investment))
                            .then();
                });
    }

    @Override
    public Mono<Void> withdraw(TransferPixRequest request) {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        Mono<AccountWallet> accountMono = accountRepository.findByPix(request.fromPix())
                .filter(w -> w.getUserId().equals(userId))
                .switchIfEmpty(Mono.error(new AccountNotFoundException("Conta de origem não foi encontrada")));
        Mono<InvestmentWallet> investmentMono = investmentRepository.findByPix(request.toPix())
                .filter(i -> i.getUserId().equals(userId))
                .switchIfEmpty(Mono.error(new InvestmentNotFoundException(
                        "Carteira de investimento não encontrada ou não pertence ao usuário.")));

        return Mono.zip(accountMono, investmentMono)
                .flatMap(tuple -> {
                    AccountWallet account = tuple.getT1();
                    InvestmentWallet investment = tuple.getT2();

                    if (!investment.getUserId().equals(userId)) {
                        throw new UnauthorizatedAccessException(
                                "Você não tem permissão para resgatar o investimento nesta conta.");
                    }

                    investment.withdraw(request.amount());
                    account.deposit(request.amount());

                    return investmentRepository.save(investment)
                            .then(accountRepository.save(account))
                            .then();
                });
    }

    @Override
    public Mono<Void> updateYield() {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        return investmentRepository.findAllByUserId(userId)
                .filter(w -> w.getUserId().equals(userId))
                .switchIfEmpty(Mono
                        .error(new UnauthorizatedAccessException("Você não tem permissão para acessar essa conta.")))
                .map(wallet -> {
                    wallet.updateYield();
                    return wallet;
                })
                .flatMap(investmentRepository::save)
                .then();

        // userRepository.findById(userId)
        // .orElseThrow(
        // () -> new UnauthorizatedAccessException("Você não tem permissão para acessar
        // essa conta."));

        // List<InvestmentWallet> wallets = investmentRepository.findAll();
        // for (InvestmentWallet wallet : wallets) {
        // wallet.updateYield();
        // }

        // investmentRepository.saveAll(wallets);
    }

    private InvestmentResponse toDTO(InvestmentWallet wallet) {
        return new InvestmentResponse(wallet.getId(), wallet.getPix(), wallet.getBalance(), wallet.getTax());
    }
}
