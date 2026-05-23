package com.programandoenjava.iambackend.auth.service;

import com.nimbusds.jose.JOSEException;
import com.programandoenjava.iambackend.user.entity.User;
import com.programandoenjava.iambackend.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public String authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Credenciales de acceso incorrectas."));
        try {
            return jwtService.createToken(user);
        } catch (JOSEException e) {
            // La relanzamos como RuntimeException para que el Controller no se entere
            throw new RuntimeException("Error técnico al generar el token", e);
        }

    }
}
