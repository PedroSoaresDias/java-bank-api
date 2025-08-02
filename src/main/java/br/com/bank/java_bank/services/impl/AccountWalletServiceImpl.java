package br.com.bank.java_bank.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.bank.java_bank.domain.DTO.AccountResponse;
import br.com.bank.java_bank.domain.DTO.CreateAccountRequest;
import br.com.bank.java_bank.domain.DTO.DepositRequest;
import br.com.bank.java_bank.domain.DTO.TransferRequest;
import br.com.bank.java_bank.domain.DTO.WithdrawRequest;
import br.com.bank.java_bank.domain.model.AccountWallet;
import br.com.bank.java_bank.domain.repository.AccountRepository;
import br.com.bank.java_bank.exceptions.AccountNotFoundException;
import br.com.bank.java_bank.services.AccountWalletService;

@Service
public class AccountWalletServiceImpl implements AccountWalletService {

    private final AccountRepository accountRepository;

    public AccountWalletServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<AccountResponse> getAllAccounts() {
        List<AccountWallet> wallets = accountRepository.findAll();
        List<AccountResponse> response = wallets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return response;
    }

    @Override
    public AccountResponse getAccountByPix(String pix) {
        AccountWallet wallet = accountRepository.findByPixContaining(pix)
                .orElseThrow(() -> new AccountNotFoundException("Conta não encontrada."));
        AccountResponse account = convertToDTO(wallet);
        return account;
    }

    @Override
    public AccountResponse createAccount(CreateAccountRequest request) {
        AccountWallet wallet = new AccountWallet();
        wallet.setPix(request.pix());
        wallet.setBalance(request.balance());

        AccountWallet walletSaved = accountRepository.save(wallet);
        return convertToDTO(walletSaved);
    }

    @Override
    public void deposit(DepositRequest request) {
        AccountWallet wallet = accountRepository.findByPixContaining(request.pix())
                .orElseThrow(() -> new AccountNotFoundException("Conta não encontrada."));

        wallet.deposit(request.amount());
        accountRepository.save(wallet);
    }

    @Override
    public void withdraw(WithdrawRequest request) {
        AccountWallet wallet = accountRepository.findByPixContaining(request.pix())
                .orElseThrow(() -> new AccountNotFoundException("Conta não encontrada."));

        wallet.withdraw(request.amount());
        accountRepository.save(wallet);
    }

    @Override
    public void transfer(TransferRequest request) {
        AccountWallet source = accountRepository.findByPixContaining(request.fromPix())
                .orElseThrow(() -> new AccountNotFoundException("Conta de origem não foi encontrada"));
        AccountWallet target = accountRepository.findByPixContaining(request.toPix())
                .orElseThrow(() -> new AccountNotFoundException("Conta de destino não foi encontrada"));

        source.withdraw(request.amount());
        target.deposit(request.amount());

        accountRepository.save(source);
        accountRepository.save(target);
    }

    private AccountResponse convertToDTO(AccountWallet account) {
        return new AccountResponse(account.getId(), account.getPix(), account.getBalance());
    }

}
