package br.com.bank.java_bank_api.controllers;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.bank.java_bank_api.domain.DTO.AccountResponse;
import br.com.bank.java_bank_api.domain.DTO.CreateAccountRequest;
import br.com.bank.java_bank_api.domain.DTO.DepositRequest;
import br.com.bank.java_bank_api.domain.DTO.TransferPixRequest;
import br.com.bank.java_bank_api.domain.DTO.WithdrawRequest;
import br.com.bank.java_bank_api.services.AccountWalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/accounts")
@Slf4j
@Tag(name = "Conta Corrente", description = "Operações com conta corrente")
public class AccountWalletController {
    private final AccountWalletService service;

    public AccountWalletController(AccountWalletService service) {
        this.service = service;
    }

    @Operation(summary = "Buscar todas as contas correntes do usuário autenticado")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description = "Todas as contas encontradas"),
        @ApiResponse(responseCode = "403", description = "O usuário não tem permissão de acessar as contas")    
    })
    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts(@RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "20", name = "size") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return ResponseEntity.ok(service.getAllMyAccounts(pageable));
    }

    @Operation(summary = "Buscar a conta corrente do usuário autenticado com a chave Pix")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description = "Conta corrente encontradas pela chave Pix"),
        @ApiResponse(responseCode = "403", description = "O usuário não tem permissão de acessar essa conta"),
        @ApiResponse(responseCode = "404", description = "Conta corrente não encontrada")    
    })
    @GetMapping("/{pix}")
    public ResponseEntity<AccountResponse> getAccountByPix(@Valid @PathVariable("pix") String pix) {
        return ResponseEntity.ok(service.getAccountByPix(pix));
    }

    @Operation(summary = "Criar uma conta corrente com o usuário autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Conta corrente criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Falha ao criar uma conta corrente"),
        @ApiResponse(responseCode = "403", description = "O usuário não tem permissão de criar uma conta")
    })
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        service.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Fazer um depósito na conta corrente com o usuário autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Depósito realizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Falha ao depositar na conta corrente"),
        @ApiResponse(responseCode = "403", description = "O usuário não tem permissão de depositar nesta conta"),
        @ApiResponse(responseCode = "404", description = "Conta corrente não encontrada")
    })
    @PostMapping("/deposit")
    public ResponseEntity<Void> deposit(@Valid @RequestBody DepositRequest request) {
        service.deposit(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Fazer um saque da conta corrente com o usuário autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Saque realizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Falha ao sacar aa conta corrente"),
        @ApiResponse(responseCode = "403", description = "O usuário não tem permissão de sacar desta conta"),
        @ApiResponse(responseCode = "404", description = "Conta corrente não encontrada")
    })
    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@Valid @RequestBody WithdrawRequest request) {
        service.withdraw(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Fazer uma transferência entre contas corrente com o usuário autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Transferência realizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Falha ao fazer a transferência entre contas"),
        @ApiResponse(responseCode = "403", description = "O usuário não tem permissão de realizar transferência entre contas"),
        @ApiResponse(responseCode = "404", description = "Conta de origem não encontrada"),
        @ApiResponse(responseCode = "404", description = "Conta de destino não encontrada"),
    })
    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransferPixRequest request) {
        service.transfer(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
