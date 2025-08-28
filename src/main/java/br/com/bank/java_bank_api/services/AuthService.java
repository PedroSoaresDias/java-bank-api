package br.com.bank.java_bank_api.services;

import br.com.bank.java_bank_api.domain.DTO.AuthRequest;
import br.com.bank.java_bank_api.domain.DTO.AuthResponse;


public interface AuthService {
    AuthResponse authenticate(AuthRequest request);
}
