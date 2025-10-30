package com.ejemplo.controller;

import com.ejemplo.model.Pedido;
import com.ejemplo.model.Producto;
import com.ejemplo.model.Usuario;
import com.ejemplo.repository.PedidoRepository;
import com.ejemplo.repository.UsuarioRepository;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración para PedidoController
 * Valida operaciones CRUD, cálculos de totales y gestión de estados
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("PedidoController - Pruebas de Integración")
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PedidoRepository pedidoRepository;

    @MockBean
    private ProductoService productoService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    private Pedido pedido;
    private Usuario usuario;
    private Producto producto;

    @BeforeEach
    void setUp() {
        // Configurar usuario
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setUsername("jperez");
        usuario.setEmail("juan@example.com");
        usuario.setActivo(true);

        // Configurar producto
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Laptop HP");
        producto.setDescripcion("Laptop HP 15 pulgadas");
        producto.setPrecio(new BigDecimal("799.99"));
        producto.setStock(10);
        producto.setCategoria("Electrónicos");
        producto.setMarca("HP");
        producto.setActivo(true);

        // Configurar pedido
        pedido = new Pedido(usuario, producto, 2);
        pedido.setId(1L);
        pedido.setEstado(Pedido.EstadoPedido.PENDIENTE);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setPrecioUnitario(producto.getPrecio());
        pedido.calcularTotal();
    }

    // ==================== Tests para GET /pedidos ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /pedidos - Debe retornar lista de pedidos")
    void testObtenerTodos_Success() throws Exception {
        // Arrange
        when(pedidoRepository.findAllWithRelations()).thenReturn(Arrays.asList(pedido, pedido));

        // Act & Assert
        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(pedidoRepository, times(1)).findAllWithRelations();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /pedidos - Debe retornar lista vacía")
    void testObtenerTodos_EmptyList() throws Exception {
        // Arrange
        when(pedidoRepository.findAllWithRelations()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(pedidoRepository, times(1)).findAllWithRelations();
    }

    // ==================== Tests para GET /pedidos/{id} ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /pedidos/{id} - Debe retornar pedido por ID")
    void testObtenerPorId_Success() throws Exception {
        // Arrange
        when(pedidoRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(pedido));

        // Act & Assert
        mockMvc.perform(get("/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.cantidad", is(2)))
                .andExpect(jsonPath("$.estado", is("PENDIENTE")));

        verify(pedidoRepository, times(1)).findByIdWithRelations(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /pedidos/{id} - Debe retornar 404 cuando no existe")
    void testObtenerPorId_NotFound() throws Exception {
        // Arrange
        when(pedidoRepository.findByIdWithRelations(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/pedidos/999"))
                .andExpect(status().isNotFound());

        verify(pedidoRepository, times(1)).findByIdWithRelations(999L);
    }

    // ==================== Tests para POST /pedidos ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /pedidos - Debe crear pedido exitosamente")
    void testCrear_Success() throws Exception {
        // Arrange
        PedidoController.CrearPedidoRequest request = new PedidoController.CrearPedidoRequest();
        request.setUsuarioId(1L);
        request.setProductoId(1L);
        request.setCantidad(2);
        request.setObservaciones("Pedido de prueba");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(productoService.obtenerPorId(1L)).thenReturn(producto);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(productoService.reducirStock(1L, 2)).thenReturn(producto);

        // Act & Assert
        mockMvc.perform(post("/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cantidad", is(2)))
                .andExpect(jsonPath("$.estado", is("PENDIENTE")));

        verify(usuarioRepository, times(1)).findById(1L);
        verify(productoService, times(1)).obtenerPorId(1L);
        verify(productoService, times(1)).reducirStock(1L, 2);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /pedidos - Debe fallar con datos inválidos (cantidad negativa)")
    void testCrear_InvalidData_NegativeQuantity() throws Exception {
        // Arrange
        PedidoController.CrearPedidoRequest request = new PedidoController.CrearPedidoRequest();
        request.setUsuarioId(1L);
        request.setProductoId(1L);
        request.setCantidad(-1);

        // Act & Assert
        mockMvc.perform(post("/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /pedidos - Debe fallar cuando usuario no existe")
    void testCrear_UsuarioNoExiste() throws Exception {
        // Arrange
        PedidoController.CrearPedidoRequest request = new PedidoController.CrearPedidoRequest();
        request.setUsuarioId(999L);
        request.setProductoId(1L);
        request.setCantidad(2);

        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(usuarioRepository, times(1)).findById(999L);
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /pedidos - Debe fallar con stock insuficiente")
    void testCrear_StockInsuficiente() throws Exception {
        // Arrange
        PedidoController.CrearPedidoRequest request = new PedidoController.CrearPedidoRequest();
        request.setUsuarioId(1L);
        request.setProductoId(1L);
        request.setCantidad(100);

        producto.setStock(5);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(productoService.obtenerPorId(1L)).thenReturn(producto);

        // Act & Assert
        mockMvc.perform(post("/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(usuarioRepository, times(1)).findById(1L);
        verify(productoService, times(1)).obtenerPorId(1L);
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    // ==================== Tests para PUT /pedidos/{id} ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /pedidos/{id} - Debe actualizar pedido pendiente")
    void testActualizar_Success() throws Exception {
        // Arrange
        PedidoController.CrearPedidoRequest request = new PedidoController.CrearPedidoRequest();
        request.setUsuarioId(1L);
        request.setProductoId(1L);
        request.setCantidad(3);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(productoService.obtenerPorId(1L)).thenReturn(producto);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(productoService.aumentarStock(1L, 2)).thenReturn(producto);
        when(productoService.reducirStock(1L, 3)).thenReturn(producto);

        // Act & Assert
        mockMvc.perform(put("/pedidos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(pedidoRepository, times(1)).findById(1L);
        verify(productoService, times(1)).aumentarStock(1L, 2);
        verify(productoService, times(1)).reducirStock(1L, 3);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /pedidos/{id} - Debe fallar si no está pendiente")
    void testActualizar_EstadoInvalido() throws Exception {
        // Arrange
        pedido.setEstado(Pedido.EstadoPedido.ENTREGADO);
        PedidoController.CrearPedidoRequest request = new PedidoController.CrearPedidoRequest();
        request.setUsuarioId(1L);
        request.setProductoId(1L);
        request.setCantidad(3);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        // Act & Assert
        mockMvc.perform(put("/pedidos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        verify(pedidoRepository, times(1)).findById(1L);
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    // ==================== Tests para DELETE /pedidos/{id} ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /pedidos/{id} - Debe eliminar pedido pendiente")
    void testEliminar_Success() throws Exception {
        // Arrange
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(productoService.aumentarStock(1L, 2)).thenReturn(producto);
        doNothing().when(pedidoRepository).deleteById(1L);

        // Act & Assert
        mockMvc.perform(delete("/pedidos/1"))
                .andExpect(status().isNoContent());

        verify(pedidoRepository, times(1)).findById(1L);
        verify(productoService, times(1)).aumentarStock(1L, 2);
        verify(pedidoRepository, times(1)).deleteById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /pedidos/{id} - Debe fallar si pedido no puede cancelarse")
    void testEliminar_NoPuedeCancelarse() throws Exception {
        // Arrange
        pedido.setEstado(Pedido.EstadoPedido.ENTREGADO);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        // Act & Assert
        mockMvc.perform(delete("/pedidos/1"))
                .andExpect(status().isConflict());

        verify(pedidoRepository, times(1)).findById(1L);
        verify(pedidoRepository, never()).deleteById(1L);
    }

    // ==================== Tests para GET /pedidos/usuario/{usuarioId} ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /pedidos/usuario/{usuarioId} - Debe retornar pedidos del usuario")
    void testObtenerPorUsuario_Success() throws Exception {
        // Arrange
        when(pedidoRepository.findByUsuarioId(1L)).thenReturn(Arrays.asList(pedido));

        // Act & Assert
        mockMvc.perform(get("/pedidos/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(pedidoRepository, times(1)).findByUsuarioId(1L);
    }

    // ==================== Tests para GET /pedidos/producto/{productoId} ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /pedidos/producto/{productoId} - Debe retornar pedidos del producto")
    void testObtenerPorProducto_Success() throws Exception {
        // Arrange
        when(pedidoRepository.findByProductoId(1L)).thenReturn(Arrays.asList(pedido));

        // Act & Assert
        mockMvc.perform(get("/pedidos/producto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(pedidoRepository, times(1)).findByProductoId(1L);
    }

    // ==================== Tests para GET /pedidos/estado/{estado} ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /pedidos/estado/{estado} - Debe retornar pedidos por estado")
    void testObtenerPorEstado_Success() throws Exception {
        // Arrange
        when(pedidoRepository.findByEstado(Pedido.EstadoPedido.PENDIENTE))
                .thenReturn(Arrays.asList(pedido));

        // Act & Assert
        mockMvc.perform(get("/pedidos/estado/PENDIENTE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(pedidoRepository, times(1)).findByEstado(Pedido.EstadoPedido.PENDIENTE);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /pedidos/estado/{estado} - Debe fallar con estado inválido")
    void testObtenerPorEstado_EstadoInvalido() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/pedidos/estado/INVALIDO"))
                .andExpect(status().isBadRequest());

        verify(pedidoRepository, never()).findByEstado(any());
    }

    // ==================== Tests para PATCH /pedidos/{id}/estado ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PATCH /pedidos/{id}/estado - Debe cambiar estado exitosamente")
    void testCambiarEstado_Success() throws Exception {
        // Arrange
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        // Act & Assert
        mockMvc.perform(patch("/pedidos/1/estado")
                .param("estado", "CONFIRMADO"))
                .andExpect(status().isOk());

        verify(pedidoRepository, times(1)).findById(1L);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PATCH /pedidos/{id}/estado - Debe fallar con estado inválido")
    void testCambiarEstado_EstadoInvalido() throws Exception {
        // Arrange
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        // Act & Assert
        mockMvc.perform(patch("/pedidos/1/estado")
                .param("estado", "ESTADO_INVALIDO"))
                .andExpect(status().isBadRequest());

        verify(pedidoRepository, times(1)).findById(1L);
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    // ==================== Tests para PATCH /pedidos/{id}/cancelar ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PATCH /pedidos/{id}/cancelar - Debe cancelar pedido pendiente")
    void testCancelar_Success() throws Exception {
        // Arrange
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(productoService.aumentarStock(1L, 2)).thenReturn(producto);

        // Act & Assert
        mockMvc.perform(patch("/pedidos/1/cancelar"))
                .andExpect(status().isOk());

        verify(pedidoRepository, times(1)).findById(1L);
        verify(productoService, times(1)).aumentarStock(1L, 2);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PATCH /pedidos/{id}/cancelar - Debe fallar si no puede cancelarse")
    void testCancelar_NoPuedeCancelarse() throws Exception {
        // Arrange
        pedido.setEstado(Pedido.EstadoPedido.ENTREGADO);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        // Act & Assert
        mockMvc.perform(patch("/pedidos/1/cancelar"))
                .andExpect(status().isConflict());

        verify(pedidoRepository, times(1)).findById(1L);
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    // ==================== Tests para GET /pedidos/estadisticas ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /pedidos/estadisticas - Debe retornar estadísticas")
    void testObtenerEstadisticas_Success() throws Exception {
        // Arrange
        when(pedidoRepository.count()).thenReturn(10L);
        when(pedidoRepository.countByEstado(Pedido.EstadoPedido.PENDIENTE)).thenReturn(3L);
        when(pedidoRepository.countByEstado(Pedido.EstadoPedido.CONFIRMADO)).thenReturn(2L);
        when(pedidoRepository.countByEstado(Pedido.EstadoPedido.ENTREGADO)).thenReturn(4L);
        when(pedidoRepository.countByEstado(Pedido.EstadoPedido.CANCELADO)).thenReturn(1L);
        when(pedidoRepository.calcularTotalGeneralVentas()).thenReturn(new BigDecimal("5000.00"));

        // Act & Assert
        mockMvc.perform(get("/pedidos/estadisticas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", is(10)))
                .andExpect(jsonPath("$.pendientes", is(3)))
                .andExpect(jsonPath("$.confirmados", is(2)))
                .andExpect(jsonPath("$.entregados", is(4)))
                .andExpect(jsonPath("$.cancelados", is(1)))
                .andExpect(jsonPath("$.ventasTotal", is(5000.00)));

        verify(pedidoRepository, times(1)).count();
        verify(pedidoRepository, times(4)).countByEstado(any());
        verify(pedidoRepository, times(1)).calcularTotalGeneralVentas();
    }

    // ==================== Tests de Validación de Cálculos ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Validar cálculo automático de total en pedido")
    void testCalculoAutomaticoTotal() throws Exception {
        // Arrange
        PedidoController.CrearPedidoRequest request = new PedidoController.CrearPedidoRequest();
        request.setUsuarioId(1L);
        request.setProductoId(1L);
        request.setCantidad(5);

        Pedido pedidoConTotal = new Pedido(usuario, producto, 5);
        pedidoConTotal.setId(1L);
        pedidoConTotal.calcularTotal();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(productoService.obtenerPorId(1L)).thenReturn(producto);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoConTotal);
        when(productoService.reducirStock(1L, 5)).thenReturn(producto);

        // Act & Assert
        mockMvc.perform(post("/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.total", is(3999.95)));

        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }
}
