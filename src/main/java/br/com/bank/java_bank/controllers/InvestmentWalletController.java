package br.com.bank.java_bank.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bank.java_bank.domain.DTO.CreateInvestmentWalletRequest;
import br.com.bank.java_bank.domain.DTO.InvestmentDepositRequest;
import br.com.bank.java_bank.domain.DTO.InvestmentResponse;
import br.com.bank.java_bank.domain.model.InvestmentWallet;
import br.com.bank.java_bank.services.InvestmentWalletService;

@RestController
@RequestMapping("/investments")
public class InvestmentWalletController {
    private final InvestmentWalletService service;

    public InvestmentWalletController(InvestmentWalletService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<InvestmentResponse>> findAllInvestments() {
        return ResponseEntity.ok(service.findAllInvestments());
    }

    @GetMapping("/{pix}")
    public ResponseEntity<InvestmentResponse> findInvestmentByPix(@PathVariable("pix") String pix) {
        return ResponseEntity.ok(service.findInvestmentByPix(pix));
    }

    @PostMapping
    public ResponseEntity<InvestmentWallet> create(@RequestBody CreateInvestmentWalletRequest request) {
        service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/invest")
    public ResponseEntity<Void> invest(@RequestBody InvestmentDepositRequest request) {
        service.invest(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@RequestBody InvestmentDepositRequest request) {
        service.withdraw(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    @PostMapping("/yield/update")
    public ResponseEntity<Void> updateYield() {
        service.updateYield();
        return ResponseEntity.ok().build();
    }
}
