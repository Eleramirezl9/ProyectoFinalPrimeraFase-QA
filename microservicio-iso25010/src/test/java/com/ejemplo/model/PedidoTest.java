package com.ejemplo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la entidad Pedido
 * Valida lógica de negocio, cálculos y transiciones de estado
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@DisplayName("Pedido - Pruebas Unitarias")
class PedidoTest {

    private Usuario usuario;
    private Producto producto;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Laptop");
        producto.setPrecio(new BigDecimal("1500.00"));
        producto.setStock(10);

        pedido = new Pedido(usuario, producto, 2);
    }

    @Test
    @DisplayName("Debe crear pedido con constructor completo")
    void testConstructorCompleto() {
        // Arrange & Act
        Pedido nuevoPedido = new Pedido(usuario, producto, 3, "Entrega urgente");

        // Assert
        assertNotNull(nuevoPedido);
        assertEquals(usuario, nuevoPedido.getUsuario());
        assertEquals(producto, nuevoPedido.getProducto());
        assertEquals(3, nuevoPedido.getCantidad());
        assertEquals("Entrega urgente", nuevoPedido.getObservaciones());
        assertEquals(new BigDecimal("4500.00"), nuevoPedido.getTotal());
        assertNotNull(nuevoPedido.getFechaPedido());
    }

    @Test
    @DisplayName("Debe calcular el total correctamente")
    void testCalcularTotal() {
        // Arrange
        pedido.setCantidad(5);
        pedido.setPrecioUnitario(new BigDecimal("100.00"));

        // Act
        pedido.calcularTotal();

        // Assert
        assertEquals(new BigDecimal("500.00"), pedido.getTotal());
    }

    @Test
    @DisplayName("Debe calcular total con decimales")
    void testCalcularTotalConDecimales() {
        // Arrange
        pedido.setCantidad(3);
        pedido.setPrecioUnitario(new BigDecimal("99.99"));

        // Act
        pedido.calcularTotal();

        // Assert
        assertEquals(new BigDecimal("299.97"), pedido.getTotal());
    }

    @Test
    @DisplayName("Debe permitir cancelar pedido PENDIENTE")
    void testPuedeSerCanceladoPendiente() {
        // Arrange
        pedido.setEstado(Pedido.EstadoPedido.PENDIENTE);

        // Act & Assert
        assertTrue(pedido.puedeSerCancelado());
    }

    @Test
    @DisplayName("Debe permitir cancelar pedido CONFIRMADO")
    void testPuedeSerCanceladoConfirmado() {
        // Arrange
        pedido.setEstado(Pedido.EstadoPedido.CONFIRMADO);

        // Act & Assert
        assertTrue(pedido.puedeSerCancelado());
    }

    @Test
    @DisplayName("No debe permitir cancelar pedido EN_PROCESO")
    void testNoPuedeSerCanceladoEnProceso() {
        // Arrange
        pedido.setEstado(Pedido.EstadoPedido.EN_PROCESO);

        // Act & Assert
        assertFalse(pedido.puedeSerCancelado());
    }

    @Test
    @DisplayName("No debe permitir cancelar pedido ENVIADO")
    void testNoPuedeSerCanceladoEnviado() {
        // Arrange
        pedido.setEstado(Pedido.EstadoPedido.ENVIADO);

        // Act & Assert
        assertFalse(pedido.puedeSerCancelado());
    }

    @Test
    @DisplayName("No debe permitir cancelar pedido ENTREGADO")
    void testNoPuedeSerCanceladoEntregado() {
        // Arrange
        pedido.setEstado(Pedido.EstadoPedido.ENTREGADO);

        // Act & Assert
        assertFalse(pedido.puedeSerCancelado());
    }

    @Test
    @DisplayName("Debe verificar si está entregado")
    void testEstaEntregado() {
        // Arrange
        pedido.setEstado(Pedido.EstadoPedido.ENTREGADO);

        // Act & Assert
        assertTrue(pedido.estaEntregado());
    }

    @Test
    @DisplayName("Debe verificar si no está entregado")
    void testNoEstaEntregado() {
        // Arrange
        pedido.setEstado(Pedido.EstadoPedido.PENDIENTE);

        // Act & Assert
        assertFalse(pedido.estaEntregado());
    }

    @Test
    @DisplayName("Debe verificar si está cancelado")
    void testEstaCancelado() {
        // Arrange
        pedido.setEstado(Pedido.EstadoPedido.CANCELADO);

        // Act & Assert
        assertTrue(pedido.estaCancelado());
    }

    @Test
    @DisplayName("Debe verificar si no está cancelado")
    void testNoEstaCancelado() {
        // Arrange
        pedido.setEstado(Pedido.EstadoPedido.PENDIENTE);

        // Act & Assert
        assertFalse(pedido.estaCancelado());
    }

    @Test
    @DisplayName("Debe inicializar estado como PENDIENTE")
    void testEstadoInicialPendiente() {
        // Arrange & Act
        Pedido nuevoPedido = new Pedido();

        // Assert
        assertEquals(Pedido.EstadoPedido.PENDIENTE, nuevoPedido.getEstado());
    }

    @Test
    @DisplayName("Debe establecer fecha de pedido en constructor")
    void testFechaPedidoEnConstructor() {
        // Arrange
        LocalDateTime antes = LocalDateTime.now().minusSeconds(1);

        // Act
        Pedido nuevoPedido = new Pedido(usuario, producto, 1);
        LocalDateTime despues = LocalDateTime.now().plusSeconds(1);

        // Assert
        assertNotNull(nuevoPedido.getFechaPedido());
        assertTrue(nuevoPedido.getFechaPedido().isAfter(antes));
        assertTrue(nuevoPedido.getFechaPedido().isBefore(despues));
    }

    @Test
    @DisplayName("Debe ejecutar prePersist correctamente")
    void testPrePersist() {
        // Arrange
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setProducto(producto);
        nuevoPedido.setCantidad(2);

        // Act
        nuevoPedido.prePersist();

        // Assert
        assertEquals(producto.getPrecio(), nuevoPedido.getPrecioUnitario());
        assertNotNull(nuevoPedido.getTotal());
    }

    @Test
    @DisplayName("Debe ejecutar preUpdate correctamente")
    void testPreUpdate() {
        // Arrange
        LocalDateTime antes = LocalDateTime.now().minusSeconds(1);

        // Act
        pedido.preUpdate();
        LocalDateTime despues = LocalDateTime.now().plusSeconds(1);

        // Assert
        assertNotNull(pedido.getFechaActualizacion());
        assertTrue(pedido.getFechaActualizacion().isAfter(antes));
        assertTrue(pedido.getFechaActualizacion().isBefore(despues));
    }

    @Test
    @DisplayName("Debe actualizar precio al establecer producto")
    void testSetProductoActualizaPrecio() {
        // Arrange
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setCantidad(2);

        // Act
        nuevoPedido.setProducto(producto);

        // Assert
        assertEquals(producto.getPrecio(), nuevoPedido.getPrecioUnitario());
        assertEquals(new BigDecimal("3000.00"), nuevoPedido.getTotal());
    }

    @Test
    @DisplayName("No debe actualizar precio si ya existe al establecer producto")
    void testSetProductoNoActualizaPrecioExistente() {
        // Arrange
        BigDecimal precioOriginal = new BigDecimal("1000.00");
        pedido.setPrecioUnitario(precioOriginal);

        Producto nuevoProducto = new Producto();
        nuevoProducto.setPrecio(new BigDecimal("2000.00"));

        // Act
        pedido.setProducto(nuevoProducto);

        // Assert
        assertEquals(precioOriginal, pedido.getPrecioUnitario());
    }

    @Test
    @DisplayName("Debe generar toString correctamente")
    void testToString() {
        // Arrange
        pedido.setId(1L);

        // Act
        String resultado = pedido.toString();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.contains("id=1"));
        assertTrue(resultado.contains("cantidad=2"));
        assertTrue(resultado.contains("estado=PENDIENTE"));
    }

    @Test
    @DisplayName("EstadoPedido debe tener descripción correcta")
    void testEstadoPedidoDescripcion() {
        // Assert
        assertEquals("Pendiente", Pedido.EstadoPedido.PENDIENTE.getDescripcion());
        assertEquals("Confirmado", Pedido.EstadoPedido.CONFIRMADO.getDescripcion());
        assertEquals("En Proceso", Pedido.EstadoPedido.EN_PROCESO.getDescripcion());
        assertEquals("Enviado", Pedido.EstadoPedido.ENVIADO.getDescripcion());
        assertEquals("Entregado", Pedido.EstadoPedido.ENTREGADO.getDescripcion());
        assertEquals("Cancelado", Pedido.EstadoPedido.CANCELADO.getDescripcion());
    }

    @Test
    @DisplayName("Debe manejar cantidad nula en calcularTotal")
    void testCalcularTotalConCantidadNula() {
        // Arrange
        pedido.setCantidad(null);

        // Act
        pedido.calcularTotal();

        // Assert
        // No debe lanzar excepción, simplemente no calcula
        assertNotNull(pedido);
    }

    @Test
    @DisplayName("Debe manejar precio nulo en calcularTotal")
    void testCalcularTotalConPrecioNulo() {
        // Arrange
        pedido.setPrecioUnitario(null);

        // Act
        pedido.calcularTotal();

        // Assert
        // No debe lanzar excepción, simplemente no calcula
        assertNotNull(pedido);
    }
}
