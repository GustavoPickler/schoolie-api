package com.api.auth.util;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtGenerator {

    // Chave secreta para assinatura do token (não segura, apenas para teste)
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Tempo de validade do token (1 hora)
    private static final long VALIDITY_DURATION = 3600000;

    public String generateToken(String email) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + VALIDITY_DURATION);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey)
                .compact();
    }
}