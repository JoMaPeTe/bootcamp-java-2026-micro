package com.programandoenjava.flightservice.flight.infrastructure.config;

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
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                //  IMPORTANTE: Añadimos el filtro del Portero
                .addFilterBefore(gatewayTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //  BORRADO: .httpBasic(...) -> ¡Ya no queremos entrar por la puerta de atrás!

        return http.build();
    }

    //  BORRADO: userDetailsService() -> El microservicio ya no necesita conocer contraseñas
}