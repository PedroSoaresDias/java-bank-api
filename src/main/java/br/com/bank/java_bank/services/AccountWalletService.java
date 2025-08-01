package br.com.bank.java_bank.services;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import br.com.bank.java_bank.domain.model.AccountWallet;
import br.com.bank.java_bank.domain.model.MoneyAudit;

public interface AccountWalletService {
    List<AccountWallet> getAllAccounts();

    AccountWallet getAccountByPix(String pix);

    AccountWallet createAccount(List<String> pix, long amount, String description);

    void deposit(String pix, long amount, String description);

    void withdraw(String pix, long amount);

    Map<OffsetDateTime, List<MoneyAudit>> getHistory(String pix);
}
