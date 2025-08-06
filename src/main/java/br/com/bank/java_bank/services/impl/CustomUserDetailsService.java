package br.com.bank.java_bank.services.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.bank.java_bank.domain.model.User;
import br.com.bank.java_bank.domain.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Long userId = Long.parseLong(id);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário com ID " + id + " não encontrado."));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getId().toString())
                .password(user.getPassword())
                .build();
    }

}
