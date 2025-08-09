package com.ejemplo.exception;

import com.ejemplo.dto.ErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones para la aplicación
 * Centraliza el manejo de errores y proporciona respuestas consistentes
 * 
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Maneja errores de validación de argumentos de métodos
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        logger.warn("Error de validación: {}", ex.getMessage());
        
        List<String> details = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.add(error.getField() + ": " + error.getDefaultMessage());
        }
        
        ErrorDTO errorDTO = ErrorDTO.validationError(
            "Error de validación en los datos enviados",
            request.getDescription(false).replace("uri=", ""),
            details
        );
        
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja violaciones de restricciones de validación
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDTO> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {
        
        logger.warn("Error de restricción: {}", ex.getMessage());
        
        List<String> details = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        
        ErrorDTO errorDTO = ErrorDTO.validationError(
            "Error de validación en las restricciones",
            request.getDescription(false).replace("uri=", ""),
            details
        );
        
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja entidades no encontradas
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleEntityNotFoundException(
            EntityNotFoundException ex, WebRequest request) {
        
        logger.warn("Entidad no encontrada: {}", ex.getMessage());
        
        ErrorDTO errorDTO = ErrorDTO.notFound(
            ex.getMessage() != null ? ex.getMessage() : "Recurso no encontrado",
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja errores de tipo de argumento incorrecto
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDTO> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        
        logger.warn("Error de tipo de argumento: {}", ex.getMessage());
        
        String message = String.format("El parámetro '%s' debe ser de tipo %s", 
            ex.getName(), 
            ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
        
        ErrorDTO errorDTO = ErrorDTO.badRequest(
            message,
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja errores de lectura de mensaje HTTP
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDTO> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, WebRequest request) {
        
        logger.warn("Error de lectura de mensaje HTTP: {}", ex.getMessage());
        
        ErrorDTO errorDTO = ErrorDTO.badRequest(
            "Error en el formato del JSON enviado. Verifique la sintaxis y los tipos de datos.",
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja métodos HTTP no soportados
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorDTO> handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, WebRequest request) {
        
        logger.warn("Método HTTP no soportado: {}", ex.getMessage());
        
        String message = String.format("El método %s no está soportado para esta URL. Métodos soportados: %s",
            ex.getMethod(),
            String.join(", ", ex.getSupportedMethods() != null ? ex.getSupportedMethods() : new String[]{}));
        
        ErrorDTO errorDTO = ErrorDTO.methodNotAllowed(
            message,
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorDTO, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Maneja argumentos ilegales
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDTO> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        
        logger.warn("Argumento ilegal: {}", ex.getMessage());
        
        ErrorDTO errorDTO = ErrorDTO.badRequest(
            ex.getMessage() != null ? ex.getMessage() : "Argumento inválido",
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja estados ilegales
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorDTO> handleIllegalStateException(
            IllegalStateException ex, WebRequest request) {
        
        logger.warn("Estado ilegal: {}", ex.getMessage());
        
        ErrorDTO errorDTO = ErrorDTO.conflict(
            ex.getMessage() != null ? ex.getMessage() : "Estado inválido para la operación",
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorDTO, HttpStatus.CONFLICT);
    }

    /**
     * Maneja excepciones de runtime genéricas
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDTO> handleRuntimeException(
            RuntimeException ex, WebRequest request) {
        
        logger.error("Error de runtime: {}", ex.getMessage(), ex);
        
        ErrorDTO errorDTO = ErrorDTO.internalServerError(
            "Error interno del servidor. Por favor, contacte al administrador.",
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Maneja todas las demás excepciones no capturadas
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGenericException(
            Exception ex, WebRequest request) {
        
        logger.error("Error no manejado: {}", ex.getMessage(), ex);
        
        ErrorDTO errorDTO = ErrorDTO.internalServerError(
            "Error interno del servidor. Por favor, contacte al administrador.",
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

