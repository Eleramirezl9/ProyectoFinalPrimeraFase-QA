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

        // ⚠️ IMPORTANTE: Este generador es solo para desarrollo
        // NO incluir credenciales reales en el código fuente
        if (args.length == 0) {
            System.out.println("==============================================");
            System.out.println("GENERADOR DE PASSWORD BCRYPT");
            System.out.println("==============================================");
            System.out.println("Uso: java PasswordGenerator <password>");
            System.out.println("\nEjemplo:");
            System.out.println("  java PasswordGenerator miPasswordSeguro");
            System.out.println("\n⚠️  NO incluir credenciales reales en el código");
            System.out.println("==============================================");
            return;
        }

        String password = args[0];
        String hash = encoder.encode(password);

        System.out.println("==============================================");
        System.out.println("GENERADOR DE PASSWORD BCRYPT");
        System.out.println("==============================================");
        System.out.println("Hash BCrypt generado exitosamente");
        System.out.println("Hash: " + hash);
        System.out.println("==============================================");
        System.out.println("\nUsa este hash en tu data.sql");

        // Verificar que el hash funciona
        boolean matches = encoder.matches(password, hash);
        System.out.println("Verificación: " + (matches ? "✓ CORRECTO" : "✗ ERROR"));
    }
}
