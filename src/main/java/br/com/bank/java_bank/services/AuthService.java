package br.com.bank.java_bank.services;

import br.com.bank.java_bank.domain.DTO.AuthRequest;
import br.com.bank.java_bank.domain.DTO.AuthResponse;

public interface AuthService {
    AuthResponse authenticate(AuthRequest request);
}
