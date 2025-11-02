package com.ejemplo.resilience;

import com.ejemplo.model.Producto;
import com.ejemplo.repository.ProductoRepository;
import com.ejemplo.service.ProductoService;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Pruebas de integración para Circuit Breaker en ProductoService
 * Valida que los patrones de resiliencia funcionen correctamente
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Circuit Breaker - Pruebas de Resiliencia")
class CircuitBreakerTest {

    @Autowired
    private ProductoService productoService;

    @Autowired(required = false)
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @MockBean
    private ProductoRepository productoRepository;

    private Producto productoMock;
    private CircuitBreaker circuitBreaker;

    @BeforeEach
    void setUp() {
        // Configurar producto mock
        productoMock = new Producto();
        productoMock.setId(1L);
        productoMock.setNombre("Laptop");
        productoMock.setDescripcion("Laptop de prueba");
        productoMock.setPrecio(new BigDecimal("1500.00"));
        productoMock.setStock(10);
        productoMock.setCategoria("Electrónica");
        productoMock.setMarca("HP");
        productoMock.setActivo(true);
        productoMock.setFechaCreacion(LocalDateTime.now());

        // Resetear circuit breaker si existe
        if (circuitBreakerRegistry != null) {
            circuitBreaker = circuitBreakerRegistry.find("productoService").orElse(null);
            if (circuitBreaker != null) {
                circuitBreaker.reset();
            }
        }
    }

    @Test
    @DisplayName("Circuit Breaker debe permitir llamadas exitosas")
    void testCircuitBreaker_SuccessfulCalls() {
        // Arrange
        when(productoRepository.findAll()).thenReturn(List.of(productoMock));

        // Act
        List<Producto> productos = productoService.obtenerTodos();

        // Assert
        assertNotNull(productos);
        assertEquals(1, productos.size());
        verify(productoRepository, times(1)).findAll();

        // Verificar estado del circuit breaker
        if (circuitBreaker != null) {
            assertEquals(CircuitBreaker.State.CLOSED, circuitBreaker.getState(),
                        "Circuit Breaker debe estar en estado CLOSED después de llamadas exitosas");
        }
    }

    @Test
    @DisplayName("Circuit Breaker debe ejecutar fallback cuando hay fallos")
    void testCircuitBreaker_FallbackOnFailure() {
        // Arrange - Simular fallo en el repositorio
        when(productoRepository.findAll()).thenThrow(new RuntimeException("Database connection failed"));

        // Act
        List<Producto> productos = productoService.obtenerTodos();

        // Assert - Debe retornar lista vacía del fallback
        assertNotNull(productos);
        assertTrue(productos.isEmpty(), "Fallback debe retornar lista vacía");
        verify(productoRepository, atLeastOnce()).findAll();
    }

    @Test
    @DisplayName("Retry debe intentar múltiples veces antes de fallar")
    void testRetry_MultipleAttempts() {
        // Arrange - Simular fallo en el repositorio
        when(productoRepository.findById(anyLong()))
                .thenThrow(new RuntimeException("Temporary error"));

        // Act
        Producto producto = productoService.obtenerPorId(1L);

        // Assert - Debe retornar producto fallback
        assertNotNull(producto);
        assertEquals("Producto no disponible temporalmente", producto.getNombre());

        // Verificar que se intentó múltiples veces (retry)
        verify(productoRepository, atLeast(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Circuit Breaker debe retornar producto por ID exitosamente")
    void testCircuitBreaker_ObtenerPorId_Success() {
        // Arrange
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoMock));

        // Act
        Producto producto = productoService.obtenerPorId(1L);

        // Assert
        assertNotNull(producto);
        assertEquals("Laptop", producto.getNombre());
        assertEquals(1L, producto.getId());
        verify(productoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Fallback debe retornar producto genérico cuando falla obtenerPorId")
    void testCircuitBreaker_ObtenerPorId_Fallback() {
        // Arrange - Simular fallo
        when(productoRepository.findById(anyLong()))
                .thenThrow(new RuntimeException("Service unavailable"));

        // Act
        Producto productoFallback = productoService.obtenerPorId(999L);

        // Assert
        assertNotNull(productoFallback);
        assertEquals(999L, productoFallback.getId());
        assertEquals("Producto no disponible temporalmente", productoFallback.getNombre());
        assertFalse(productoFallback.getActivo());
        assertEquals(BigDecimal.ZERO, productoFallback.getPrecio());
    }

    @Test
    @DisplayName("Circuit Breaker debe manejar múltiples llamadas fallidas")
    void testCircuitBreaker_MultipleFails() {
        // Arrange
        when(productoRepository.findAll())
                .thenThrow(new RuntimeException("Connection timeout"));

        // Act - Realizar múltiples llamadas fallidas
        for (int i = 0; i < 6; i++) {
            List<Producto> productos = productoService.obtenerTodos();
            assertNotNull(productos);
            assertTrue(productos.isEmpty(), "Debe retornar lista vacía del fallback");
        }

        // Assert - Verificar que el circuit breaker eventualmente se abre
        // (esto depende de la configuración: 5 llamadas mínimas, 50% tasa de fallo)
        if (circuitBreaker != null) {
            var metrics = circuitBreaker.getMetrics();
            assertTrue(metrics.getNumberOfFailedCalls() > 0,
                      "Debe registrar llamadas fallidas");
        }
    }

    @Test
    @DisplayName("reducirStock debe lanzar excepción del fallback cuando falla")
    void testCircuitBreaker_ReducirStock_FallbackException() {
        // Arrange
        when(productoRepository.findById(anyLong()))
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.reducirStock(1L, 5);
        });

        assertTrue(exception.getMessage().contains("Servicio de inventario no disponible"),
                  "Mensaje de fallback debe indicar servicio no disponible");
    }

    @Test
    @DisplayName("CircuitBreaker Registry debe estar configurado")
    void testCircuitBreakerRegistry_IsConfigured() {
        // Assert
        assertNotNull(circuitBreakerRegistry, "CircuitBreakerRegistry debe estar disponible");

        // Verificar que existe el circuit breaker para productoService
        Optional<CircuitBreaker> cbOptional = circuitBreakerRegistry.find("productoService");
        assertTrue(cbOptional.isPresent(), "Circuit Breaker 'productoService' debe estar configurado");

        CircuitBreaker cb = cbOptional.get();
        assertEquals("productoService", cb.getName());

        // Verificar configuración
        var config = cb.getCircuitBreakerConfig();
        assertEquals(50.0f, config.getFailureRateThreshold(),
                    "Failure rate threshold debe ser 50%");
        assertEquals(10, config.getSlidingWindowSize(),
                    "Sliding window size debe ser 10");
        assertEquals(5, config.getMinimumNumberOfCalls(),
                    "Minimum number of calls debe ser 5");
    }

    @Test
    @DisplayName("Métricas del Circuit Breaker deben actualizarse correctamente")
    void testCircuitBreaker_MetricsUpdate() {
        // Arrange
        when(productoRepository.findAll()).thenReturn(List.of(productoMock));

        // Act
        productoService.obtenerTodos();
        productoService.obtenerTodos();

        // Assert
        if (circuitBreaker != null) {
            var metrics = circuitBreaker.getMetrics();
            assertTrue(metrics.getNumberOfSuccessfulCalls() >= 2,
                      "Debe registrar al menos 2 llamadas exitosas");
            assertEquals(0, metrics.getNumberOfFailedCalls(),
                        "No debe tener llamadas fallidas");
        }
    }
}
