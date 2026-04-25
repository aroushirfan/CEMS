package com.cems.cemsbackend.service;

import com.cems.cemsbackend.model.AccessLevel;
import com.cems.cemsbackend.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JwtService jwtService;
    private SecretKey secretKey;
    private static final String TEST_SECRET = "dGVzdFNlY3JldEtleUZvclRlc3RpbmdQdXJwb3Nlc1RoaXNJc0xvbmdFbm91Z2g="; // Base64 encoded
    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(TEST_SECRET));
        jwtService = new JwtService(TEST_SECRET);
        
        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
    }

    @Test
    void generateToken_WithAdminUser_IncludesAdminRole() {
        user.setAccessLevel(AccessLevel.ADMIN);

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        Claims claims = parseTokenWithSecret(token);
        assertEquals("ADMIN", claims.get("role", String.class));
        assertEquals(userId.toString(), claims.getSubject());
    }

    @Test
    void generateToken_WithFacultyUser_IncludesFacultyRole() {
        user.setAccessLevel(AccessLevel.FACULTY);

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        Claims claims = parseTokenWithSecret(token);
        assertEquals("FACULTY", claims.get("role", String.class));
        assertEquals(userId.toString(), claims.getSubject());
    }

    @Test
    void generateToken_WithRegularUser_IncludesUserRole() {
        user.setAccessLevel(AccessLevel.USER);

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        Claims claims = parseTokenWithSecret(token);
        assertEquals("USER", claims.get("role", String.class));
        assertEquals(userId.toString(), claims.getSubject());
    }

    @Test
    void generateToken_HasExpirationDate() {
        user.setAccessLevel(AccessLevel.USER);

        String token = jwtService.generateToken(user);

        Claims claims = parseTokenWithSecret(token);
        assertNotNull(claims.getExpiration());
        assertTrue(claims.getExpiration().after(new Date()));
    }

    @Test
    void generateToken_HasIssuedAtDate() {
        user.setAccessLevel(AccessLevel.USER);

        String token = jwtService.generateToken(user);

        Claims claims = parseTokenWithSecret(token);
        assertNotNull(claims.getIssuedAt());
    }

    @Test
    void parseAllClaims_WithValidToken_ReturnsClaims() {
        user.setAccessLevel(AccessLevel.ADMIN);
        String token = jwtService.generateToken(user);

        Claims claims = jwtService.parseAllClaims(token);

        assertNotNull(claims);
        assertEquals(userId.toString(), claims.getSubject());
        assertEquals("ADMIN", claims.get("role", String.class));
    }

    @Test
    void parseAllClaims_WithInvalidToken_ReturnsNull() {
        String invalidToken = "invalid.token.here";

        Claims claims = jwtService.parseAllClaims(invalidToken);

        assertNull(claims);
    }

    @Test
    void parseAllClaims_WithMalformedToken_ReturnsNull() {
        String malformedToken = "malformed";

        Claims claims = jwtService.parseAllClaims(malformedToken);

        assertNull(claims);
    }

    @Test
    void parseAllClaims_WithEmptyToken_ReturnsNull() {
        String emptyToken = "";

        Claims claims = jwtService.parseAllClaims(emptyToken);

        assertNull(claims);
    }

    @Test
    void parseAllClaims_WithNullToken_ReturnsNull() {
        Claims claims = jwtService.parseAllClaims(null);

        assertNull(claims);
    }

    @Test
    void getUserIdFromClaims_ExtractsCorrectUuid() {
        user.setAccessLevel(AccessLevel.USER);
        String token = jwtService.generateToken(user);
        Claims claims = jwtService.parseAllClaims(token);

        UUID extractedId = jwtService.getUserIdFromClaims(claims);

        assertEquals(userId, extractedId);
    }

    @Test
    void getUserIdFromClaims_WithDifferentUuid() {
        UUID differentId = UUID.randomUUID();
        user.setId(differentId);
        user.setAccessLevel(AccessLevel.USER);
        String token = jwtService.generateToken(user);
        Claims claims = jwtService.parseAllClaims(token);

        UUID extractedId = jwtService.getUserIdFromClaims(claims);

        assertEquals(differentId, extractedId);
    }

    @Test
    void getRoleFromClaims_WithAdminRole_ReturnsCorrectAuthority() {
        user.setAccessLevel(AccessLevel.ADMIN);
        String token = jwtService.generateToken(user);
        Claims claims = jwtService.parseAllClaims(token);

        GrantedAuthority authority = jwtService.getRoleFromClaims(claims);

        assertNotNull(authority);
        assertEquals("ROLE_ADMIN", authority.getAuthority());
    }

    @Test
    void getRoleFromClaims_WithFacultyRole_ReturnsCorrectAuthority() {
        user.setAccessLevel(AccessLevel.FACULTY);
        String token = jwtService.generateToken(user);
        Claims claims = jwtService.parseAllClaims(token);

        GrantedAuthority authority = jwtService.getRoleFromClaims(claims);

        assertNotNull(authority);
        assertEquals("ROLE_FACULTY", authority.getAuthority());
    }

    @Test
    void getRoleFromClaims_WithUserRole_ReturnsCorrectAuthority() {
        user.setAccessLevel(AccessLevel.USER);
        String token = jwtService.generateToken(user);
        Claims claims = jwtService.parseAllClaims(token);

        GrantedAuthority authority = jwtService.getRoleFromClaims(claims);

        assertNotNull(authority);
        assertEquals("ROLE_USER", authority.getAuthority());
    }

    @Test
    void getRoleFromClaims_WhenRoleClaimMissing_ReturnsNull() {
        user.setAccessLevel(AccessLevel.USER);
        
        // Create a claims object without the role claim
        Claims claimsWithoutRole = Jwts.claims().subject(userId.toString()).build();

        GrantedAuthority authority = jwtService.getRoleFromClaims(claimsWithoutRole);

        assertNull(authority);
    }

    @Test
    void generateToken_CreatesValidSignature() {
        user.setAccessLevel(AccessLevel.USER);

        String token = jwtService.generateToken(user);

        // Verify we can parse it without exception
        assertDoesNotThrow(() -> jwtService.parseAllClaims(token));
    }

    /**
     * Helper method to parse token with the test secret key for verification
     */
    private Claims parseTokenWithSecret(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
