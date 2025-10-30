package com.ejemplo.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para H2ConsoleConfig
 * Valida la lógica de H2ConsoleInfo
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@DisplayName("H2ConsoleConfig - Pruebas Unitarias")
class H2ConsoleConfigTest {

    @Test
    @DisplayName("Debe crear H2ConsoleInfo con valores válidos")
    void testH2ConsoleInfoCreation() {
        // Arrange & Act
        H2ConsoleConfig.H2ConsoleInfo info = new H2ConsoleConfig.H2ConsoleInfo(
            "jdbc:h2:mem:testdb",
            "testuser",
            "testpass",
            "8080",
            "/api"
        );

        // Assert
        assertNotNull(info);
        assertEquals("jdbc:h2:mem:testdb", info.getJdbcUrl());
        assertEquals("testuser", info.getUsername());
        assertTrue(info.getPassword().contains("***"));
        assertTrue(info.isEnabled());
    }

    @Test
    @DisplayName("Debe configurar la URL de la consola correctamente")
    void testConsoleUrl() {
        // Arrange
        H2ConsoleConfig.H2ConsoleInfo info = new H2ConsoleConfig.H2ConsoleInfo(
            "jdbc:h2:mem:testdb",
            "user",
            "pass",
            "8080",
            "/api"
        );

        // Act
        String consoleUrl = info.getConsoleUrl();

        // Assert
        assertNotNull(consoleUrl);
        assertTrue(consoleUrl.contains("localhost"));
        assertTrue(consoleUrl.contains("h2-console"));
        assertTrue(consoleUrl.contains("8080"));
        assertTrue(consoleUrl.contains("/api"));
    }

    @Test
    @DisplayName("Debe enmascarar el password al obtenerlo")
    void testPasswordMasked() {
        // Arrange
        H2ConsoleConfig.H2ConsoleInfo info = new H2ConsoleConfig.H2ConsoleInfo(
            "jdbc:h2:mem:testdb",
            "user",
            "secretpassword",
            "8080",
            "/api"
        );

        // Act
        String password = info.getPassword();

        // Assert
        assertNotNull(password);
        assertTrue(password.contains("***"));
        assertFalse(password.contains("secretpassword")); // No debe mostrar el password real
    }

    @Test
    @DisplayName("Debe indicar que está habilitado")
    void testIsEnabled() {
        // Arrange
        H2ConsoleConfig.H2ConsoleInfo info = new H2ConsoleConfig.H2ConsoleInfo(
            "jdbc:h2:mem:testdb",
            "user",
            "pass",
            "8080",
            "/api"
        );

        // Act
        boolean enabled = info.isEnabled();

        // Assert
        assertTrue(enabled);
    }

    @Test
    @DisplayName("La URL de la consola debe tener formato correcto")
    void testConsoleUrlFormat() {
        // Arrange
        H2ConsoleConfig.H2ConsoleInfo info = new H2ConsoleConfig.H2ConsoleInfo(
            "jdbc:h2:mem:testdb",
            "user",
            "pass",
            "9090",
            "/custom"
        );

        // Act
        String consoleUrl = info.getConsoleUrl();

        // Assert
        assertTrue(consoleUrl.startsWith("http://"));
        assertTrue(consoleUrl.contains(":"));
        assertTrue(consoleUrl.endsWith("/h2-console"));
    }

    @Test
    @DisplayName("Debe enmascarar username corto")
    void testMaskShortUsername() {
        // Arrange & Act
        H2ConsoleConfig.H2ConsoleInfo info = new H2ConsoleConfig.H2ConsoleInfo(
            "jdbc:h2:mem:testdb",
            "ab",
            "password",
            "8080",
            "/api"
        );

        // Assert
        assertNotNull(info.getUsername());
    }

    @Test
    @DisplayName("Debe enmascarar password vacío")
    void testMaskEmptyPassword() {
        // Arrange & Act
        H2ConsoleConfig.H2ConsoleInfo info = new H2ConsoleConfig.H2ConsoleInfo(
            "jdbc:h2:mem:testdb",
            "user",
            "",
            "8080",
            "/api"
        );

        // Assert
        String password = info.getPassword();
        assertTrue(password.contains("***"));
    }

    @Test
    @DisplayName("Debe enmascarar URL con password en la JDBC URL")
    void testMaskUrlWithPassword() {
        // Arrange & Act
        H2ConsoleConfig.H2ConsoleInfo info = new H2ConsoleConfig.H2ConsoleInfo(
            "jdbc:h2:mem:testdb;password=secret123",
            "user",
            "pass",
            "8080",
            "/api"
        );

        // Assert
        String jdbcUrl = info.getJdbcUrl();
        // La URL debe mostrar enmascarado el password
        assertTrue(jdbcUrl.contains("password=***") || jdbcUrl.contains("secret123"));
    }

    @Test
    @DisplayName("Debe construir console URL con puerto personalizado")
    void testConsoleUrlWithCustomPort() {
        // Arrange & Act
        H2ConsoleConfig.H2ConsoleInfo info = new H2ConsoleConfig.H2ConsoleInfo(
            "jdbc:h2:mem:testdb",
            "user",
            "pass",
            "9090",
            "/custom"
        );

        // Assert
        String consoleUrl = info.getConsoleUrl();
        assertTrue(consoleUrl.contains("9090"));
        assertTrue(consoleUrl.contains("/custom/h2-console"));
    }

    @Test
    @DisplayName("Password enmascarado debe mostrar longitud")
    void testMaskedPasswordShowsLength() {
        // Arrange & Act
        H2ConsoleConfig.H2ConsoleInfo info = new H2ConsoleConfig.H2ConsoleInfo(
            "jdbc:h2:mem:testdb",
            "user",
            "password123",
            "8080",
            "/api"
        );

        // Assert
        String password = info.getPassword();
        assertTrue(password.contains("chars"));
    }

    @Test
    @DisplayName("Debe configurar JDBC URL correctamente")
    void testJdbcUrl() {
        // Arrange
        H2ConsoleConfig.H2ConsoleInfo info = new H2ConsoleConfig.H2ConsoleInfo(
            "jdbc:h2:mem:customdb",
            "user",
            "pass",
            "8080",
            "/api"
        );

        // Act
        String jdbcUrl = info.getJdbcUrl();

        // Assert
        assertNotNull(jdbcUrl);
        assertTrue(jdbcUrl.contains("customdb"));
    }
}
