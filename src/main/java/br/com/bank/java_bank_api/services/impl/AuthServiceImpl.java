package br.com.bank.java_bank_api.services.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.bank.java_bank_api.domain.DTO.AuthRequest;
import br.com.bank.java_bank_api.domain.DTO.AuthResponse;
import br.com.bank.java_bank_api.domain.model.User;
import br.com.bank.java_bank_api.services.AuthService;
import br.com.bank.java_bank_api.domain.repository.UserRepository;
import br.com.bank.java_bank_api.exceptions.AuthenticationException;
import br.com.bank.java_bank_api.utils.JwtTokenGenerator;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenGenerator jwtGenerator;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
            JwtTokenGenerator jwtGenerator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AuthenticationException("Credenciais inválidas");
        }

        String token = jwtGenerator.generateToken(user.getId());
        return new AuthResponse(token);
    }

    public Long getAuthenticatedUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String userId = auth.getName();
            return Long.parseLong(userId);
        }
        return null;
    }

}
