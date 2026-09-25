package com.rotaclara.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

/**
 * Emite e valida os JWT usados para autenticação stateless da API
 * (RNF04 — controle de acesso por perfil).
 */
@Service
public class JwtService {

    private final SecretKey chave;
    private final long expiracaoMinutos;

    public JwtService(
            @Value("${rotaclara.jwt.secret}") String secret,
            @Value("${rotaclara.jwt.expiracao-minutos}") long expiracaoMinutos
    ) {
        this.chave = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiracaoMinutos = expiracaoMinutos;
    }

    public String gerarToken(String login, String perfil) {
        Instant agora = Instant.now();
        return Jwts.builder()
                .subject(login)
                .claim("perfil", perfil)
                .issuedAt(Date.from(agora))
                .expiration(Date.from(agora.plusSeconds(expiracaoMinutos * 60)))
                .signWith(chave)
                .compact();
    }

    public Claims validarEExtrairClaims(String token) {
        return Jwts.parser()
                .verifyWith(chave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
