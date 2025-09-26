package br.com.bank.java_bank_api.services.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.bank.java_bank_api.domain.DTO.AccountResponse;
import br.com.bank.java_bank_api.domain.DTO.CreateAccountRequest;
import br.com.bank.java_bank_api.domain.DTO.DepositRequest;
import br.com.bank.java_bank_api.domain.DTO.TransferPixRequest;
import br.com.bank.java_bank_api.domain.DTO.WithdrawRequest;
import br.com.bank.java_bank_api.domain.model.AccountWallet;
import br.com.bank.java_bank_api.domain.model.User;
import br.com.bank.java_bank_api.domain.repository.AccountRepository;
import br.com.bank.java_bank_api.exceptions.AccountNotFoundException;
import br.com.bank.java_bank_api.exceptions.UnauthorizatedAccessException;
import br.com.bank.java_bank_api.services.AccountWalletService;
import br.com.bank.java_bank_api.utils.SecurityUtil;
import jakarta.transaction.Transactional;
import br.com.bank.java_bank_api.domain.repository.UserRepository;
import br.com.bank.java_bank_api.exceptions.PixInUseException;
import br.com.bank.java_bank_api.exceptions.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountWalletServiceImpl implements AccountWalletService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountWalletServiceImpl(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<AccountResponse> getAllMyAccounts(Pageable pageable) {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        Page<AccountWallet> wallets = accountRepository.findAccountsByUserId(pageable, userId);
        Page<AccountResponse> response = wallets
                .map(this::convertToDTO);

        return response.toList();
    }

    @Override
    public AccountResponse getAccountByPix(String pix) {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        AccountWallet wallet = accountRepository.findByPixContaining(pix)
                .orElseThrow(() -> new AccountNotFoundException("Conta não encontrada."));

        if (!wallet.getUser().getId().equals(userId)) {
            throw new UnauthorizatedAccessException("Você não tem permissão para ver essa conta.");
        }

        AccountResponse account = convertToDTO(wallet);
        return account;
    }

    @Override
    public AccountResponse createAccount(CreateAccountRequest request) {
        Long userId = SecurityUtil.getAuthenticatedUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AccountNotFoundException("Conta não encontrada."));

        if (accountRepository.existsByPix(request.pix())) {
            throw new PixInUseException("Chave Pix em uso.");
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
