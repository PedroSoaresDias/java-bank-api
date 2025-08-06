package br.com.bank.java_bank.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.bank.java_bank.domain.DTO.AccountResponse;
import br.com.bank.java_bank.domain.DTO.CreateAccountRequest;
import br.com.bank.java_bank.domain.DTO.DepositRequest;
import br.com.bank.java_bank.domain.DTO.TransferPixRequest;
import br.com.bank.java_bank.domain.DTO.WithdrawRequest;
import br.com.bank.java_bank.domain.model.AccountWallet;
import br.com.bank.java_bank.domain.model.User;
import br.com.bank.java_bank.domain.repository.AccountRepository;
import br.com.bank.java_bank.domain.repository.UserRepository;
import br.com.bank.java_bank.exceptions.AccountNotFoundException;
import br.com.bank.java_bank.exceptions.AccountWithInvestmentException;
import br.com.bank.java_bank.exceptions.UnauthorizatedAccessException;
import br.com.bank.java_bank.exceptions.UserNotFoundException;
import br.com.bank.java_bank.services.AccountWalletService;
import br.com.bank.java_bank.utils.SecurityUtil;
import jakarta.transaction.Transactional;

@Service
public class AccountWalletServiceImpl implements AccountWalletService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountWalletServiceImpl(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<AccountResponse> getAllMyAccounts() {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        List<AccountWallet> wallets = accountRepository.findAccountsByUserId(userId);
        List<AccountResponse> response = wallets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return response;
    }

    @Override
    public AccountResponse getAccountByPix(String pix) {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        AccountWallet wallet = accountRepository.findByPixContaining(pix)
                .filter(w -> w.getUser().getId().equals(userId))
                .orElseThrow(() -> new AccountNotFoundException("Conta não encontrada."));
        AccountResponse account = convertToDTO(wallet);
        return account;
    }

    @Override
    public AccountResponse createAccount(CreateAccountRequest request) {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AccountNotFoundException("Conta não encontrada."));

        if (accountRepository.existsByPix(request.pix())) {
            throw new AccountWithInvestmentException("Já existe uma conta de investimento com essa chave Pix.");
        }


        AccountWallet wallet = new AccountWallet();
        wallet.setPix(request.pix());
        wallet.setBalance(request.balance());
        wallet.setUser(user);

        AccountWallet walletSaved = accountRepository.save(wallet);
        return convertToDTO(walletSaved);
    }

    @Override
    @Transactional
    public void deposit(DepositRequest request) {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        AccountWallet wallet = accountRepository.findByPixContaining(request.pix())
                .orElseThrow(() -> new AccountNotFoundException("Conta não encontrada para o Pix informado."));

        if (!wallet.getUser().getId().equals(userId)) {
            throw new UnauthorizatedAccessException("Você não tem permissão para depositar nesta conta.");
        }

        wallet.deposit(request.amount());
        accountRepository.save(wallet);
    }

    @Override
    @Transactional
    public void withdraw(WithdrawRequest request) {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        AccountWallet wallet = accountRepository.findByPixContaining(request.pix())
                .orElseThrow(() -> new AccountNotFoundException("Conta não encontrada para o Pix informado."));

        if (!wallet.getUser().getId().equals(userId)) {
            throw new UnauthorizatedAccessException("Você não tem permissão para depositar nesta conta.");
        }

        wallet.withdraw(request.amount());
        accountRepository.save(wallet);
    }

    @Override
    @Transactional
    public void transfer(TransferPixRequest request) {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        AccountWallet source = accountRepository.findByPixContaining(request.fromPix())
                .orElseThrow(() -> new AccountNotFoundException("Conta de origem não foi encontrada"));
        AccountWallet target = accountRepository.findByPixContaining(request.toPix())
                .orElseThrow(() -> new AccountNotFoundException("Conta de destino não foi encontrada"));

        if (!source.getUser().getId().equals(userId)) {
            throw new UnauthorizatedAccessException("Você não tem permissão para fazer transferencia entre contas.");
        }

        source.withdraw(request.amount());
        target.deposit(request.amount());

        accountRepository.save(source);
        accountRepository.save(target);
    }

    private AccountResponse convertToDTO(AccountWallet account) {
        return new AccountResponse(account.getId(), account.getPix(), account.getBalance());
    }

}
