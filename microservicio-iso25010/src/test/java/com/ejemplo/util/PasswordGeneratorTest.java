package com.ejemplo.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para PasswordGenerator
 * Valida la generación de hashes BCrypt
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@DisplayName("PasswordGenerator - Pruebas Unitarias")
class PasswordGeneratorTest {

    @Test
    @DisplayName("Debe ejecutar el método main sin errores cuando se pasa un password")
    void testMainMethod() {
        // Arrange
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            // Act - Pasar un password como argumento
            PasswordGenerator.main(new String[]{"testPassword123"});

            // Assert
            String output = outContent.toString();
            assertNotNull(output);
            assertTrue(output.contains("GENERADOR DE PASSWORD BCRYPT"));
            assertTrue(output.contains("Hash BCrypt generado exitosamente"));
            assertTrue(output.contains("Hash:"));
            assertTrue(output.contains("Verificación:"));
            assertTrue(output.contains("CORRECTO") || output.contains("✓"));
        } finally {
            // Restaurar System.out
            System.setOut(originalOut);
        }
    }

    @Test
    @DisplayName("Debe mostrar mensaje de uso cuando no se pasan argumentos")
    void testMainMethodWithoutArguments() {
        // Arrange
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            // Act - No pasar argumentos
            PasswordGenerator.main(new String[]{});

            // Assert
            String output = outContent.toString();
            assertNotNull(output);
            assertTrue(output.contains("GENERADOR DE PASSWORD BCRYPT"));
            assertTrue(output.contains("Uso: java PasswordGenerator <password>"));
            assertTrue(output.contains("NO incluir credenciales reales en el código"));
        } finally {
            // Restaurar System.out
            System.setOut(originalOut);
        }
    }

    @Test
    @DisplayName("Debe generar hash BCrypt válido")
    void testBCryptHashGeneration() {
        // Arrange
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "password123";

        // Act
        String hash = encoder.encode(password);

        // Assert
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
        assertTrue(hash.startsWith("$2a$") || hash.startsWith("$2b$"));
        assertTrue(hash.length() >= 60); // BCrypt hash tiene al menos 60 caracteres
    }

    @Test
    @DisplayName("Debe verificar que el hash coincide con el password")
    void testBCryptHashMatches() {
        // Arrange
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "password123";
        String hash = encoder.encode(password);

        // Act
        boolean matches = encoder.matches(password, hash);

        // Assert
        assertTrue(matches);
    }

    @Test
    @DisplayName("Debe rechazar password incorrecto")
    void testBCryptHashDoesNotMatch() {
        // Arrange
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "password123";
        String wrongPassword = "wrongpassword";
        String hash = encoder.encode(password);

        // Act
        boolean matches = encoder.matches(wrongPassword, hash);

        // Assert
        assertFalse(matches);
    }

    @Test
    @DisplayName("Hashes diferentes para el mismo password")
    void testDifferentHashesForSamePassword() {
        // Arrange
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "password123";

        // Act
        String hash1 = encoder.encode(password);
        String hash2 = encoder.encode(password);

        // Assert
        assertNotEquals(hash1, hash2); // BCrypt usa salt aleatorio
        assertTrue(encoder.matches(password, hash1));
        assertTrue(encoder.matches(password, hash2));
    }

    @Test
    @DisplayName("Debe generar hash para password vacío")
    void testBCryptWithEmptyPassword() {
        // Arrange
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "";

        // Act
        String hash = encoder.encode(password);

        // Assert
        assertNotNull(hash);
        assertTrue(encoder.matches(password, hash));
    }

    @Test
    @DisplayName("Debe generar hash para password con caracteres especiales")
    void testBCryptWithSpecialCharacters() {
        // Arrange
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "P@ssw0rd!#$%";

        // Act
        String hash = encoder.encode(password);

        // Assert
        assertNotNull(hash);
        assertTrue(encoder.matches(password, hash));
    }

    @Test
    @DisplayName("Debe generar hash para password con espacios")
    void testBCryptWithSpaces() {
        // Arrange
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "pass word 123";

        // Act
        String hash = encoder.encode(password);

        // Assert
        assertNotNull(hash);
        assertTrue(encoder.matches(password, hash));
    }

    @Test
    @DisplayName("Debe generar hash para password largo")
    void testBCryptWithLongPassword() {
        // Arrange
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "a".repeat(100);

        // Act
        String hash = encoder.encode(password);

        // Assert
        assertNotNull(hash);
        assertTrue(encoder.matches(password, hash));
    }

    @Test
    @DisplayName("Hash debe contener componentes BCrypt válidos")
    void testBCryptHashStructure() {
        // Arrange
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "password123";

        // Act
        String hash = encoder.encode(password);
        String[] parts = hash.split("\\$");

        // Assert
        assertTrue(parts.length >= 4); // BCrypt: $2a$rounds$salt$hash
        assertTrue(parts[1].equals("2a") || parts[1].equals("2b")); // Versión BCrypt
        assertNotNull(parts[2]); // Rounds (cost factor)
        assertFalse(parts[3].isEmpty()); // Salt + hash
    }
}
