package br.com.bank.user_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/users")
public class UserController {
    @GetMapping
    public Flux<String> getAllUsers() {
        return Flux.just("javeiro", "type", "cobaia");
    }
}
