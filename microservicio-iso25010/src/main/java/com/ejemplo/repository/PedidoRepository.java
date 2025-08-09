package com.ejemplo.repository;

import com.ejemplo.model.Pedido;
import com.ejemplo.model.Usuario;
import com.ejemplo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para la entidad Pedido
 * Proporciona operaciones CRUD y consultas personalizadas
 * 
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    /**
     * Busca pedidos por usuario
     * @param usuario Usuario propietario de los pedidos
     * @return Lista de pedidos del usuario
     */
    List<Pedido> findByUsuario(Usuario usuario);

    /**
     * Busca pedidos por ID de usuario
     * @param usuarioId ID del usuario
     * @return Lista de pedidos del usuario
     */
    List<Pedido> findByUsuarioId(Long usuarioId);

    /**
     * Busca pedidos por producto
     * @param producto Producto de los pedidos
     * @return Lista de pedidos del producto
     */
    List<Pedido> findByProducto(Producto producto);

    /**
     * Busca pedidos por ID de producto
     * @param productoId ID del producto
     * @return Lista de pedidos del producto
     */
    List<Pedido> findByProductoId(Long productoId);

    /**
     * Busca pedidos por estado
     * @param estado Estado del pedido
     * @return Lista de pedidos con el estado especificado
     */
    List<Pedido> findByEstado(Pedido.EstadoPedido estado);

    /**
     * Busca pedidos pendientes
     * @return Lista de pedidos pendientes
     */
    @Query("SELECT p FROM Pedido p WHERE p.estado = 'PENDIENTE'")
    List<Pedido> findPedidosPendientes();

    /**
     * Busca pedidos confirmados
     * @return Lista de pedidos confirmados
     */
    @Query("SELECT p FROM Pedido p WHERE p.estado = 'CONFIRMADO'")
    List<Pedido> findPedidosConfirmados();

    /**
     * Busca pedidos entregados
     * @return Lista de pedidos entregados
     */
    @Query("SELECT p FROM Pedido p WHERE p.estado = 'ENTREGADO'")
    List<Pedido> findPedidosEntregados();

    /**
     * Busca pedidos cancelados
     * @return Lista de pedidos cancelados
     */
    @Query("SELECT p FROM Pedido p WHERE p.estado = 'CANCELADO'")
    List<Pedido> findPedidosCancelados();

    /**
     * Busca pedidos por rango de fechas
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Lista de pedidos en el rango de fechas
     */
    @Query("SELECT p FROM Pedido p WHERE p.fechaPedido BETWEEN :fechaInicio AND :fechaFin")
    List<Pedido> findByFechaPedidoBetween(
            @Param("fechaInicio") LocalDateTime fechaInicio, 
            @Param("fechaFin") LocalDateTime fechaFin);

    /**
     * Busca pedidos por rango de totales
     * @param totalMinimo Total mínimo
     * @param totalMaximo Total máximo
     * @return Lista de pedidos en el rango de totales
     */
    @Query("SELECT p FROM Pedido p WHERE p.total BETWEEN :totalMinimo AND :totalMaximo")
    List<Pedido> findByTotalBetween(
            @Param("totalMinimo") BigDecimal totalMinimo, 
            @Param("totalMaximo") BigDecimal totalMaximo);

    /**
     * Busca pedidos con total mayor al especificado
     * @param total Total mínimo
     * @return Lista de pedidos con total mayor
     */
    List<Pedido> findByTotalGreaterThan(BigDecimal total);

    /**
     * Busca pedidos con total menor al especificado
     * @param total Total máximo
     * @return Lista de pedidos con total menor
     */
    List<Pedido> findByTotalLessThan(BigDecimal total);

    /**
     * Cuenta pedidos por usuario
     * @param usuario Usuario
     * @return Número de pedidos del usuario
     */
    long countByUsuario(Usuario usuario);

    /**
     * Cuenta pedidos por producto
     * @param producto Producto
     * @return Número de pedidos del producto
     */
    long countByProducto(Producto producto);

    /**
     * Cuenta pedidos por estado
     * @param estado Estado del pedido
     * @return Número de pedidos con el estado
     */
    long countByEstado(Pedido.EstadoPedido estado);

    /**
     * Calcula el total de ventas por usuario
     * @param usuarioId ID del usuario
     * @return Total de ventas del usuario
     */
    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Pedido p WHERE p.usuario.id = :usuarioId")
    BigDecimal calcularTotalVentasPorUsuario(@Param("usuarioId") Long usuarioId);

    /**
     * Calcula el total de ventas por producto
     * @param productoId ID del producto
     * @return Total de ventas del producto
     */
    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Pedido p WHERE p.producto.id = :productoId")
    BigDecimal calcularTotalVentasPorProducto(@Param("productoId") Long productoId);

    /**
     * Calcula el total general de ventas
     * @return Total general de ventas
     */
    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Pedido p")
    BigDecimal calcularTotalGeneralVentas();

    /**
     * Busca los últimos pedidos de un usuario
     * @param usuarioId ID del usuario
     * @param limite Número máximo de pedidos a retornar
     * @return Lista de últimos pedidos del usuario
     */
    @Query("SELECT p FROM Pedido p WHERE p.usuario.id = :usuarioId ORDER BY p.fechaPedido DESC")
    List<Pedido> findUltimosPedidosPorUsuario(@Param("usuarioId") Long usuarioId);

    /**
     * Busca pedidos que pueden ser cancelados
     * @return Lista de pedidos que pueden ser cancelados
     */
    @Query("SELECT p FROM Pedido p WHERE p.estado IN ('PENDIENTE', 'CONFIRMADO')")
    List<Pedido> findPedidosQuePuedenSerCancelados();
}

