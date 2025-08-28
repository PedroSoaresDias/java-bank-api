package br.com.bank.java_bank_api.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.bank.java_bank_api.domain.DTO.CreateInvestmentWalletRequest;
import br.com.bank.java_bank_api.domain.DTO.InvestmentResponse;
import br.com.bank.java_bank_api.domain.DTO.TransferPixRequest;
import br.com.bank.java_bank_api.domain.model.AccountWallet;
import br.com.bank.java_bank_api.domain.model.InvestmentWallet;
import br.com.bank.java_bank_api.domain.model.User;
import br.com.bank.java_bank_api.domain.repository.AccountRepository;
import br.com.bank.java_bank_api.domain.repository.InvestmentRepository;
import br.com.bank.java_bank_api.domain.repository.UserRepository;
import br.com.bank.java_bank_api.exceptions.AccountNotFoundException;
import br.com.bank.java_bank_api.exceptions.AccountWithInvestmentException;
import br.com.bank.java_bank_api.exceptions.InvestmentNotFoundException;
import br.com.bank.java_bank_api.exceptions.UnauthorizatedAccessException;
import br.com.bank.java_bank_api.exceptions.UserNotFoundException;
import br.com.bank.java_bank_api.services.InvestmentWalletService;
import br.com.bank.java_bank_api.utils.SecurityUtil;
import jakarta.transaction.Transactional;

@Service
public class InvestmentWalletServiceImpl implements InvestmentWalletService {

    private final InvestmentRepository investmentRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public InvestmentWalletServiceImpl(InvestmentRepository investmentRepository, AccountRepository accountRepository,
            UserRepository userRepository) {
        this.investmentRepository = investmentRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<InvestmentResponse> findAllMyInvestments() {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        List<InvestmentWallet> wallets = investmentRepository.findAllByUserId(userId);
        List<InvestmentResponse> response = wallets.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return response;
    }

    @Override
    public InvestmentResponse findInvestmentByPix(String pix) {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        InvestmentWallet wallet = investmentRepository.findByPixContaining(pix)
                .orElseThrow(() -> new InvestmentNotFoundException("Investimento não encontrado"));

        if (!wallet.getUser().getId().equals(userId)) {
            throw new UnauthorizatedAccessException("Você não tem permissão para acessar essa conta.");
        }

        InvestmentResponse response = toDTO(wallet);
        return response;
    }

    @Override
    public InvestmentWallet create(CreateInvestmentWalletRequest request) {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        if (investmentRepository.existsByPix(request.pix())) {
            throw new AccountWithInvestmentException("Já existe uma conta de investimento com essa chave Pix.");
        }

        InvestmentWallet wallet = new InvestmentWallet();
        wallet.setPix(request.pix());
        wallet.setBalance(request.amount());
        wallet.setInitialDeposit(request.amount());
        wallet.setTax(request.tax());
        wallet.setUser(user);

        return investmentRepository.save(wallet);
    }
 

    @Override
    @Transactional
    public void invest(TransferPixRequest request) {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        AccountWallet account = accountRepository.findByPixContaining(request.fromPix())
                .filter(a -> a.getUser().getId().equals(userId))
                .orElseThrow(() -> new AccountNotFoundException("Conta não encontrada"));
        
        InvestmentWallet investment = investmentRepository.findByPixContaining(request.toPix())
                .filter(i -> i.getUser().getId().equals(userId))
                .orElseThrow(() -> new InvestmentNotFoundException(
                        "Carteira de investimento não encontrada ou não pertence ao usuário."));

        if (!investment.getUser().getId().equals(userId)) {
            throw new UnauthorizatedAccessException("Você não tem permissão para investir nesta conta.");
        }

        account.withdraw(request.amount());
        investment.deposit(request.amount());

        accountRepository.save(account);
        investmentRepository.save(investment);
    }
    

    @Override
    @Transactional
    public void withdraw(TransferPixRequest request) {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        InvestmentWallet investment = investmentRepository.findByPixContaining(request.fromPix())
                .filter(i -> i.getUser().getId().equals(userId))
                .orElseThrow(() -> new InvestmentNotFoundException(
                        "Carteira de investimento não encontrada ou não pertence ao usuário."));
        AccountWallet account = accountRepository.findByPixContaining(request.toPix())
                .filter(a -> a.getUser().getId().equals(userId))
                .orElseThrow(() -> new AccountNotFoundException("Conta não encontrada"));


        if (!investment.getUser().getId().equals(userId)) {
            throw new UnauthorizatedAccessException("Você não tem permissão para resgatar o investimento nesta conta.");
        }

        investment.withdraw(request.amount());
        account.deposit(request.amount());

        investmentRepository.save(investment);
        accountRepository.save(account);
    }

    @Override
    public void updateYield() {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));

        List<InvestmentWallet> wallets = investmentRepository.findAll();
        for (InvestmentWallet wallet : wallets) {
            wallet.updateYield();
        }

        investmentRepository.saveAll(wallets);
    }

    private InvestmentResponse toDTO(InvestmentWallet wallet) {
            return new InvestmentResponse(wallet.getId(), wallet.getPix(), wallet.getBalance(), wallet.getTax());
    }
}
