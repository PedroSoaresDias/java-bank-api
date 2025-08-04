package br.com.bank.java_bank.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.bank.java_bank.domain.DTO.CreateInvestmentWalletRequest;
import br.com.bank.java_bank.domain.DTO.InvestmentDepositRequest;
import br.com.bank.java_bank.domain.DTO.InvestmentResponse;
import br.com.bank.java_bank.domain.model.AccountWallet;
// import br.com.bank.java_bank.domain.model.AccountWallet;
import br.com.bank.java_bank.domain.model.InvestmentWallet;
import br.com.bank.java_bank.domain.model.User;
import br.com.bank.java_bank.domain.repository.AccountRepository;
import br.com.bank.java_bank.domain.repository.InvestmentRepository;
import br.com.bank.java_bank.domain.repository.UserRepository;
import br.com.bank.java_bank.exceptions.AccountNotFoundException;
// import br.com.bank.java_bank.exceptions.AccountNotFoundException;
// import br.com.bank.java_bank.exceptions.AccountWithInvestmentException;
import br.com.bank.java_bank.exceptions.InvestmentNotFoundException;
import br.com.bank.java_bank.exceptions.UserNotFoundException;
import br.com.bank.java_bank.services.InvestmentWalletService;

@Service
public class InvestmentWalletServiceImpl implements InvestmentWalletService {

    private final InvestmentRepository investmentRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public InvestmentWalletServiceImpl(InvestmentRepository investmentRepository, AccountRepository accountRepository, UserRepository userRepository) {
        this.investmentRepository = investmentRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<InvestmentResponse> findAllInvestments() {
        List<InvestmentWallet> wallets = investmentRepository.findAll();
        List<InvestmentResponse> response = wallets.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return response;
    }

    @Override
    public InvestmentResponse findInvestmentById(Long id) {
        InvestmentWallet wallet = investmentRepository.findById(id)
                .orElseThrow(() -> new InvestmentNotFoundException("Investimento não encontrado"));
        InvestmentResponse response = toDTO(wallet);
        return response;
    }

    @Override
    public InvestmentWallet create(CreateInvestmentWalletRequest request) {
        // InvestmentWallet wallet = investmentRepository.findByPixContaining(request.pix())
        //         .orElseThrow(() -> new AccountWithInvestmentException("Conta não encontrada"));

        // if (investmentRepository.findByAccount(account).isPresent()) {
        //     throw new AccountWithInvestmentException("Conta já possui uma carteira de investimento");
        // }

        InvestmentWallet wallet = new InvestmentWallet();
        // wallet.setWallet(account);
        // wallet.setPix(request.pix());
        wallet.setBalance(request.amount());
        wallet.setInitialDeposit(request.amount());
        wallet.setTax(request.tax());

        return investmentRepository.save(wallet);
    }
 
    @Override
    public void invest(String email, InvestmentDepositRequest request) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        AccountWallet account = accountRepository.findByUserId(user.getId())
                .orElseThrow(() -> new AccountNotFoundException("Conta não encontrada"));
        InvestmentWallet investment = investmentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new InvestmentNotFoundException("Carteira de investimento não encontrada"));
        
        account.withdraw(request.amount());
        investment.deposit(request.amount());

        accountRepository.save(account);
        investmentRepository.save(investment);
    }
    
    @Override
    public void withdraw(String email, InvestmentDepositRequest request) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        AccountWallet account = accountRepository.findByUserId(user.getId())
                .orElseThrow(() -> new AccountNotFoundException("Conta não encontrada"));
        InvestmentWallet investment = investmentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new InvestmentNotFoundException("Carteira de investimento não encontrada"));
        
        investment.withdraw(request.amount());
        account.deposit(request.amount());

        investmentRepository.save(investment);
        accountRepository.save(account);
    }

    @Override
    public void updateYield() {
        List<InvestmentWallet> wallets = investmentRepository.findAll();
        for (InvestmentWallet wallet : wallets) {
            wallet.updateYield();
        }

        investmentRepository.saveAll(wallets);
    }

    private InvestmentResponse toDTO(InvestmentWallet wallet) {
        return new InvestmentResponse(wallet.getId(), wallet.getBalance(), wallet.getTax());
    }
}
