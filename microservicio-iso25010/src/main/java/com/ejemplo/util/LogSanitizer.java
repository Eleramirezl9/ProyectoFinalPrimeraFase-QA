package com.ejemplo.util;

/**
 * Utilidad para sanitizar datos antes de registrarlos en logs.
 * Previene ataques de Log Injection eliminando caracteres de control
 * como saltos de línea y retornos de carro.
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
public class LogSanitizer {

    private LogSanitizer() {
        // Constructor privado para evitar instanciación
        throw new IllegalStateException("Utility class");
    }

    /**
     * Sanitiza una cadena de texto eliminando caracteres de control
     * que podrían ser usados para Log Injection.
     *
     * Elimina:
     * - Saltos de línea (\n)
     * - Retornos de carro (\r)
     * - Tabulaciones (\t)
     * - Otros caracteres de control
     *
     * @param input Cadena de texto a sanitizar
     * @return Cadena sanitizada, o "null" si el input es nulo
     */
    public static String sanitize(String input) {
        if (input == null) {
            return "null";
        }

        // Eliminar caracteres de control peligrosos para logs
        return input.replaceAll("[\r\n\t]", "_")
                   .replaceAll("[\\p{Cntrl}]", ""); // Elimina todos los caracteres de control
    }

    /**
     * Sanitiza un objeto convirtiéndolo a String y aplicando sanitización.
     *
     * @param input Objeto a sanitizar
     * @return Cadena sanitizada
     */
    public static String sanitize(Object input) {
        if (input == null) {
            return "null";
        }
        return sanitize(input.toString());
    }

    /**
     * Trunca y sanitiza una cadena si excede el límite especificado.
     * Útil para evitar logs excesivamente largos.
     *
     * @param input Cadena a sanitizar
     * @param maxLength Longitud máxima permitida
     * @return Cadena sanitizada y truncada si es necesario
     */
    public static String sanitizeAndTruncate(String input, int maxLength) {
        String sanitized = sanitize(input);
        if (sanitized.length() > maxLength) {
            return sanitized.substring(0, maxLength) + "...";
        }
        return sanitized;
    }
}
