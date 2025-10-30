package com.ejemplo.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para ErrorDTO
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@DisplayName("ErrorDTO - Pruebas Unitarias")
class ErrorDTOTest {

    @Test
    @DisplayName("Debe crear ErrorDTO con todos los campos")
    void testErrorDTOConstructor() {
        // Arrange & Act
        ErrorDTO errorDTO = new ErrorDTO(400, "Bad Request", "Error de validación",
                "/api/usuarios", Arrays.asList("Campo requerido", "Formato inválido"));

        // Assert
        assertNotNull(errorDTO);
        assertEquals(400, errorDTO.getStatus());
        assertEquals("Error de validación", errorDTO.getMessage());
        assertEquals("/api/usuarios", errorDTO.getPath());
        assertEquals(2, errorDTO.getDetails().size());
        assertNotNull(errorDTO.getTimestamp());
    }

    @Test
    @DisplayName("Debe crear ErrorDTO de validación")
    void testValidationError() {
        // Arrange & Act
        ErrorDTO error = ErrorDTO.validationError("Error de validación", "/api/test",
                Arrays.asList("Detalle 1", "Detalle 2"));

        // Assert
        assertEquals(400, error.getStatus());
        assertEquals("Error de validación", error.getMessage());
        assertEquals("/api/test", error.getPath());
        assertEquals(2, error.getDetails().size());
    }

    @Test
    @DisplayName("Debe crear ErrorDTO not found")
    void testNotFoundError() {
        // Arrange & Act
        ErrorDTO error = ErrorDTO.notFound("Recurso no encontrado", "/api/test");

        // Assert
        assertEquals(404, error.getStatus());
        assertEquals("Recurso no encontrado", error.getMessage());
        assertEquals("/api/test", error.getPath());
    }

    @Test
    @DisplayName("Debe crear ErrorDTO bad request")
    void testBadRequestError() {
        // Arrange & Act
        ErrorDTO error = ErrorDTO.badRequest("Solicitud incorrecta", "/api/test");

        // Assert
        assertEquals(400, error.getStatus());
        assertEquals("Solicitud incorrecta", error.getMessage());
        assertEquals("/api/test", error.getPath());
    }

    @Test
    @DisplayName("Debe crear ErrorDTO unauthorized")
    void testUnauthorizedError() {
        // Arrange & Act
        ErrorDTO error = ErrorDTO.unauthorized("No autorizado", "/api/test");

        // Assert
        assertEquals(401, error.getStatus());
        assertEquals("No autorizado", error.getMessage());
        assertEquals("/api/test", error.getPath());
    }

    @Test
    @DisplayName("Debe crear ErrorDTO forbidden")
    void testForbiddenError() {
        // Arrange & Act
        ErrorDTO error = ErrorDTO.forbidden("Acceso denegado", "/api/test");

        // Assert
        assertEquals(403, error.getStatus());
        assertEquals("Acceso denegado", error.getMessage());
        assertEquals("/api/test", error.getPath());
    }

    @Test
    @DisplayName("Debe crear ErrorDTO conflict")
    void testConflictError() {
        // Arrange & Act
        ErrorDTO error = ErrorDTO.conflict("Conflicto", "/api/test");

        // Assert
        assertEquals(409, error.getStatus());
        assertEquals("Conflicto", error.getMessage());
        assertEquals("/api/test", error.getPath());
    }

    @Test
    @DisplayName("Debe crear ErrorDTO method not allowed")
    void testMethodNotAllowedError() {
        // Arrange & Act
        ErrorDTO error = ErrorDTO.methodNotAllowed("Método no permitido", "/api/test");

        // Assert
        assertEquals(405, error.getStatus());
        assertEquals("Método no permitido", error.getMessage());
        assertEquals("/api/test", error.getPath());
    }

    @Test
    @DisplayName("Debe crear ErrorDTO internal server error")
    void testInternalServerError() {
        // Arrange & Act
        ErrorDTO error = ErrorDTO.internalServerError("Error del servidor", "/api/test");

        // Assert
        assertEquals(500, error.getStatus());
        assertEquals("Error del servidor", error.getMessage());
        assertEquals("/api/test", error.getPath());
    }

    @Test
    @DisplayName("Debe manejar detalles nulos")
    void testErrorDTOWithNullDetails() {
        // Arrange & Act
        ErrorDTO error = new ErrorDTO(400, "Bad Request", "Error", "/api/test");

        // Assert
        assertNotNull(error);
        assertNull(error.getDetails());
    }

    @Test
    @DisplayName("Debe poder modificar campos con setters")
    void testSetters() {
        // Arrange
        ErrorDTO error = new ErrorDTO(400, "Bad Request", "Error inicial", "/api/test");

        // Act
        error.setStatus(500);
        error.setMessage("Error actualizado");
        error.setPath("/api/nuevo");
        error.setDetails(Arrays.asList("Nuevo detalle"));

        // Assert
        assertEquals(500, error.getStatus());
        assertEquals("Error actualizado", error.getMessage());
        assertEquals("/api/nuevo", error.getPath());
        assertEquals(1, error.getDetails().size());
    }
}
