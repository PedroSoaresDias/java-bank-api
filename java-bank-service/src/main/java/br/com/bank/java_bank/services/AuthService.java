package br.com.bank.java_bank.services;

import br.com.bank.java_bank.domain.DTO.AuthRequest;
import br.com.bank.java_bank.domain.DTO.AuthResponse;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<AuthResponse> authenticate(AuthRequest request);
}
