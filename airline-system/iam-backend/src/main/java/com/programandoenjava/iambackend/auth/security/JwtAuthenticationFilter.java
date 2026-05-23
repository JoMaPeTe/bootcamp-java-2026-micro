package com.programandoenjava.iambackend.auth.security;

import com.nimbusds.jose.JOSEException;
import com.programandoenjava.iambackend.auth.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JWTService jwtService;

    @Autowired
    public JwtAuthenticationFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        // 1. Si no hay header o no empieza por Bearer, ignoramos y seguimos la cadena, sino no podemos entrar al login
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 2. Extraemos el token real (quitando "Bearer ")
        String token = authHeader.substring(7);
        try {
            boolean isValid = jwtService.validateToken(token);
            if (isValid) {
                String username = jwtService.getSubject(token);
                List<String> roles = jwtService.getRolesClaim(token);
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))

                        .toList();
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorities
                );
                SecurityContextHolder.getContext().setAuthentication(auth);

                filterChain.doFilter(request, response);

            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalido");
            }
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
