package br.com.bank.java_bank.services.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

import br.com.bank.java_bank.domain.model.AccountWallet;
import br.com.bank.java_bank.domain.model.BankService;
import br.com.bank.java_bank.domain.model.Money;
import br.com.bank.java_bank.domain.repository.AccountRepository;
// import br.com.bank.java_bank.domain.repository.MoneyAuditRepository;
import br.com.bank.java_bank.domain.repository.MoneyRepository;
import br.com.bank.java_bank.exceptions.AccountNotFoundException;
import br.com.bank.java_bank.exceptions.PixInUseException;
import br.com.bank.java_bank.services.AccountWalletService;
import br.com.bank.java_bank.services.CommonsService;
import jakarta.transaction.Transactional;

@Service
public class AccountWalletServiceImpl implements AccountWalletService {

    private final AccountRepository accountRepository;
    private final MoneyRepository moneyRepository;
    // private final MoneyAuditRepository moneyAuditRepository;
    private final CommonsService commonsService;

    public AccountWalletServiceImpl(final AccountRepository accountRepository, final MoneyRepository moneyRepository, final CommonsService commonsService) {
        this.accountRepository = accountRepository;
        this.moneyRepository = moneyRepository;
        
        this.commonsService = commonsService;
    }

    @Override
    public List<AccountWallet> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public AccountWallet getAccountByPix(String pix) {
        return accountRepository.findByPixKeysContaining(pix).orElseThrow(() -> new AccountNotFoundException("Conta com chave Pix '" + pix + "' não encontrada."));
    }

    @Override
    public AccountWallet createAccount(List<String> pix, long amount, String description) {
        pix.forEach(p -> {
            accountRepository.findByPixKeysContaining(p).ifPresent(acc -> {
                throw new PixInUseException("Chave Pix '" + p + "' já está em uso.");
            });
        });

        AccountWallet account = new AccountWallet();
        account.setPix(pix);
        account.setService(BankService.ACCOUNT);

        accountRepository.save(account);

        UUID transactionId = UUID.randomUUID();
        List<Money> generatedMoney = commonsService.generateMoney(transactionId, BankService.ACCOUNT, amount,
                description, account);
        generatedMoney.forEach(money -> {
            money.setWallet(account);
            money.getHistory().forEach(h -> h.setMoney(money));
        });

        moneyRepository.saveAll(generatedMoney);
        account.getMoney().addAll(generatedMoney);

        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public void deposit(String pix, long amount, String description) {
        AccountWallet wallet = getAccountByPix(pix);

        UUID transactionId = UUID.randomUUID();
        List<Money> moneyList = commonsService.generateMoney(transactionId, BankService.ACCOUNT, amount, description,
                wallet);
        moneyList.forEach(money -> {
            money.setWallet(wallet);
            money.getHistory().forEach(h -> h.setMoney(money));
        });

        moneyRepository.saveAll(moneyList);
        wallet.getMoney().addAll(moneyList);

        accountRepository.save(wallet);
    }

    @Override
    public void withdraw(String pix, long amount) {
        AccountWallet wallet = getAccountByPix(pix);

        commonsService.checkFundsForTransaction(wallet, amount);

        List<Money> toWithdraw = wallet.getMoney().stream()
            .limit(amount)
                .toList();
    
        wallet.getMoney().removeAll(toWithdraw);
        moneyRepository.deleteAll(toWithdraw);

        accountRepository.save(wallet);
    }

    // @Override
    // public Map<OffsetDateTime, List<MoneyAudit>> getHistory(String pix) {
    //     AccountWallet wallet = getAccountByPix(pix);

    //     List<MoneyAudit> audit = wallet.getHistory();
    //     return audit.stream().collect(Collectors.groupingBy(t -> t.createdAt().truncatedTo(SECONDS)));
    // }

    // @Override
    // public void transfer(String fromPix, String toPix, long amount) {
    //     throw new UnsupportedOperationException("Unimplemented method 'transfer'");
    // }

}
