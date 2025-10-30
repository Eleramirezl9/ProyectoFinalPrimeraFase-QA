package com.ejemplo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la entidad Producto
 * Valida lógica de gestión de stock y métodos de negocio
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@DisplayName("Producto - Pruebas Unitarias")
class ProductoTest {

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Laptop");
        producto.setDescripcion("Laptop HP");
        producto.setPrecio(new BigDecimal("1500.00"));
        producto.setStock(10);
        producto.setCategoria("Electrónica");
        producto.setMarca("HP");
        producto.setActivo(true);
    }

    @Test
    @DisplayName("Debe verificar que tiene stock")
    void testTieneStock() {
        // Arrange
        producto.setStock(5);

        // Act & Assert
        assertTrue(producto.tieneStock());
    }

    @Test
    @DisplayName("Debe verificar que no tiene stock cuando es 0")
    void testNoTieneStockCero() {
        // Arrange
        producto.setStock(0);

        // Act & Assert
        assertFalse(producto.tieneStock());
    }

    @Test
    @DisplayName("Debe verificar que no tiene stock cuando es null")
    void testNoTieneStockNull() {
        // Arrange
        producto.setStock(null);

        // Act & Assert
        assertFalse(producto.tieneStock());
    }

    @Test
    @DisplayName("Debe verificar que tiene stock suficiente para cantidad")
    void testTieneStockSuficiente() {
        // Arrange
        producto.setStock(10);

        // Act & Assert
        assertTrue(producto.tieneStock(5));
    }

    @Test
    @DisplayName("Debe verificar que tiene stock exacto para cantidad")
    void testTieneStockExacto() {
        // Arrange
        producto.setStock(10);

        // Act & Assert
        assertTrue(producto.tieneStock(10));
    }

    @Test
    @DisplayName("Debe verificar que no tiene stock suficiente")
    void testNoTieneStockSuficiente() {
        // Arrange
        producto.setStock(5);

        // Act & Assert
        assertFalse(producto.tieneStock(10));
    }

    @Test
    @DisplayName("Debe reducir stock correctamente")
    void testReducirStock() {
        // Arrange
        producto.setStock(10);

        // Act
        producto.reducirStock(3);

        // Assert
        assertEquals(7, producto.getStock());
    }

    @Test
    @DisplayName("Debe reducir todo el stock")
    void testReducirTodoElStock() {
        // Arrange
        producto.setStock(5);

        // Act
        producto.reducirStock(5);

        // Assert
        assertEquals(0, producto.getStock());
    }

    @Test
    @DisplayName("Debe lanzar excepción al reducir stock insuficiente")
    void testReducirStockInsuficiente() {
        // Arrange
        producto.setStock(5);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> producto.reducirStock(10)
        );

        assertEquals("Stock insuficiente", exception.getMessage());
        assertEquals(5, producto.getStock()); // Stock no debe cambiar
    }

    @Test
    @DisplayName("Debe lanzar excepción al reducir con stock null")
    void testReducirStockNull() {
        // Arrange
        producto.setStock(null);

        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> producto.reducirStock(1)
        );
    }

    @Test
    @DisplayName("Debe aumentar stock correctamente")
    void testAumentarStock() {
        // Arrange
        producto.setStock(10);

        // Act
        producto.aumentarStock(5);

        // Assert
        assertEquals(15, producto.getStock());
    }

    @Test
    @DisplayName("Debe aumentar stock desde cero")
    void testAumentarStockDesdeCero() {
        // Arrange
        producto.setStock(0);

        // Act
        producto.aumentarStock(10);

        // Assert
        assertEquals(10, producto.getStock());
    }

    @Test
    @DisplayName("No debe aumentar stock con cantidad cero")
    void testNoAumentarStockConCero() {
        // Arrange
        producto.setStock(10);

        // Act
        producto.aumentarStock(0);

        // Assert
        assertEquals(10, producto.getStock());
    }

    @Test
    @DisplayName("No debe aumentar stock con cantidad negativa")
    void testNoAumentarStockConNegativo() {
        // Arrange
        producto.setStock(10);

        // Act
        producto.aumentarStock(-5);

        // Assert
        assertEquals(10, producto.getStock());
    }

    @Test
    @DisplayName("Debe generar toString correctamente")
    void testToString() {
        // Act
        String resultado = producto.toString();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.contains("id=1"));
        assertTrue(resultado.contains("nombre='Laptop'"));
        assertTrue(resultado.contains("precio=1500.00"));
        assertTrue(resultado.contains("stock=10"));
        assertTrue(resultado.contains("categoria='Electrónica'"));
        assertTrue(resultado.contains("marca='HP'"));
        assertTrue(resultado.contains("activo=true"));
    }

    @Test
    @DisplayName("Debe establecer y obtener todos los campos correctamente")
    void testGettersSetters() {
        // Arrange
        Producto nuevoProducto = new Producto();

        // Act
        nuevoProducto.setId(2L);
        nuevoProducto.setNombre("Mouse");
        nuevoProducto.setDescripcion("Mouse inalámbrico");
        nuevoProducto.setPrecio(new BigDecimal("25.50"));
        nuevoProducto.setStock(50);
        nuevoProducto.setCategoria("Accesorios");
        nuevoProducto.setMarca("Logitech");
        nuevoProducto.setActivo(true);

        // Assert
        assertEquals(2L, nuevoProducto.getId());
        assertEquals("Mouse", nuevoProducto.getNombre());
        assertEquals("Mouse inalámbrico", nuevoProducto.getDescripcion());
        assertEquals(new BigDecimal("25.50"), nuevoProducto.getPrecio());
        assertEquals(50, nuevoProducto.getStock());
        assertEquals("Accesorios", nuevoProducto.getCategoria());
        assertEquals("Logitech", nuevoProducto.getMarca());
        assertTrue(nuevoProducto.getActivo());
    }

    @Test
    @DisplayName("Debe manejar producto inactivo")
    void testProductoInactivo() {
        // Arrange & Act
        producto.setActivo(false);

        // Assert
        assertFalse(producto.getActivo());
    }

    @Test
    @DisplayName("Debe permitir stock negativo después de operaciones")
    void testStockNegativoNoPermitido() {
        // Arrange
        producto.setStock(5);

        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> producto.reducirStock(6)
        );
    }
}
