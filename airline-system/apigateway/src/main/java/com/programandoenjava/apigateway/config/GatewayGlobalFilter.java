package com.programandoenjava.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class GatewayGlobalFilter implements GlobalFilter, Ordered {

    @Value("${gateway.internal.token}")
    private String gatewayInternalToken;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> {
                    Authentication auth = securityContext.getAuthentication();
                    ServerHttpRequest.Builder builder = exchange.getRequest().mutate();

                    // La contraseña para que Vuelos y Reservas abran la puerta
                    builder.header("X-Gateway-Token",    gatewayInternalToken);

                    // Si trae JWT, le pasamos los datos a los microservicios
                    if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
                        String username = jwt.getSubject();
                        List<String> roles = jwt.getClaimAsStringList("roles");

                        if (username != null) {
                            builder.header("X-Username", username);
                        }
                        if (roles != null && !roles.isEmpty()) {
                            builder.header("X-User-Roles", String.join(",", roles));
                        }
                    }
                    return exchange.mutate().request(builder.build()).build();
                })
                .switchIfEmpty(Mono.fromCallable(() -> {
                    // Si es una ruta pública (como el Login), solo inyectamos la contraseña interna
                    ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
                    builder.header("X-Gateway-Token", gatewayInternalToken);
                    return exchange.mutate().request(builder.build()).build();
                }))
                .flatMap(mutatedExchange -> chain.filter(mutatedExchange));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}