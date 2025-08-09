package com.ejemplo.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para manejo estandarizado de errores en la API
 * Proporciona información detallada sobre errores ocurridos
 * 
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
public class ErrorDTO {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<String> details;
    private String traceId;

    // Constructores
    public ErrorDTO() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorDTO(int status, String error, String message, String path) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public ErrorDTO(int status, String error, String message, String path, List<String> details) {
        this(status, error, message, path);
        this.details = details;
    }

    // Métodos estáticos para crear errores comunes
    public static ErrorDTO badRequest(String message, String path) {
        return new ErrorDTO(400, "Bad Request", message, path);
    }

    public static ErrorDTO badRequest(String message, String path, List<String> details) {
        return new ErrorDTO(400, "Bad Request", message, path, details);
    }

    public static ErrorDTO notFound(String message, String path) {
        return new ErrorDTO(404, "Not Found", message, path);
    }

    public static ErrorDTO conflict(String message, String path) {
        return new ErrorDTO(409, "Conflict", message, path);
    }

    public static ErrorDTO internalServerError(String message, String path) {
        return new ErrorDTO(500, "Internal Server Error", message, path);
    }

    public static ErrorDTO validationError(String message, String path, List<String> details) {
        return new ErrorDTO(400, "Validation Error", message, path, details);
    }

    public static ErrorDTO unauthorized(String message, String path) {
        return new ErrorDTO(401, "Unauthorized", message, path);
    }

    public static ErrorDTO forbidden(String message, String path) {
        return new ErrorDTO(403, "Forbidden", message, path);
    }

    public static ErrorDTO methodNotAllowed(String message, String path) {
        return new ErrorDTO(405, "Method Not Allowed", message, path);
    }

    public static ErrorDTO unsupportedMediaType(String message, String path) {
        return new ErrorDTO(415, "Unsupported Media Type", message, path);
    }

    // Getters y Setters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public String toString() {
        return "ErrorDTO{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", path='" + path + '\'' +
                ", details=" + details +
                ", traceId='" + traceId + '\'' +
                '}';
    }
}

