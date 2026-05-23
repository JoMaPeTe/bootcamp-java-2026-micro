package com.programandoenjava.iambackend.auth.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.programandoenjava.iambackend.auth.entity.Role;
import com.programandoenjava.iambackend.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class JWTService {
    private final String secret;
    private final String expiration;

    public JWTService(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") String expiration) {
        this.secret = secret;
        this.expiration = expiration;
    }

    public JWTClaimsSet createClaimSet(User user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .claim("id", user.getId())  // ✅ Agregar ID del usuario
                .issueTime(new Date())
                .claim("roles", roleNames)
                .issuer("https://mi-api.com")
                .expirationTime(new Date(System.currentTimeMillis() + Long.parseLong(expiration)))
                .build();
    }


    public String createToken(User user) throws JOSEException {
        final JWTClaimsSet claims = createClaimSet(user);
        JWSSigner signer = new MACSigner(secret);
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
        signedJWT.sign(signer);
        String token = signedJWT.serialize();
        return token;
    }

    public boolean validateToken(final String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(secret);
        return signedJWT.verify(verifier);
    }

    public String getSubject(final String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet().getSubject();
    }

    public String getClaim(String claimName, final String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet().getClaim(claimName).toString();
    }

    public List<String> getRolesClaim(final String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        Object roles = signedJWT.getJWTClaimsSet().getClaim("roles");

        if (roles instanceof List<?>) {
            return ((List<?>) roles).stream()
                    .map(Object::toString)
                    .toList();
        }
        return List.of(); // Devuelve lista vacía si no hay roles
    }
}