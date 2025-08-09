package com.ejemplo.controller;

import com.ejemplo.model.Pedido;
import com.ejemplo.model.Usuario;
import com.ejemplo.model.Producto;
import com.ejemplo.service.UsuarioService;
import com.ejemplo.service.ProductoService;
import com.ejemplo.repository.PedidoRepository;
import com.ejemplo.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST para la gestión de pedidos
 * Proporciona endpoints para operaciones CRUD y consultas especializadas
 * 
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "API para la gestión de pedidos del sistema")
@CrossOrigin(origins = "*")
public class PedidoController {

    private static final Logger logger = LoggerFactory.getLogger(PedidoController.class);

    @Autowired
    private PedidoRepository pedidoRepository;
    
        @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * DTO para crear pedidos
     */
    public static class CrearPedidoRequest {
        private Long usuarioId;
        private Long productoId;
        private Integer cantidad;
        private String observaciones;

        // Getters y Setters
        public Long getUsuarioId() { return usuarioId; }
        public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        public String getObservaciones() { return observaciones; }
        public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    }

    /**
     * Obtiene todos los pedidos
     */
    @GetMapping
    @Operation(summary = "Obtener todos los pedidos", 
               description = "Retorna una lista con todos los pedidos del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pedidos obtenida exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Pedido.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Pedido>> obtenerTodos() {
        logger.info("GET /pedidos - Obteniendo todos los pedidos");
        List<Pedido> pedidos = pedidoRepository.findAll();
        logger.info("Se encontraron {} pedidos", pedidos.size());
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Obtiene un pedido por su ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por ID", 
               description = "Retorna un pedido específico basado en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido encontrado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Pedido.class))),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Pedido> obtenerPorId(
            @Parameter(description = "ID único del pedido", required = true, example = "1")
            @PathVariable Long id) {
        logger.info("GET /pedidos/{} - Obteniendo pedido por ID", id);
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado con ID: " + id));
        logger.info("Pedido encontrado: ID={}, Estado={}", pedido.getId(), pedido.getEstado());
        return ResponseEntity.ok(pedido);
    }

