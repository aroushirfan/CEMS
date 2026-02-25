package com.cems.cemsbackend.service;

import com.cems.cemsbackend.model.AccessLevel;
import com.cems.cemsbackend.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey jwtSecret;
    private final long accessKeyExpiration = 1000L * 60L * 60L * 24L * 3L;

    public JwtService(
            @Value("${jwt.secret}") String jwtSecret
    ) {
        this.jwtSecret = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret));
    }

    public String generateToken(User user) {
        final var now = new Date();
        final var expiryDate = new Date(now.getTime() + accessKeyExpiration);
        String role;
        if (user.getAccessLevel() == AccessLevel.ADMIN) {
            role = "ADMIN";
        } else if (user.getAccessLevel() == AccessLevel.FACULTY) {
            role = "FACULTY";
        } else {
            role = "USER";
        }
        return Jwts.builder()
                .signWith(jwtSecret)
                .issuedAt(now)
                .expiration(expiryDate)
                .subject(user.getId().toString())
                .claim("role", role)
                .compact();
    }

    public Claims parseAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(jwtSecret)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            return null;
        }
    }

    public UUID getUserIdFromClaims(Claims claims) {
        return UUID.fromString(claims.getSubject());
    }

    public GrantedAuthority getRoleFromClaims(Claims claims) {
        if (claims.containsKey("role")) {
            return new SimpleGrantedAuthority(String.format("ROLE_%s", claims.get("role", String.class)));
        } else {
            return null;
        }
    }
}
