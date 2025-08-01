package br.com.bank.java_bank.services;

// import java.time.OffsetDateTime;
// import java.util.List;
// import java.util.UUID;
// import java.util.stream.LongStream;

import org.springframework.stereotype.Component;

// import br.com.bank.java_bank.domain.model.BankService;
// import br.com.bank.java_bank.domain.model.Money;
// import br.com.bank.java_bank.domain.model.MoneyAudit;
import br.com.bank.java_bank.domain.model.Wallet;
import br.com.bank.java_bank.exceptions.NoFundsEnoughException;

@Component
public class CommonsService {
    public void checkFundsForTransaction(Wallet wallet, long amount) {
        if (wallet.getFunds() < amount) {
            throw new NoFundsEnoughException("Sua conta não tem dinheiro o suficiente para realizar essa transação.");
        }
    }

    // public List<Money> generateMoney(UUID transactionId, long amount, String description, BankService service) {
    //     MoneyAudit audit = new MoneyAudit();
    //     audit.setTransactionId(transactionId);
    //     audit.setService(service);
    //     audit.setDescription(description);
    //     audit.setCreatedAt(OffsetDateTime.now());

    //     return LongStream.range(0, amount)
    //     .mapToObj(i -> new Money(audit))
    //             .toList();
    // }
}
