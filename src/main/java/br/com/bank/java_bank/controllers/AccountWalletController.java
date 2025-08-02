package br.com.bank.java_bank.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.bank.java_bank.domain.DTO.AccountResponse;
import br.com.bank.java_bank.domain.DTO.CreateAccountRequest;
import br.com.bank.java_bank.domain.DTO.DepositRequest;
import br.com.bank.java_bank.domain.DTO.TransferRequest;
import br.com.bank.java_bank.domain.DTO.WithdrawRequest;
import br.com.bank.java_bank.services.AccountWalletService;

@RestController
@RequestMapping("/accounts")
public class AccountWalletController {
    private final AccountWalletService service;

    public AccountWalletController(AccountWalletService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        return ResponseEntity.ok(service.getAllAccounts());
    }

    @GetMapping("/{pix}")
    public ResponseEntity<AccountResponse> getAccountByPix(@PathVariable("pix") String pix) {
        return ResponseEntity.ok(service.getAccountByPix(pix));
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody CreateAccountRequest request) {
        service.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/deposit")
    public ResponseEntity<Void> deposit(@RequestBody DepositRequest request) {
        service.deposit(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@RequestBody WithdrawRequest request) {
        service.withdraw(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody TransferRequest request) {
        service.transfer(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