    /**
     * Crea un nuevo pedido
     */
    @PostMapping
    @Operation(summary = "Crear nuevo pedido", 
               description = "Crea un nuevo pedido con los datos proporcionados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Pedido.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Usuario o producto no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Pedido> crear(
            @Parameter(description = "Datos del pedido a crear", required = true)
            @Valid @RequestBody CrearPedidoRequest request) {
        logger.info("POST /pedidos - Creando nuevo pedido para usuario ID: {}, producto ID: {}", 
                   request.getUsuarioId(), request.getProductoId());

        // Obtener usuario y producto
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + request.getUsuarioId()));

        Producto producto = productoService.obtenerPorId(request.getProductoId());

        // Validar stock
        if (!producto.tieneStock(request.getCantidad())) {
            throw new IllegalArgumentException("Stock insuficiente. Stock disponible: " + producto.getStock());
        }

        // Crear pedido
        Pedido pedido = new Pedido(usuario, producto, request.getCantidad(), request.getObservaciones());
        pedido.setFechaPedido(LocalDateTime.now());
        
        // Reducir stock del producto
        productoService.reducirStock(producto.getId(), request.getCantidad());
        
        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        logger.info("Pedido creado exitosamente con ID: {}", pedidoGuardado.getId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoGuardado);
    }

    /**
     * Actualiza un pedido existente
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar pedido", 
               description = "Actualiza los datos de un pedido existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido actualizado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Pedido.class))),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Pedido> actualizar(
            @Parameter(description = "ID único del pedido", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos del pedido", required = true)
            @Valid @RequestBody CrearPedidoRequest request) {
        logger.info("PUT /pedidos/{} - Actualizando pedido", id);
        
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado con ID: " + id));

        // Solo permitir actualización si el pedido está pendiente
        if (pedidoExistente.getEstado() != Pedido.EstadoPedido.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden actualizar pedidos en estado PENDIENTE");
        }

        // Restaurar stock del producto anterior
        productoService.aumentarStock(pedidoExistente.getProducto().getId(), pedidoExistente.getCantidad());

        // Obtener nuevo producto
        Producto nuevoProducto = productoService.obtenerPorId(request.getProductoId());
        
        // Validar stock del nuevo producto
        if (!nuevoProducto.tieneStock(request.getCantidad())) {
            // Restaurar el stock que acabamos de devolver
            productoService.reducirStock(pedidoExistente.getProducto().getId(), pedidoExistente.getCantidad());
            throw new IllegalArgumentException("Stock insuficiente para el nuevo producto. Stock disponible: " + nuevoProducto.getStock());
        }

        // Actualizar pedido
        pedidoExistente.setProducto(nuevoProducto);
        pedidoExistente.setCantidad(request.getCantidad());
        pedidoExistente.setObservaciones(request.getObservaciones());
        pedidoExistente.setPrecioUnitario(nuevoProducto.getPrecio());
        pedidoExistente.calcularTotal();
        pedidoExistente.setFechaActualizacion(LocalDateTime.now());

        // Reducir stock del nuevo producto
        productoService.reducirStock(nuevoProducto.getId(), request.getCantidad());

        Pedido pedidoActualizado = pedidoRepository.save(pedidoExistente);
        logger.info("Pedido actualizado exitosamente con ID: {}", pedidoActualizado.getId());
        
        return ResponseEntity.ok(pedidoActualizado);
    }

    /**
     * Elimina un pedido
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar pedido", 
               description = "Elimina un pedido del sistema y restaura el stock")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pedido eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
        @ApiResponse(responseCode = "400", description = "ID inválido o pedido no puede ser eliminado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID único del pedido", required = true, example = "1")
            @PathVariable Long id) {
        logger.info("DELETE /pedidos/{} - Eliminando pedido", id);
        
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado con ID: " + id));

        // Solo permitir eliminación si el pedido puede ser cancelado
        if (!pedido.puedeSerCancelado()) {
            throw new IllegalStateException("Solo se pueden eliminar pedidos en estado PENDIENTE o CONFIRMADO");
        }

        // Restaurar stock
        productoService.aumentarStock(pedido.getProducto().getId(), pedido.getCantidad());
        
        pedidoRepository.deleteById(id);
        logger.info("Pedido eliminado exitosamente con ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene pedidos por usuario
     */
    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener pedidos por usuario", 
               description = "Retorna todos los pedidos de un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedidos del usuario obtenidos",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Pedido.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Pedido>> obtenerPorUsuario(
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @PathVariable Long usuarioId) {
        logger.info("GET /pedidos/usuario/{} - Obteniendo pedidos por usuario", usuarioId);
        List<Pedido> pedidos = pedidoRepository.findByUsuarioId(usuarioId);
        logger.info("Se encontraron {} pedidos para usuario ID: {}", pedidos.size(), usuarioId);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Obtiene pedidos por producto
     */
    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Obtener pedidos por producto", 
               description = "Retorna todos los pedidos de un producto específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedidos del producto obtenidos",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Pedido.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Pedido>> obtenerPorProducto(
            @Parameter(description = "ID del producto", required = true, example = "1")
            @PathVariable Long productoId) {
        logger.info("GET /pedidos/producto/{} - Obteniendo pedidos por producto", productoId);
        List<Pedido> pedidos = pedidoRepository.findByProductoId(productoId);
        logger.info("Se encontraron {} pedidos para producto ID: {}", pedidos.size(), productoId);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Obtiene pedidos por estado
     */
    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener pedidos por estado", 
               description = "Retorna todos los pedidos con un estado específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedidos por estado obtenidos",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Pedido.class))),
        @ApiResponse(responseCode = "400", description = "Estado inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Pedido>> obtenerPorEstado(
            @Parameter(description = "Estado del pedido", required = true, example = "PENDIENTE")
            @PathVariable String estado) {
        logger.info("GET /pedidos/estado/{} - Obteniendo pedidos por estado", estado);
        
        try {
            Pedido.EstadoPedido estadoPedido = Pedido.EstadoPedido.valueOf(estado.toUpperCase());
            List<Pedido> pedidos = pedidoRepository.findByEstado(estadoPedido);
            logger.info("Se encontraron {} pedidos con estado: {}", pedidos.size(), estado);
            return ResponseEntity.ok(pedidos);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado inválido: " + estado + 
                ". Estados válidos: PENDIENTE, CONFIRMADO, EN_PROCESO, ENVIADO, ENTREGADO, CANCELADO");
        }
    }

    /**
     * Cambia el estado de un pedido
     */
    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado de pedido", 
               description = "Actualiza el estado de un pedido específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Pedido.class))),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
        @ApiResponse(responseCode = "400", description = "Estado inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Pedido> cambiarEstado(
            @Parameter(description = "ID único del pedido", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nuevo estado", required = true, example = "CONFIRMADO")
            @RequestParam String estado) {
        logger.info("PATCH /pedidos/{}/estado?estado={} - Cambiando estado de pedido", id, estado);
        
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado con ID: " + id));

        try {
            Pedido.EstadoPedido nuevoEstado = Pedido.EstadoPedido.valueOf(estado.toUpperCase());
            pedido.setEstado(nuevoEstado);
            pedido.setFechaActualizacion(LocalDateTime.now());
            
            Pedido pedidoActualizado = pedidoRepository.save(pedido);
            logger.info("Estado del pedido actualizado a: {}", nuevoEstado);
            
            return ResponseEntity.ok(pedidoActualizado);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado inválido: " + estado + 
                ". Estados válidos: PENDIENTE, CONFIRMADO, EN_PROCESO, ENVIADO, ENTREGADO, CANCELADO");
        }
    }

    /**
     * Cancela un pedido
     */
    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar pedido", 
               description = "Cancela un pedido y restaura el stock del producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido cancelado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Pedido.class))),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
        @ApiResponse(responseCode = "400", description = "Pedido no puede ser cancelado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Pedido> cancelar(
            @Parameter(description = "ID único del pedido", required = true, example = "1")
            @PathVariable Long id) {
        logger.info("PATCH /pedidos/{}/cancelar - Cancelando pedido", id);
        
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado con ID: " + id));

        if (!pedido.puedeSerCancelado()) {
            throw new IllegalStateException("El pedido no puede ser cancelado. Estado actual: " + pedido.getEstado());
        }

        // Restaurar stock
        productoService.aumentarStock(pedido.getProducto().getId(), pedido.getCantidad());
        
        // Cambiar estado
        pedido.setEstado(Pedido.EstadoPedido.CANCELADO);
        pedido.setFechaActualizacion(LocalDateTime.now());
        
        Pedido pedidoCancelado = pedidoRepository.save(pedido);
        logger.info("Pedido cancelado exitosamente con ID: {}", id);
        
        return ResponseEntity.ok(pedidoCancelado);
    }

    /**
     * Obtiene estadísticas de pedidos
     */
    @GetMapping("/estadisticas")
    @Operation(summary = "Obtener estadísticas de pedidos", 
               description = "Retorna estadísticas generales sobre los pedidos del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Object> obtenerEstadisticas() {
        logger.info("GET /pedidos/estadisticas - Obteniendo estadísticas de pedidos");
        
        long totalPedidos = pedidoRepository.count();
        long pedidosPendientes = pedidoRepository.countByEstado(Pedido.EstadoPedido.PENDIENTE);
        long pedidosConfirmados = pedidoRepository.countByEstado(Pedido.EstadoPedido.CONFIRMADO);
        long pedidosEntregados = pedidoRepository.countByEstado(Pedido.EstadoPedido.ENTREGADO);
        long pedidosCancelados = pedidoRepository.countByEstado(Pedido.EstadoPedido.CANCELADO);
        BigDecimal totalVentas = pedidoRepository.calcularTotalGeneralVentas();
        
        var estadisticas = new Object() {
            public final long total = totalPedidos;
            public final long pendientes = pedidosPendientes;
            public final long confirmados = pedidosConfirmados;
            public final long entregados = pedidosEntregados;
            public final long cancelados = pedidosCancelados;
            public final BigDecimal ventasTotal = totalVentas;
        };
        
        logger.info("Estadísticas: Total={}, Pendientes={}, Confirmados={}, Entregados={}, Cancelados={}, Ventas={}",
                   totalPedidos, pedidosPendientes, pedidosConfirmados, pedidosEntregados, pedidosCancelados, totalVentas);
        return ResponseEntity.ok(estadisticas);
    }
}

