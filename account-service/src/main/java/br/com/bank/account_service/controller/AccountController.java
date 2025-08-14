package br.com.bank.account_service.controller;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @GetMapping
    public Flux<BigDecimal> getAllInvestments() {
        return Flux.just(BigDecimal.valueOf(600.00), BigDecimal.valueOf(350.00), BigDecimal.valueOf(720.00));
    }
}
