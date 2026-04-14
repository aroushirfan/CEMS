package com.cems.cemsbackend.service;

import com.cems.cemsbackend.model.AccessLevel;
import com.cems.cemsbackend.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

/**
 * Service for handling JSON Web Token (JWT) generation, parsing, and validation.
 */
@Service
public class JwtService {

  private final SecretKey jwtSecret;
  private final long accessKeyExpiration = 1000L * 60L * 60L * 24L * 3L;

  /**
   * Initializes the JwtService with a secret key.
   *
   * @param jwtSecret the base64 encoded secret string from configuration.
   */
  public JwtService(
      @Value("${jwt.secret}") String jwtSecret
  ) {
    this.jwtSecret = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret));
  }

  /**
   * Generates a JWT for a specific user.
   *
   * @param user the user for whom the token is generated.
   * @return a signed JWT string.
   */
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

  /**
   * Parses all claims from a given token.
   *
   * @param token the JWT string to parse.
   * @return the claims contained in the token, or null if invalid.
   */
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

  /**
   * Extracts the user ID from the token claims.
   *
   * @param claims the claims extracted from the token.
   * @return the UUID of the user.
   */
  public UUID getUserIdFromClaims(Claims claims) {
    return UUID.fromString(claims.getSubject());
  }

  /**
   * Extracts the user's role and converts it to a GrantedAuthority.
   *
   * @param claims the claims extracted from the token.
   * @return the GrantedAuthority for Spring Security, or null if no role is present.
   */
  public GrantedAuthority getRoleFromClaims(Claims claims) {
    if (claims.containsKey("role")) {
      return new SimpleGrantedAuthority(String.format("ROLE_%s", claims.get("role", String.class)));
    } else {
      return null;
    }
  }
}
