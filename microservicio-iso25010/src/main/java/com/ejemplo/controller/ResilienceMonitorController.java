package com.ejemplo.controller;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para monitorear el estado de Circuit Breakers y mecanismos de resiliencia
 * Endpoints para verificar la salud y métricas de los patrones de resiliencia
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@RestController
@RequestMapping("/resilience")
@Tag(name = "Resiliencia", description = "Endpoints para monitoreo de Circuit Breakers y resiliencia")
public class ResilienceMonitorController {

    @Autowired(required = false)
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired(required = false)
    private RetryRegistry retryRegistry;

    /**
     * Obtiene el estado de todos los Circuit Breakers
     * @return Estado detallado de cada circuit breaker
     */
    @GetMapping("/circuit-breakers")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener estado de Circuit Breakers",
              description = "Retorna el estado y métricas de todos los circuit breakers configurados")
    public ResponseEntity<Map<String, Object>> obtenerEstadoCircuitBreakers() {
        Map<String, Object> response = new HashMap<>();

        if (circuitBreakerRegistry == null) {
            response.put("mensaje", "Circuit Breaker Registry no está disponible");
            response.put("enabled", false);
            return ResponseEntity.ok(response);
        }

        Map<String, Object> circuitBreakers = new HashMap<>();

        circuitBreakerRegistry.getAllCircuitBreakers().forEach(cb -> {
            Map<String, Object> cbInfo = new HashMap<>();
            cbInfo.put("nombre", cb.getName());
            cbInfo.put("estado", cb.getState().toString());
            cbInfo.put("config", obtenerConfigCircuitBreaker(cb));
            cbInfo.put("metricas", obtenerMetricasCircuitBreaker(cb));
            circuitBreakers.put(cb.getName(), cbInfo);
        });

        response.put("timestamp", Instant.now().toString());
        response.put("circuitBreakers", circuitBreakers);
        response.put("enabled", true);

        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene el estado de un Circuit Breaker específico
     * @param nombre Nombre del circuit breaker
     * @return Estado detallado del circuit breaker
     */
    @GetMapping("/circuit-breakers/{nombre}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener estado de un Circuit Breaker específico")
    public ResponseEntity<Map<String, Object>> obtenerEstadoCircuitBreaker(@PathVariable String nombre) {
        if (circuitBreakerRegistry == null) {
            return ResponseEntity.ok(Map.of(
                "mensaje", "Circuit Breaker Registry no está disponible",
                "enabled", false
            ));
        }

        CircuitBreaker cb = circuitBreakerRegistry.find(nombre)
                .orElseThrow(() -> new IllegalArgumentException("Circuit Breaker no encontrado: " + nombre));

        Map<String, Object> response = new HashMap<>();
        response.put("nombre", cb.getName());
        response.put("estado", cb.getState().toString());
        response.put("config", obtenerConfigCircuitBreaker(cb));
        response.put("metricas", obtenerMetricasCircuitBreaker(cb));
        response.put("timestamp", Instant.now().toString());

        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene el estado de todos los Retry configurados
     * @return Estado de los mecanismos de retry
     */
    @GetMapping("/retries")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener configuración de Retries")
    public ResponseEntity<Map<String, Object>> obtenerEstadoRetries() {
        Map<String, Object> response = new HashMap<>();

        if (retryRegistry == null) {
            response.put("mensaje", "Retry Registry no está disponible");
            response.put("enabled", false);
            return ResponseEntity.ok(response);
        }

        Map<String, Object> retries = new HashMap<>();

        retryRegistry.getAllRetries().forEach(retry -> {
            Map<String, Object> retryInfo = new HashMap<>();
            retryInfo.put("nombre", retry.getName());
            retryInfo.put("metricas", obtenerMetricasRetry(retry));
            retries.put(retry.getName(), retryInfo);
        });

        response.put("timestamp", Instant.now().toString());
        response.put("retries", retries);
        response.put("enabled", true);

        return ResponseEntity.ok(response);
    }

    /**
     * Transiciona manualmente un Circuit Breaker a estado CLOSED
     * @param nombre Nombre del circuit breaker
     * @return Confirmación de la transición
     */
    @PostMapping("/circuit-breakers/{nombre}/close")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cerrar manualmente un Circuit Breaker",
              description = "Fuerza la transición a estado CLOSED (útil para testing)")
    public ResponseEntity<Map<String, String>> cerrarCircuitBreaker(@PathVariable String nombre) {
        if (circuitBreakerRegistry == null) {
            return ResponseEntity.ok(Map.of(
                "mensaje", "Circuit Breaker Registry no está disponible"
            ));
        }

        CircuitBreaker cb = circuitBreakerRegistry.find(nombre)
                .orElseThrow(() -> new IllegalArgumentException("Circuit Breaker no encontrado: " + nombre));

        cb.transitionToClosedState();

        return ResponseEntity.ok(Map.of(
            "mensaje", "Circuit Breaker cerrado exitosamente",
            "nombre", nombre,
            "estadoActual", cb.getState().toString(),
            "timestamp", Instant.now().toString()
        ));
    }

    /**
     * Transiciona manualmente un Circuit Breaker a estado OPEN
     * @param nombre Nombre del circuit breaker
     * @return Confirmación de la transición
     */
    @PostMapping("/circuit-breakers/{nombre}/open")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Abrir manualmente un Circuit Breaker",
              description = "Fuerza la transición a estado OPEN (útil para testing)")
    public ResponseEntity<Map<String, String>> abrirCircuitBreaker(@PathVariable String nombre) {
        if (circuitBreakerRegistry == null) {
            return ResponseEntity.ok(Map.of(
                "mensaje", "Circuit Breaker Registry no está disponible"
            ));
        }

        CircuitBreaker cb = circuitBreakerRegistry.find(nombre)
                .orElseThrow(() -> new IllegalArgumentException("Circuit Breaker no encontrado: " + nombre));

        cb.transitionToOpenState();

        return ResponseEntity.ok(Map.of(
            "mensaje", "Circuit Breaker abierto exitosamente",
            "nombre", nombre,
            "estadoActual", cb.getState().toString(),
            "timestamp", Instant.now().toString()
        ));
    }

    /**
     * Reset de métricas de un Circuit Breaker
     * @param nombre Nombre del circuit breaker
     * @return Confirmación del reset
     */
    @PostMapping("/circuit-breakers/{nombre}/reset")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Resetear métricas de un Circuit Breaker")
    public ResponseEntity<Map<String, String>> resetearCircuitBreaker(@PathVariable String nombre) {
        if (circuitBreakerRegistry == null) {
            return ResponseEntity.ok(Map.of(
                "mensaje", "Circuit Breaker Registry no está disponible"
            ));
        }

        CircuitBreaker cb = circuitBreakerRegistry.find(nombre)
                .orElseThrow(() -> new IllegalArgumentException("Circuit Breaker no encontrado: " + nombre));

        cb.reset();

        return ResponseEntity.ok(Map.of(
            "mensaje", "Circuit Breaker reseteado exitosamente",
            "nombre", nombre,
            "estadoActual", cb.getState().toString(),
            "timestamp", Instant.now().toString()
        ));
    }

    // Métodos privados helper

    private Map<String, Object> obtenerConfigCircuitBreaker(CircuitBreaker cb) {
        Map<String, Object> config = new HashMap<>();
        var cbConfig = cb.getCircuitBreakerConfig();

        config.put("failureRateThreshold", cbConfig.getFailureRateThreshold());
        config.put("slidingWindowSize", cbConfig.getSlidingWindowSize());
        config.put("minimumNumberOfCalls", cbConfig.getMinimumNumberOfCalls());
        long waitSeconds = cbConfig.getWaitIntervalFunctionInOpenState().apply(1) / 1000;
        config.put("waitDurationInOpenState", waitSeconds + "s");
        config.put("permittedCallsInHalfOpenState", cbConfig.getPermittedNumberOfCallsInHalfOpenState());
        config.put("slidingWindowType", cbConfig.getSlidingWindowType().toString());

        return config;
    }

    private Map<String, Object> obtenerMetricasCircuitBreaker(CircuitBreaker cb) {
        Map<String, Object> metricas = new HashMap<>();
        var metrics = cb.getMetrics();

        metricas.put("numberOfSuccessfulCalls", metrics.getNumberOfSuccessfulCalls());
        metricas.put("numberOfFailedCalls", metrics.getNumberOfFailedCalls());
        metricas.put("numberOfNotPermittedCalls", metrics.getNumberOfNotPermittedCalls());
        metricas.put("failureRate", String.format("%.2f%%", metrics.getFailureRate()));
        metricas.put("numberOfBufferedCalls", metrics.getNumberOfBufferedCalls());

        return metricas;
    }

    private Map<String, Object> obtenerMetricasRetry(Retry retry) {
        Map<String, Object> metricas = new HashMap<>();
        var metrics = retry.getMetrics();

        metricas.put("numberOfSuccessfulCallsWithoutRetry", metrics.getNumberOfSuccessfulCallsWithoutRetryAttempt());
        metricas.put("numberOfSuccessfulCallsWithRetry", metrics.getNumberOfSuccessfulCallsWithRetryAttempt());
        metricas.put("numberOfFailedCallsWithRetry", metrics.getNumberOfFailedCallsWithRetryAttempt());
        metricas.put("numberOfFailedCallsWithoutRetry", metrics.getNumberOfFailedCallsWithoutRetryAttempt());

        return metricas;
    }
}
