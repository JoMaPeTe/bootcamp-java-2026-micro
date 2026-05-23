package com.programandoenjava.iambackend.auth.controller;

import com.programandoenjava.iambackend.auth.dto.AuthDto;
import com.programandoenjava.iambackend.auth.service.AuthServiceImpl;
import com.programandoenjava.iambackend.user.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthServiceImpl authServiceImpl;

    public AuthController(AuthServiceImpl authServiceImpl) {
        this.authServiceImpl = authServiceImpl;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDto> login(@RequestBody UserDto userDto) {
        try {
            // Intentamos hacer el login
            String token = authServiceImpl.authenticate(userDto.username(), userDto.password());
            return ResponseEntity.ok(new AuthDto(token));
        } catch (Exception e) {
            // Si falla, capturamos el error y mostramos el motivo exacto
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthDto("Error en el login: " + e.getMessage()));
        }
    }
}
