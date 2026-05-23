package com.programandoenjava.paymentservice.payment.infrastructure.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Filtro que verifica que TODOS los requests vienen del API Gateway
 * Y propaga la información de autenticación del usuario
 */
@Component
public class GatewayTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LogManager.getLogger(GatewayTokenFilter.class);

    @Value("${gateway.internal.token}")
    private String gatewayInternalToken;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String requestUri = request.getRequestURI();
        logger.info("GatewayTokenFilter: {} {}", request.getMethod(), requestUri);

        // Obtener headers propagados del Gateway
        String gatewayToken = request.getHeader("X-Gateway-Token");
        String userId = request.getHeader("X-User-Id");
        String username = request.getHeader("X-Username");
        String userRoles = request.getHeader("X-User-Roles");

        // ❌ Verifying: ¿Viene del Gateway?
        if (gatewayToken == null) {
            logger.warn("BLOCKED: Missing X-Gateway-Token from {}", request.getRemoteAddr());
            sendErrorResponse(response,
                "Missing X-Gateway-Token",
                "This microservice requires requests from API Gateway only");
            return;
        }

        // ❌ Verifying: ¿Token es válido?
        if (!gatewayToken.equals(gatewayInternalToken)) {
            logger.warn("BLOCKED: Invalid X-Gateway-Token from {}", request.getRemoteAddr());
            sendErrorResponse(response,
                "Invalid X-Gateway-Token",
                "Gateway token verification failed");
            return;
        }

        // ✅ Token válido, crear Authentication con roles reales
        logger.info("ALLOWED: Valid gateway token for user: {} with roles: {}", username, userRoles);

        // Crear autenticación con datos propagados del Gateway
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                    username != null ? username : "anonymous",
                    null,
                    buildAuthorities(userRoles)
                );

        // Guardar en contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Continuar con siguiente filter
        filterChain.doFilter(request, response);
    }

    /**
     * Construir autoridades basadas en roles del header
     */
    private List<SimpleGrantedAuthority> buildAuthorities(String userRoles) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if (userRoles != null && !userRoles.trim().isEmpty()) {
            String[] roles = userRoles.split(",");
            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.trim()));
            }
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return authorities;
    }

    /**
     * Enviar respuesta de error en formato JSON
     */
    private void sendErrorResponse(
            HttpServletResponse response,
            String error,
            String message) throws IOException {

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String jsonResponse = String.format(
            "{\"error\": \"%s\", \"message\": \"%s\"}",
            error,
            message
        );

        response.getWriter().write(jsonResponse);
    }
}
