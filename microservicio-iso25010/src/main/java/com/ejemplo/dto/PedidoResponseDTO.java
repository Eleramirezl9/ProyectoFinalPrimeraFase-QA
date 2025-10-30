package com.ejemplo.dto;

import com.ejemplo.model.Pedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para la respuesta de Pedidos
 * Evita ciclos de serialización JSON al exponer solo datos necesarios
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
public class PedidoResponseDTO {

    private Long id;
    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioEmail;
    private Long productoId;
    private String productoNombre;
    private BigDecimal productoPrecio;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal total;
    private String estado;
    private String observaciones;
    private LocalDateTime fechaPedido;
    private LocalDateTime fechaEntrega;
    private LocalDateTime fechaActualizacion;

    // Constructor vacío
    public PedidoResponseDTO() {
    }

    // Constructor desde entidad Pedido
    public PedidoResponseDTO(Pedido pedido) {
        this.id = pedido.getId();
        this.cantidad = pedido.getCantidad();
        this.precioUnitario = pedido.getPrecioUnitario();
        this.total = pedido.getTotal();
        this.estado = pedido.getEstado() != null ? pedido.getEstado().name() : null;
        this.observaciones = pedido.getObservaciones();
        this.fechaPedido = pedido.getFechaPedido();
        this.fechaEntrega = pedido.getFechaEntrega();
        this.fechaActualizacion = pedido.getFechaActualizacion();

        // Usuario (evitando carga completa)
        if (pedido.getUsuario() != null) {
            this.usuarioId = pedido.getUsuario().getId();
            this.usuarioNombre = pedido.getUsuario().getNombre() + " " + pedido.getUsuario().getApellido();
            this.usuarioEmail = pedido.getUsuario().getEmail();
        }

        // Producto (evitando carga completa)
        if (pedido.getProducto() != null) {
            this.productoId = pedido.getProducto().getId();
            this.productoNombre = pedido.getProducto().getNombre();
            this.productoPrecio = pedido.getProducto().getPrecio();
        }
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public BigDecimal getProductoPrecio() {
        return productoPrecio;
    }

    public void setProductoPrecio(BigDecimal productoPrecio) {
        this.productoPrecio = productoPrecio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
