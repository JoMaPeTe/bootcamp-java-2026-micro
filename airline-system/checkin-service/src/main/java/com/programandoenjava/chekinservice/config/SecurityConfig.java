package com.programandoenjava.chekinservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final GatewayTokenFilter gatewayTokenFilter;

    public SecurityConfig(GatewayTokenFilter gatewayTokenFilter) {
        this.gatewayTokenFilter = gatewayTokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                // ✅ El filtro lee quién es el usuario desde las cabeceras que manda el Gateway
                .addFilterBefore(gatewayTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        // ❌ BORRADO: .httpBasic(...)

        return http.build();
    }

    // ❌ BORRADO: userDetailsService() -> Ya no hace falta "quemar" usuarios aquí
}