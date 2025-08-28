package br.com.bank.java_bank_api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bank.java_bank_api.domain.DTO.CreateInvestmentWalletRequest;
import br.com.bank.java_bank_api.domain.DTO.InvestmentResponse;
import br.com.bank.java_bank_api.domain.DTO.TransferPixRequest;
import br.com.bank.java_bank_api.domain.model.InvestmentWallet;
import br.com.bank.java_bank_api.services.InvestmentWalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Buscar todas as contas de investimento do usuário autenticado")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description = "Todas as contas encontradas"),
        @ApiResponse(responseCode = "403", description = "O usuário não tem permissão de acessar as contas")    
    })
    @GetMapping
    public ResponseEntity<List<InvestmentResponse>> findAllInvestments() {
        return ResponseEntity.ok(service.findAllMyInvestments());
    }

    @Operation(summary = "Buscar a conta de investimento do usuário autenticado com a chave Pix")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description = "Conta de investimento encontradas pela chave Pix"),
        @ApiResponse(responseCode = "403", description = "O usuário não tem permissão de acessar essa conta"),
        @ApiResponse(responseCode = "404", description = "Conta de investimento não encontrada")    
    })
    @GetMapping("/{pix}")
    public ResponseEntity<InvestmentResponse> findInvestmentByPix(@Valid @PathVariable("pix") String pix) {
        return ResponseEntity.ok(service.findInvestmentByPix(pix));
    }

    @Operation(summary = "Criar uma conta de investimento com o usuário autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Conta de investimento criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Falha ao criar uma conta de investimento"),
        @ApiResponse(responseCode = "403", description = "O usuário não tem permissão de criar uma conta")
    })
    @PostMapping
    public ResponseEntity<InvestmentWallet> create(@Valid @RequestBody CreateInvestmentWalletRequest request) {
        service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Fazer um investimento para a conta de investimento com o usuário autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Aplicação do investimento realizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Falha ao aplicar um investimento"),
        @ApiResponse(responseCode = "403", description = "O usuário não tem permissão de realizar um investimento nesta conta"),
        @ApiResponse(responseCode = "404", description = "Conta de origem não encontrada"),
        @ApiResponse(responseCode = "404", description = "Conta de destino não encontrada"),
    })
    @PostMapping("/invest")
    public ResponseEntity<Void> invest(@Valid @RequestBody TransferPixRequest request) {
        service.invest(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Fazer um resgate do investimento para a conta corrente com o usuário autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Resgate do investimento realizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Falha ao resgatar um investimento"),
        @ApiResponse(responseCode = "403", description = "O usuário não tem permissão de realizar um resgate do investimento nesta conta"),
        @ApiResponse(responseCode = "404", description = "Conta de origem não encontrada"),
        @ApiResponse(responseCode = "404", description = "Conta de destino não encontrada"),
    })
    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@Valid @RequestBody TransferPixRequest request) {
        service.withdraw(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Atualizar os rendimentos dos investimentos com o usuário autenticado")
    @ApiResponses(value={
        @ApiResponse(responseCode = "200", description = "Rendimentos atualizados com sucesso"),
        @ApiResponse(responseCode = "403", description = "O usuário não tem permissão de atualizar os investimentos essa conta")
    })
    @PostMapping("/yield/update")
    public ResponseEntity<Void> updateYield() {
        service.updateYield();
        return ResponseEntity.ok().build();
    }
}
