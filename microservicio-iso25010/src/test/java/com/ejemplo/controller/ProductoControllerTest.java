package com.ejemplo.controller;

import com.ejemplo.model.Producto;
import com.ejemplo.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración para ProductoController
 * Utiliza MockMvc con contexto completo de Spring Boot
 * Spring Security configurado con usuarios mock
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("ProductoController - Pruebas de Integración")
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductoService productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Laptop HP");
        producto.setDescripcion("Laptop HP 15 pulgadas");
        producto.setPrecio(new BigDecimal("799.99"));
        producto.setStock(10);
        producto.setCategoria("Electrónicos");
        producto.setMarca("HP");
        producto.setActivo(true);
    }

    // ==================== Tests para GET /productos ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /productos - Debe retornar lista de productos")
    void testObtenerTodos_Success() throws Exception {
        // Arrange
        when(productoService.obtenerTodos()).thenReturn(Arrays.asList(producto, producto));

        // Act & Assert
        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Laptop HP")));

        verify(productoService, times(1)).obtenerTodos();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /productos - Debe retornar lista vacía")
    void testObtenerTodos_EmptyList() throws Exception {
        // Arrange
        when(productoService.obtenerTodos()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(productoService, times(1)).obtenerTodos();
    }

    // ==================== Tests para GET /productos/{id} ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /productos/{id} - Debe retornar producto por ID")
    void testObtenerPorId_Success() throws Exception {
        // Arrange
        when(productoService.obtenerPorId(1L)).thenReturn(producto);

        // Act & Assert
        mockMvc.perform(get("/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Laptop HP")))
                .andExpect(jsonPath("$.precio", is(799.99)));

        verify(productoService, times(1)).obtenerPorId(1L);
    }

    // ==================== Tests para POST /productos ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /productos - Debe crear producto exitosamente")
    void testCrear_Success() throws Exception {
        // Arrange
        when(productoService.crear(any(Producto.class))).thenReturn(producto);

        // Act & Assert
        mockMvc.perform(post("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Laptop HP")));

        verify(productoService, times(1)).crear(any(Producto.class));
    }

    // ==================== Tests para PUT /productos/{id} ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /productos/{id} - Debe actualizar producto")
    void testActualizar_Success() throws Exception {
        // Arrange
        when(productoService.actualizar(eq(1L), any(Producto.class))).thenReturn(producto);

        // Act & Assert
        mockMvc.perform(put("/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Laptop HP")));

        verify(productoService, times(1)).actualizar(eq(1L), any(Producto.class));
    }

    // ==================== Tests para DELETE /productos/{id} ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /productos/{id} - Debe eliminar producto")
    void testEliminar_Success() throws Exception {
        // Arrange
        doNothing().when(productoService).eliminar(1L);

        // Act & Assert
        mockMvc.perform(delete("/productos/1"))
                .andExpect(status().isNoContent());

        verify(productoService, times(1)).eliminar(1L);
    }

    // ==================== Tests para GET /productos/buscar ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /productos/buscar - Debe buscar por nombre")
    void testBuscarPorNombre_Success() throws Exception {
        // Arrange
        when(productoService.buscarPorNombre(anyString())).thenReturn(Arrays.asList(producto));

        // Act & Assert
        mockMvc.perform(get("/productos/buscar")
                .param("nombre", "Laptop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(productoService, times(1)).buscarPorNombre("Laptop");
    }

    // ==================== Tests para GET /productos/categoria/{categoria} ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /productos/categoria/{categoria} - Debe buscar por categoría")
    void testBuscarPorCategoria_Success() throws Exception {
        // Arrange
        when(productoService.buscarPorCategoria(anyString())).thenReturn(Arrays.asList(producto));

        // Act & Assert
        mockMvc.perform(get("/productos/categoria/Electronicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(productoService, times(1)).buscarPorCategoria(anyString());
    }

    // ==================== Tests para GET /productos/marca/{marca} ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /productos/marca/{marca} - Debe buscar por marca")
    void testBuscarPorMarca_Success() throws Exception {
        // Arrange
        when(productoService.buscarPorMarca(anyString())).thenReturn(Arrays.asList(producto));

        // Act & Assert
        mockMvc.perform(get("/productos/marca/HP"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(productoService, times(1)).buscarPorMarca("HP");
    }

    // ==================== Tests para GET /productos/activos ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /productos/activos - Debe retornar productos activos")
    void testObtenerProductosActivos_Success() throws Exception {
        // Arrange
        when(productoService.obtenerProductosActivos()).thenReturn(Arrays.asList(producto));

        // Act & Assert
        mockMvc.perform(get("/productos/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(productoService, times(1)).obtenerProductosActivos();
    }

    // ==================== Tests para GET /productos/con-stock ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /productos/con-stock - Debe retornar productos con stock")
    void testObtenerProductosConStock_Success() throws Exception {
        // Arrange
        when(productoService.obtenerProductosConStock()).thenReturn(Arrays.asList(producto));

        // Act & Assert
        mockMvc.perform(get("/productos/con-stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(productoService, times(1)).obtenerProductosConStock();
    }

    // ==================== Tests para GET /productos/sin-stock ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /productos/sin-stock - Debe retornar productos sin stock")
    void testObtenerProductosSinStock_Success() throws Exception {
        // Arrange
        when(productoService.obtenerProductosSinStock()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/productos/sin-stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(productoService, times(1)).obtenerProductosSinStock();
    }

    // ==================== Tests para GET /productos/precio ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /productos/precio - Debe buscar por rango de precios")
    void testBuscarPorRangoPrecios_Success() throws Exception {
        // Arrange
        when(productoService.buscarPorRangoPrecios(any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(Arrays.asList(producto));

        // Act & Assert
        mockMvc.perform(get("/productos/precio")
                .param("precioMinimo", "500.00")
                .param("precioMaximo", "1000.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(productoService, times(1))
                .buscarPorRangoPrecios(any(BigDecimal.class), any(BigDecimal.class));
    }

    // ==================== Tests para GET /productos/buscar-texto ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /productos/buscar-texto - Debe buscar por texto libre")
    void testBuscarPorTexto_Success() throws Exception {
        // Arrange
        when(productoService.buscarPorTexto(anyString())).thenReturn(Arrays.asList(producto));

        // Act & Assert
        mockMvc.perform(get("/productos/buscar-texto")
                .param("texto", "Laptop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(productoService, times(1)).buscarPorTexto("Laptop");
    }

    // ==================== Tests para GET /productos/categorias ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /productos/categorias - Debe retornar lista de categorías")
    void testObtenerCategorias_Success() throws Exception {
        // Arrange
        List<String> categorias = Arrays.asList("Electrónicos", "Ropa", "Hogar");
        when(productoService.obtenerCategorias()).thenReturn(categorias);

        // Act & Assert
        mockMvc.perform(get("/productos/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        verify(productoService, times(1)).obtenerCategorias();
    }

    // ==================== Tests para GET /productos/marcas ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /productos/marcas - Debe retornar lista de marcas")
    void testObtenerMarcas_Success() throws Exception {
        // Arrange
        List<String> marcas = Arrays.asList("HP", "Dell", "Apple");
        when(productoService.obtenerMarcas()).thenReturn(marcas);

        // Act & Assert
        mockMvc.perform(get("/productos/marcas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        verify(productoService, times(1)).obtenerMarcas();
    }

    // ==================== Tests para PATCH /productos/{id}/stock ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PATCH /productos/{id}/stock - Debe actualizar stock")
    void testActualizarStock_Success() throws Exception {
        // Arrange
        producto.setStock(50);
        when(productoService.actualizarStock(eq(1L), eq(50))).thenReturn(producto);

        // Act & Assert
        mockMvc.perform(patch("/productos/1/stock")
                .param("stock", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock", is(50)));

        verify(productoService, times(1)).actualizarStock(1L, 50);
    }

    // ==================== Tests para PATCH /productos/{id}/activar ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PATCH /productos/{id}/activar - Debe activar producto")
    void testActivar_Success() throws Exception {
        // Arrange
        when(productoService.activar(1L)).thenReturn(producto);

        // Act & Assert
        mockMvc.perform(patch("/productos/1/activar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo", is(true)));

        verify(productoService, times(1)).activar(1L);
    }

    // ==================== Tests para PATCH /productos/{id}/desactivar ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PATCH /productos/{id}/desactivar - Debe desactivar producto")
    void testDesactivar_Success() throws Exception {
        // Arrange
        producto.setActivo(false);
        when(productoService.desactivar(1L)).thenReturn(producto);

        // Act & Assert
        mockMvc.perform(patch("/productos/1/desactivar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo", is(false)));

        verify(productoService, times(1)).desactivar(1L);
    }

    // ==================== Tests de Validación y Excepciones ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /productos - Debe fallar con datos inválidos (nombre vacío)")
    void testCrear_InvalidData_EmptyName() throws Exception {
        // Arrange
        producto.setNombre("");

        // Act & Assert
        mockMvc.perform(post("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isBadRequest());

        verify(productoService, never()).crear(any(Producto.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /productos - Debe fallar con precio negativo")
    void testCrear_InvalidData_NegativePrice() throws Exception {
        // Arrange
        producto.setPrecio(new BigDecimal("-10.00"));

        // Act & Assert
        mockMvc.perform(post("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isBadRequest());

        verify(productoService, never()).crear(any(Producto.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /productos/{id} - Debe retornar 404 cuando no existe")
    void testObtenerPorId_NotFound() throws Exception {
        // Arrange
        when(productoService.obtenerPorId(999L))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Producto no encontrado"));

        // Act & Assert
        mockMvc.perform(get("/productos/999"))
                .andExpect(status().isNotFound());

        verify(productoService, times(1)).obtenerPorId(999L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /productos/{id} - Debe retornar 404 cuando no existe")
    void testEliminar_NotFound() throws Exception {
        // Arrange
        doThrow(new jakarta.persistence.EntityNotFoundException("Producto no encontrado"))
                .when(productoService).eliminar(999L);

        // Act & Assert
        mockMvc.perform(delete("/productos/999"))
                .andExpect(status().isNotFound());

        verify(productoService, times(1)).eliminar(999L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PATCH /productos/{id}/stock - Debe actualizar con cantidad válida")
    void testActualizarStock_InvalidQuantity() throws Exception {
        // Arrange
        when(productoService.actualizarStock(eq(1L), eq(-5)))
                .thenThrow(new IllegalArgumentException("El stock no puede ser negativo"));

        // Act & Assert
        mockMvc.perform(patch("/productos/1/stock")
                .param("stock", "-5"))
                .andExpect(status().isBadRequest());

        verify(productoService, times(1)).actualizarStock(1L, -5);
    }
}
