package com.ejemplo.service;

import com.ejemplo.model.Producto;
import com.ejemplo.repository.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para ProductoService
 * Valida la lógica de negocio para operaciones CRUD de productos
 * Cobertura objetivo: 80%+
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductoService - Pruebas Unitarias")
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto productoMock;

    @BeforeEach
    void setUp() {
        productoMock = new Producto();
        productoMock.setId(1L);
        productoMock.setNombre("Laptop HP");
        productoMock.setDescripcion("Laptop HP 15 pulgadas");
        productoMock.setPrecio(new BigDecimal("799.99"));
        productoMock.setStock(10);
        productoMock.setCategoria("Electrónicos");
        productoMock.setMarca("HP");
        productoMock.setActivo(true);
    }

    // ==================== Tests para obtenerTodos ====================
    @Test
    @DisplayName("obtenerTodos - Debe retornar lista de todos los productos")
    void testObtenerTodos_Success() {
        // Arrange
        List<Producto> productos = Arrays.asList(productoMock, new Producto());
        when(productoRepository.findAll()).thenReturn(productos);

        // Act
        List<Producto> resultado = productoService.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodos - Debe retornar lista vacía cuando no hay productos")
    void testObtenerTodos_EmptyList() {
        // Arrange
        when(productoRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Producto> resultado = productoService.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(productoRepository, times(1)).findAll();
    }

    // ==================== Tests para obtenerPorId ====================
    @Test
    @DisplayName("obtenerPorId - Debe retornar producto cuando existe")
    void testObtenerPorId_Success() {
        // Arrange
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoMock));

        // Act
        Producto resultado = productoService.obtenerPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("Laptop HP", resultado.getNombre());
        assertEquals(new BigDecimal("799.99"), resultado.getPrecio());
        verify(productoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorId - Debe lanzar excepción cuando producto no existe")
    void testObtenerPorId_NotFound() {
        // Arrange
        when(productoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> productoService.obtenerPorId(999L));
        verify(productoRepository, times(1)).findById(999L);
    }

    // ==================== Tests para crear ====================
    @Test
    @DisplayName("crear - Debe crear producto exitosamente")
    void testCrear_Success() {
        // Arrange
        when(productoRepository.save(any(Producto.class))).thenReturn(productoMock);

        // Act
        Producto resultado = productoService.crear(productoMock);

        // Assert
        assertNotNull(resultado);
        assertEquals("Laptop HP", resultado.getNombre());
        assertTrue(resultado.getActivo());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    @DisplayName("crear - Debe lanzar excepción cuando precio es negativo")
    void testCrear_PrecioNegativo() {
        // Arrange
        productoMock.setPrecio(new BigDecimal("-100"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> productoService.crear(productoMock));
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    @DisplayName("crear - Debe lanzar excepción cuando stock es negativo")
    void testCrear_StockNegativo() {
        // Arrange
        productoMock.setStock(-5);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> productoService.crear(productoMock));
        verify(productoRepository, never()).save(any(Producto.class));
    }

    // ==================== Tests para actualizar ====================
    @Test
    @DisplayName("actualizar - Debe actualizar producto exitosamente")
    void testActualizar_Success() {
        // Arrange
        Producto productoActualizado = new Producto();
        productoActualizado.setNombre("Laptop HP Pro");
        productoActualizado.setDescripcion("Laptop HP Pro 17 pulgadas");
        productoActualizado.setPrecio(new BigDecimal("999.99"));
        productoActualizado.setStock(15);
        productoActualizado.setCategoria("Electrónicos");
        productoActualizado.setMarca("HP");

        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoMock));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoMock);

        // Act
        Producto resultado = productoService.actualizar(1L, productoActualizado);

        // Assert
        assertNotNull(resultado);
        verify(productoRepository, times(1)).findById(1L);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    @DisplayName("actualizar - Debe lanzar excepción cuando producto no existe")
    void testActualizar_NotFound() {
        // Arrange
        when(productoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> productoService.actualizar(999L, productoMock));
        verify(productoRepository, never()).save(any(Producto.class));
    }

    // ==================== Tests para eliminar ====================
    @Test
    @DisplayName("eliminar - Debe eliminar producto exitosamente")
    void testEliminar_Success() {
        // Arrange
        when(productoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productoRepository).deleteById(1L);

        // Act
        productoService.eliminar(1L);

        // Assert
        verify(productoRepository, times(1)).existsById(1L);
        verify(productoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar - Debe lanzar excepción cuando producto no existe")
    void testEliminar_NotFound() {
        // Arrange
        when(productoRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> productoService.eliminar(999L));
        verify(productoRepository, never()).deleteById(anyLong());
    }

    // ==================== Tests para buscarPorNombre ====================
    @Test
    @DisplayName("buscarPorNombre - Debe retornar productos que coincidan con nombre")
    void testBuscarPorNombre_Success() {
        // Arrange
        List<Producto> productos = Arrays.asList(productoMock);
        when(productoRepository.findByNombreContainingIgnoreCase(anyString())).thenReturn(productos);

        // Act
        List<Producto> resultado = productoService.buscarPorNombre("Laptop");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(productoRepository, times(1)).findByNombreContainingIgnoreCase("Laptop");
    }

    // ==================== Tests para buscarPorCategoria ====================
    @Test
    @DisplayName("buscarPorCategoria - Debe retornar productos de la categoría")
    void testBuscarPorCategoria_Success() {
        // Arrange
        List<Producto> productos = Arrays.asList(productoMock);
        when(productoRepository.findByCategoria(anyString())).thenReturn(productos);

        // Act
        List<Producto> resultado = productoService.buscarPorCategoria("Electrónicos");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(productoRepository, times(1)).findByCategoria("Electrónicos");
    }

    // ==================== Tests para buscarPorMarca ====================
    @Test
    @DisplayName("buscarPorMarca - Debe retornar productos de la marca")
    void testBuscarPorMarca_Success() {
        // Arrange
        List<Producto> productos = Arrays.asList(productoMock);
        when(productoRepository.findByMarca(anyString())).thenReturn(productos);

        // Act
        List<Producto> resultado = productoService.buscarPorMarca("HP");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(productoRepository, times(1)).findByMarca("HP");
    }

    // ==================== Tests para obtener productos activos ====================
    @Test
    @DisplayName("obtenerProductosActivos - Debe retornar solo productos activos")
    void testObtenerProductosActivos_Success() {
        // Arrange
        List<Producto> productos = Arrays.asList(productoMock);
        when(productoRepository.findByActivoTrue()).thenReturn(productos);

        // Act
        List<Producto> resultado = productoService.obtenerProductosActivos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(productoRepository, times(1)).findByActivoTrue();
    }

    // ==================== Tests para obtener productos con/sin stock ====================
    @Test
    @DisplayName("obtenerProductosConStock - Debe retornar productos con stock")
    void testObtenerProductosConStock_Success() {
        // Arrange
        List<Producto> productos = Arrays.asList(productoMock);
        when(productoRepository.findProductosConStock()).thenReturn(productos);

        // Act
        List<Producto> resultado = productoService.obtenerProductosConStock();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(productoRepository, times(1)).findProductosConStock();
    }

    @Test
    @DisplayName("obtenerProductosSinStock - Debe retornar productos sin stock")
    void testObtenerProductosSinStock_Success() {
        // Arrange
        productoMock.setStock(0);
        List<Producto> productos = Arrays.asList(productoMock);
        when(productoRepository.findProductosSinStock()).thenReturn(productos);

        // Act
        List<Producto> resultado = productoService.obtenerProductosSinStock();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(productoRepository, times(1)).findProductosSinStock();
    }

    // ==================== Tests para buscarPorRangoPrecios ====================
    @Test
    @DisplayName("buscarPorRangoPrecios - Debe retornar productos en rango de precios")
    void testBuscarPorRangoPrecios_Success() {
        // Arrange
        List<Producto> productos = Arrays.asList(productoMock);
        when(productoRepository.findByPrecioBetween(any(BigDecimal.class), any(BigDecimal.class)))
            .thenReturn(productos);

        // Act
        List<Producto> resultado = productoService.buscarPorRangoPrecios(
            new BigDecimal("500"), new BigDecimal("1000")
        );

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(productoRepository, times(1))
            .findByPrecioBetween(any(BigDecimal.class), any(BigDecimal.class));
    }

    // ==================== Tests para actualizarStock ====================
    @Test
    @DisplayName("actualizarStock - Debe actualizar stock exitosamente")
    void testActualizarStock_Success() {
        // Arrange
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoMock));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoMock);

        // Act
        Producto resultado = productoService.actualizarStock(1L, 20);

        // Assert
        assertNotNull(resultado);
        verify(productoRepository, times(1)).findById(1L);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    @DisplayName("actualizarStock - Debe lanzar excepción con stock negativo")
    void testActualizarStock_StockNegativo() {
        // Arrange
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoMock));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> productoService.actualizarStock(1L, -5));
        verify(productoRepository, never()).save(any(Producto.class));
    }

    // ==================== Tests para activar/desactivar ====================
    @Test
    @DisplayName("activar - Debe activar producto exitosamente")
    void testActivar_Success() {
        // Arrange
        productoMock.setActivo(false);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoMock));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoMock);

        // Act
        Producto resultado = productoService.activar(1L);

        // Assert
        assertNotNull(resultado);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    @DisplayName("desactivar - Debe desactivar producto exitosamente")
    void testDesactivar_Success() {
        // Arrange
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoMock));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoMock);

        // Act
        Producto resultado = productoService.desactivar(1L);

        // Assert
        assertNotNull(resultado);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    // ==================== Tests para obtenerCategorias ====================
    @Test
    @DisplayName("obtenerCategorias - Debe retornar lista de categorías únicas")
    void testObtenerCategorias_Success() {
        // Arrange
        List<String> categorias = Arrays.asList("Electrónicos", "Ropa", "Hogar");
        when(productoRepository.findDistinctCategorias()).thenReturn(categorias);

        // Act
        List<String> resultado = productoService.obtenerCategorias();

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        verify(productoRepository, times(1)).findDistinctCategorias();
    }

    // ==================== Tests para obtenerMarcas ====================
    @Test
    @DisplayName("obtenerMarcas - Debe retornar lista de marcas únicas")
    void testObtenerMarcas_Success() {
        // Arrange
        List<String> marcas = Arrays.asList("HP", "Dell", "Apple");
        when(productoRepository.findDistinctMarcas()).thenReturn(marcas);

        // Act
        List<String> resultado = productoService.obtenerMarcas();

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        verify(productoRepository, times(1)).findDistinctMarcas();
    }

    // ==================== Tests para buscarPorTexto ====================
    @Test
    @DisplayName("buscarPorTexto - Debe buscar productos por texto libre")
    void testBuscarPorTexto_Success() {
        // Arrange
        List<Producto> productos = Arrays.asList(productoMock);
        when(productoRepository.buscarPorTexto(anyString())).thenReturn(productos);

        // Act
        List<Producto> resultado = productoService.buscarPorTexto("Laptop");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(productoRepository, times(1)).buscarPorTexto("Laptop");
    }
}
