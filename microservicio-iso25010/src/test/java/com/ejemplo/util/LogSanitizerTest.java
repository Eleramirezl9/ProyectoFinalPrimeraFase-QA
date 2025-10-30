package com.ejemplo.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests unitarios para LogSanitizer
 * Verifica la sanitización correcta de entradas de usuario en logs
 */
@DisplayName("LogSanitizer Tests")
class LogSanitizerTest {

    @Test
    @DisplayName("Debe sanitizar saltos de línea")
    void testSanitizeNewlines() {
        String input = "texto\ncon\nsaltos";
        String result = LogSanitizer.sanitize(input);

        assertFalse(result.contains("\n"), "No debe contener saltos de línea");
        assertEquals("texto_con_saltos", result);
    }

    @Test
    @DisplayName("Debe sanitizar retornos de carro")
    void testSanitizeCarriageReturns() {
        String input = "texto\rcon\rretornos";
        String result = LogSanitizer.sanitize(input);

        assertFalse(result.contains("\r"), "No debe contener retornos de carro");
        assertEquals("texto_con_retornos", result);
    }

    @Test
    @DisplayName("Debe sanitizar tabulaciones")
    void testSanitizeTabs() {
        String input = "texto\tcon\ttabs";
        String result = LogSanitizer.sanitize(input);

        assertFalse(result.contains("\t"), "No debe contener tabulaciones");
        assertEquals("texto_con_tabs", result);
    }

    @Test
    @DisplayName("Debe sanitizar múltiples caracteres de control")
    void testSanitizeMultipleControlChars() {
        String input = "attack\r\ninjection\tattempt";
        String result = LogSanitizer.sanitize(input);

        assertFalse(result.contains("\r"), "No debe contener \\r");
        assertFalse(result.contains("\n"), "No debe contener \\n");
        assertFalse(result.contains("\t"), "No debe contener \\t");
        assertEquals("attack__injection_attempt", result);
    }

    @Test
    @DisplayName("Debe manejar entrada nula")
    void testSanitizeNullInput() {
        String result = LogSanitizer.sanitize((String) null);

        assertNotNull(result, "No debe retornar null");
        assertEquals("null", result);
    }

