package com.ejemplo.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utilidad temporal para generar passwords BCrypt
 * Ejecutar este main para obtener el hash correcto
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
public class PasswordGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String password = "password123";
        String hash = encoder.encode(password);

        System.out.println("==============================================");
        System.out.println("GENERADOR DE PASSWORD BCRYPT");
        System.out.println("==============================================");
        System.out.println("Password original: " + password);
        System.out.println("Hash BCrypt:       " + hash);
        System.out.println("==============================================");
        System.out.println("\nUsa este hash en tu data.sql");

        // Verificar que el hash funciona
        boolean matches = encoder.matches(password, hash);
        System.out.println("Verificación: " + (matches ? "✓ CORRECTO" : "✗ ERROR"));
    }
}
