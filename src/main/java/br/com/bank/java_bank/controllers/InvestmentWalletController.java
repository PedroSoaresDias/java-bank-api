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
import br.com.bank.java_bank.domain.DTO.InvestmentResponse;
import br.com.bank.java_bank.domain.DTO.TransferPixRequest;
import br.com.bank.java_bank.domain.model.InvestmentWallet;
import br.com.bank.java_bank.services.InvestmentWalletService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/investments")
@Tag(name = "Conta de Investimentos", description = "Operações com conta de investimentos")
public class InvestmentWalletController {
    private final InvestmentWalletService service;

    public InvestmentWalletController(InvestmentWalletService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<InvestmentResponse>> findAllInvestments() {
        return ResponseEntity.ok(service.findAllMyInvestments());
    }

    @GetMapping("/{pix}")
    public ResponseEntity<InvestmentResponse> findInvestmentByPix(@Valid @PathVariable("pix") String pix) {
        return ResponseEntity.ok(service.findInvestmentByPix(pix));
    }

    @PostMapping
    public ResponseEntity<InvestmentWallet> create(@Valid @RequestBody CreateInvestmentWalletRequest request) {
        service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/invest")
    public ResponseEntity<Void> invest(@Valid @RequestBody TransferPixRequest request) {
        service.invest(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@Valid @RequestBody TransferPixRequest request) {
        service.withdraw(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    @PostMapping("/yield/update")
    public ResponseEntity<Void> updateYield() {
        service.updateYield();
        return ResponseEntity.ok().build();
    }
}