    @Test
    @DisplayName("Debe manejar cadena vacía")
    void testSanitizeEmptyString() {
        String result = LogSanitizer.sanitize("");

        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    @DisplayName("Debe preservar texto sin caracteres de control")
    void testSanitizeCleanText() {
        String input = "texto limpio sin caracteres de control";
        String result = LogSanitizer.sanitize(input);

        assertEquals(input, result, "Texto limpio debe permanecer igual");
    }

    @Test
    @DisplayName("Debe sanitizar objeto nulo")
    void testSanitizeNullObject() {
        String result = LogSanitizer.sanitize((Object) null);

        assertNotNull(result);
        assertEquals("null", result);
    }

    @Test
    @DisplayName("Debe sanitizar objeto y convertir a String")
    void testSanitizeObject() {
        Integer numero = 12345;
        String result = LogSanitizer.sanitize(numero);

        assertEquals("12345", result);
    }

    @Test
    @DisplayName("Debe sanitizar objeto con caracteres de control en toString")
    void testSanitizeObjectWithControlChars() {
        Object obj = new Object() {
            @Override
            public String toString() {
                return "objeto\ncon\rsaltos";
            }
        };

        String result = LogSanitizer.sanitize(obj);

        assertFalse(result.contains("\n"));
        assertFalse(result.contains("\r"));
        assertEquals("objeto_con_saltos", result);
    }

    @Test
    @DisplayName("Debe truncar texto largo correctamente")
    void testSanitizeAndTruncate() {
        String longText = "a".repeat(100);
        String result = LogSanitizer.sanitizeAndTruncate(longText, 50);

        assertNotNull(result);
        assertEquals(53, result.length()); // 50 + "..." (3 caracteres)
        assertTrue(result.endsWith("..."));
    }

    @Test
    @DisplayName("No debe truncar texto corto")
    void testSanitizeAndTruncateShortText() {
        String shortText = "texto corto";
        String result = LogSanitizer.sanitizeAndTruncate(shortText, 50);

        assertEquals(shortText, result);
        assertFalse(result.endsWith("..."));
    }

    @Test
    @DisplayName("Debe truncar en el límite exacto")
    void testSanitizeAndTruncateExactLimit() {
        String text = "a".repeat(50);
        String result = LogSanitizer.sanitizeAndTruncate(text, 50);

        assertEquals(text, result);
        assertFalse(result.endsWith("..."));
    }

    @Test
    @DisplayName("Debe sanitizar y truncar texto con caracteres de control")
    void testSanitizeAndTruncateWithControlChars() {
        String text = "texto\ncon\rsaltos\t".repeat(20);
        String result = LogSanitizer.sanitizeAndTruncate(text, 30);

        assertFalse(result.contains("\n"));
        assertFalse(result.contains("\r"));
        assertFalse(result.contains("\t"));
        assertEquals(33, result.length()); // 30 + "..."
        assertTrue(result.endsWith("..."));
    }

    @Test
    @DisplayName("Debe manejar intento de Log Injection")
    void testPreventLogInjection() {
        // Simulación de ataque de Log Injection
        String maliciousInput = "usuario@example.com\n[INFO] Admin login successful\n[INFO] ";
        String result = LogSanitizer.sanitize(maliciousInput);

        // Verificar que los saltos de línea fueron removidos
        assertFalse(result.contains("\n"), "Debe prevenir inyección de nuevas líneas de log");

        // Verificar que el texto malicioso fue neutralizado
        String[] lines = result.split("\n");
        assertEquals(1, lines.length, "Debe resultar en una sola línea");
    }

    @Test
    @DisplayName("Debe prevenir Log Forging con CRLF")
    void testPreventLogForging() {
        // Intento de Log Forging con CRLF
        String attack = "user input\r\n2025-10-30 FAKE ERROR: System compromised";
        String result = LogSanitizer.sanitize(attack);

        assertFalse(result.contains("\r\n"), "Debe prevenir CRLF injection");
        assertFalse(result.contains("\r"), "Debe prevenir CR");
        assertFalse(result.contains("\n"), "Debe prevenir LF");

        // El resultado debe ser una línea continua
        assertEquals("user input__2025-10-30 FAKE ERROR: System compromised", result);
    }

    @Test
    @DisplayName("Debe eliminar todos los caracteres de control Unicode")
    void testRemoveUnicodeControlChars() {
        // Caracteres de control Unicode (U+0000 a U+001F)
        String input = "texto\u0000con\u0001caracteres\u001Fde\u0007control";
        String result = LogSanitizer.sanitize(input);

        // Verificar que los caracteres de control fueron eliminados
        assertFalse(result.matches(".*\\p{Cntrl}.*"), "No debe contener caracteres de control");
    }

    @Test
    @DisplayName("Debe preservar caracteres especiales válidos")
    void testPreserveValidSpecialChars() {
        String input = "usuario@example.com con-guiones_y.puntos, espacios y números 123!";
        String result = LogSanitizer.sanitize(input);

        // Caracteres válidos deben preservarse (excepto los reemplazados por _)
        assertTrue(result.contains("@"));
        assertTrue(result.contains("-"));
        assertTrue(result.contains("_"));
        assertTrue(result.contains("."));
        assertTrue(result.contains(","));
        assertTrue(result.contains("!"));
        assertTrue(result.contains("123"));
    }

    @Test
    @DisplayName("Constructor privado no debe ser instanciable")
    void testPrivateConstructor() throws Exception {
        // Usar reflexión para intentar instanciar la clase
        var constructor = LogSanitizer.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        try {
            constructor.newInstance();
            fail("Constructor privado debería lanzar excepción");
        } catch (java.lang.reflect.InvocationTargetException e) {
            // La reflexión envuelve la excepción en InvocationTargetException
            assertTrue(e.getCause() instanceof IllegalStateException,
                "La causa debe ser IllegalStateException");
            assertEquals("Utility class", e.getCause().getMessage());
        }
    }
}
