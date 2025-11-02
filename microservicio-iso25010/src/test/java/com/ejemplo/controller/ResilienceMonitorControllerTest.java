package com.ejemplo.controller;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración para ResilienceMonitorController
 * Utiliza MockMvc con contexto completo de Spring Boot
 * Spring Security configurado con usuarios mock
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("ResilienceMonitorController - Pruebas de Integración")
class ResilienceMonitorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @MockBean
    private RetryRegistry retryRegistry;

    private CircuitBreaker circuitBreaker;
    private Retry retry;

    @BeforeEach
    void setUp() {
        // Configurar Circuit Breaker mock
        CircuitBreakerConfig cbConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5)
                .waitDurationInOpenState(Duration.ofSeconds(60))
                .permittedNumberOfCallsInHalfOpenState(3)
                .build();

        circuitBreaker = CircuitBreaker.of("testCircuitBreaker", cbConfig);

        // Configurar Retry mock
        RetryConfig retryConfig = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(500))
                .build();

        retry = Retry.of("testRetry", retryConfig);
    }

    // ==================== Tests para GET /resilience/circuit-breakers ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /resilience/circuit-breakers - Debe retornar todos los circuit breakers")
    void testObtenerEstadoCircuitBreakers_Success() throws Exception {
        // Arrange
        when(circuitBreakerRegistry.getAllCircuitBreakers())
                .thenReturn(Set.of(circuitBreaker));

        // Act & Assert
        mockMvc.perform(get("/resilience/circuit-breakers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled", is(true)))
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.circuitBreakers", notNullValue()));

        verify(circuitBreakerRegistry, times(1)).getAllCircuitBreakers();
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("GET /resilience/circuit-breakers - Debe denegar acceso sin rol ADMIN")
    void testObtenerEstadoCircuitBreakers_Forbidden() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/resilience/circuit-breakers"))
                .andExpect(status().isForbidden());
    }

    // ==================== Tests para GET /resilience/circuit-breakers/{nombre} ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /resilience/circuit-breakers/{nombre} - Debe retornar circuit breaker específico")
    void testObtenerEstadoCircuitBreaker_Success() throws Exception {
        // Arrange
        when(circuitBreakerRegistry.find("testCircuitBreaker"))
                .thenReturn(Optional.of(circuitBreaker));

        // Act & Assert
        mockMvc.perform(get("/resilience/circuit-breakers/testCircuitBreaker"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("testCircuitBreaker")))
                .andExpect(jsonPath("$.estado", notNullValue()))
                .andExpect(jsonPath("$.config", notNullValue()))
                .andExpect(jsonPath("$.metricas", notNullValue()))
                .andExpect(jsonPath("$.timestamp", notNullValue()));

        verify(circuitBreakerRegistry, times(1)).find("testCircuitBreaker");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /resilience/circuit-breakers/{nombre} - Circuit breaker no encontrado")
    void testObtenerEstadoCircuitBreaker_NotFound() throws Exception {
        // Arrange
        when(circuitBreakerRegistry.find("nonExistent"))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/resilience/circuit-breakers/nonExistent"))
                .andExpect(status().isBadRequest());

        verify(circuitBreakerRegistry, times(1)).find("nonExistent");
    }

    // ==================== Tests para GET /resilience/retries ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /resilience/retries - Debe retornar todos los retries")
    void testObtenerEstadoRetries_Success() throws Exception {
        // Arrange
        when(retryRegistry.getAllRetries())
                .thenReturn(Set.of(retry));

        // Act & Assert
        mockMvc.perform(get("/resilience/retries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled", is(true)))
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.retries", notNullValue()));

        verify(retryRegistry, times(1)).getAllRetries();
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("GET /resilience/retries - Debe denegar acceso sin rol ADMIN")
    void testObtenerEstadoRetries_Forbidden() throws Exception {
        mockMvc.perform(get("/resilience/retries"))
                .andExpect(status().isForbidden());
    }

    // ==================== Tests para POST /resilience/circuit-breakers/{nombre}/close ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /resilience/circuit-breakers/{nombre}/close - Debe cerrar circuit breaker")
    void testCerrarCircuitBreaker_Success() throws Exception {
        // Arrange
        when(circuitBreakerRegistry.find("testCircuitBreaker"))
                .thenReturn(Optional.of(circuitBreaker));

        // Act & Assert
        mockMvc.perform(post("/resilience/circuit-breakers/testCircuitBreaker/close"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje", containsString("cerrado exitosamente")))
                .andExpect(jsonPath("$.nombre", is("testCircuitBreaker")))
                .andExpect(jsonPath("$.estadoActual", notNullValue()))
                .andExpect(jsonPath("$.timestamp", notNullValue()));

        verify(circuitBreakerRegistry, times(1)).find("testCircuitBreaker");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /resilience/circuit-breakers/{nombre}/close - Circuit breaker no encontrado")
    void testCerrarCircuitBreaker_NotFound() throws Exception {
        // Arrange
        when(circuitBreakerRegistry.find("nonExistent"))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/resilience/circuit-breakers/nonExistent/close"))
                .andExpect(status().isBadRequest());

        verify(circuitBreakerRegistry, times(1)).find("nonExistent");
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("POST /resilience/circuit-breakers/{nombre}/close - Debe denegar acceso sin rol ADMIN")
    void testCerrarCircuitBreaker_Forbidden() throws Exception {
        mockMvc.perform(post("/resilience/circuit-breakers/test/close"))
                .andExpect(status().isForbidden());
    }

    // ==================== Tests para POST /resilience/circuit-breakers/{nombre}/open ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /resilience/circuit-breakers/{nombre}/open - Debe abrir circuit breaker")
    void testAbrirCircuitBreaker_Success() throws Exception {
        // Arrange
        when(circuitBreakerRegistry.find("testCircuitBreaker"))
                .thenReturn(Optional.of(circuitBreaker));

        // Act & Assert
        mockMvc.perform(post("/resilience/circuit-breakers/testCircuitBreaker/open"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje", containsString("abierto exitosamente")))
                .andExpect(jsonPath("$.nombre", is("testCircuitBreaker")))
                .andExpect(jsonPath("$.estadoActual", notNullValue()))
                .andExpect(jsonPath("$.timestamp", notNullValue()));

        verify(circuitBreakerRegistry, times(1)).find("testCircuitBreaker");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /resilience/circuit-breakers/{nombre}/open - Circuit breaker no encontrado")
    void testAbrirCircuitBreaker_NotFound() throws Exception {
        // Arrange
        when(circuitBreakerRegistry.find("nonExistent"))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/resilience/circuit-breakers/nonExistent/open"))
                .andExpect(status().isBadRequest());

        verify(circuitBreakerRegistry, times(1)).find("nonExistent");
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("POST /resilience/circuit-breakers/{nombre}/open - Debe denegar acceso sin rol ADMIN")
    void testAbrirCircuitBreaker_Forbidden() throws Exception {
        mockMvc.perform(post("/resilience/circuit-breakers/test/open"))
                .andExpect(status().isForbidden());
    }

    // ==================== Tests para POST /resilience/circuit-breakers/{nombre}/reset ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /resilience/circuit-breakers/{nombre}/reset - Debe resetear circuit breaker")
    void testResetearCircuitBreaker_Success() throws Exception {
        // Arrange
        when(circuitBreakerRegistry.find("testCircuitBreaker"))
                .thenReturn(Optional.of(circuitBreaker));

        // Act & Assert
        mockMvc.perform(post("/resilience/circuit-breakers/testCircuitBreaker/reset"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje", containsString("reseteado exitosamente")))
                .andExpect(jsonPath("$.nombre", is("testCircuitBreaker")))
                .andExpect(jsonPath("$.estadoActual", notNullValue()))
                .andExpect(jsonPath("$.timestamp", notNullValue()));

        verify(circuitBreakerRegistry, times(1)).find("testCircuitBreaker");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /resilience/circuit-breakers/{nombre}/reset - Circuit breaker no encontrado")
    void testResetearCircuitBreaker_NotFound() throws Exception {
        // Arrange
        when(circuitBreakerRegistry.find("nonExistent"))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/resilience/circuit-breakers/nonExistent/reset"))
                .andExpect(status().isBadRequest());

        verify(circuitBreakerRegistry, times(1)).find("nonExistent");
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("POST /resilience/circuit-breakers/{nombre}/reset - Debe denegar acceso sin rol ADMIN")
    void testResetearCircuitBreaker_Forbidden() throws Exception {
        mockMvc.perform(post("/resilience/circuit-breakers/test/reset"))
                .andExpect(status().isForbidden());
    }
}
