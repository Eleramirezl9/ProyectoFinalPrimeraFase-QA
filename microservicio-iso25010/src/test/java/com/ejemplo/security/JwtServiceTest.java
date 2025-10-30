package com.ejemplo.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para JwtService
 * Valida generación, validación y extracción de información de tokens JWT
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("JwtService - Pruebas Unitarias")
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        userDetails = User.builder()
                .username("testuser@example.com")
                .password("password123")
                .authorities(Collections.emptyList())
                .build();
    }

    @Test
    @DisplayName("Debe generar un token JWT válido")
    void testGenerateToken_Success() {
        // Act
        String token = jwtService.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT tiene 3 partes: header.payload.signature
    }

    @Test
    @DisplayName("Debe generar un token con claims adicionales")
    void testGenerateTokenWithExtraClaims_Success() {
        // Arrange
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");
        extraClaims.put("userId", 123L);

        // Act
        String token = jwtService.generateToken(extraClaims, userDetails);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Debe extraer el username del token")
    void testExtractUsername_Success() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act
        String username = jwtService.extractUsername(token);

        // Assert
        assertEquals("testuser@example.com", username);
    }

    @Test
    @DisplayName("Debe validar un token correcto")
    void testIsTokenValid_ValidToken() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Debe rechazar un token con username incorrecto")
    void testIsTokenValid_InvalidUsername() {
        // Arrange
        String token = jwtService.generateToken(userDetails);
        UserDetails differentUser = User.builder()
                .username("different@example.com")
                .password("password123")
                .authorities(Collections.emptyList())
                .build();

        // Act
        boolean isValid = jwtService.isTokenValid(token, differentUser);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Debe generar un refresh token")
    void testGenerateRefreshToken_Success() {
        // Act
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // Assert
        assertNotNull(refreshToken);
        assertFalse(refreshToken.isEmpty());

        // Verificar que el username se puede extraer
        String username = jwtService.extractUsername(refreshToken);
        assertEquals("testuser@example.com", username);
    }

    @Test
    @DisplayName("Debe obtener el tiempo de expiración")
    void testGetExpirationTime_ReturnsPositiveValue() {
        // Act
        long expirationTime = jwtService.getExpirationTime();

        // Assert
        assertTrue(expirationTime > 0);
    }

    @Test
    @DisplayName("Debe extraer la fecha de expiración del token")
    void testExtractExpiration_Success() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act
        Date expiration = jwtService.extractClaim(token, io.jsonwebtoken.Claims::getExpiration);

        // Assert
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date())); // Debe ser una fecha futura
    }

    @Test
    @DisplayName("Debe extraer claims personalizados del token")
    void testExtractCustomClaim_Success() {
        // Arrange
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("customClaim", "customValue");
        String token = jwtService.generateToken(extraClaims, userDetails);

        // Act
        String customValue = jwtService.extractClaim(token,
            claims -> claims.get("customClaim", String.class));

        // Assert
        assertEquals("customValue", customValue);
    }

    @Test
    @DisplayName("El token debe contener el subject correcto")
    void testTokenContainsCorrectSubject() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act
        String subject = jwtService.extractClaim(token, io.jsonwebtoken.Claims::getSubject);

        // Assert
        assertEquals("testuser@example.com", subject);
    }

    @Test
    @DisplayName("Tokens generados con claims diferentes deben ser distintos")
    void testGenerateDifferentTokens() {
        // Arrange
        Map<String, Object> claims1 = new HashMap<>();
        claims1.put("claim1", "value1");
        Map<String, Object> claims2 = new HashMap<>();
        claims2.put("claim2", "value2");

        // Act
        String token1 = jwtService.generateToken(claims1, userDetails);
        String token2 = jwtService.generateToken(claims2, userDetails);

        // Assert
        assertNotEquals(token1, token2);
    }

    @Test
    @DisplayName("Token debe ser válido inmediatamente después de generarse")
    void testTokenValidImmediatelyAfterGeneration() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertTrue(isValid);
    }
}
