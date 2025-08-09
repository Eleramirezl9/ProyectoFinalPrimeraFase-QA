package com.ejemplo.repository;

import com.ejemplo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repositorio para la entidad Producto
 * Proporciona operaciones CRUD y consultas personalizadas
 * 
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Busca productos por nombre (ignorando mayúsculas/minúsculas)
     * @param nombre Nombre a buscar
     * @return Lista de productos que coinciden
     */
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca productos por categoría
     * @param categoria Categoría a buscar
     * @return Lista de productos de la categoría
     */
    List<Producto> findByCategoria(String categoria);

    /**
     * Busca productos por marca
     * @param marca Marca a buscar
     * @return Lista de productos de la marca
     */
    List<Producto> findByMarca(String marca);

    /**
     * Busca productos activos
     * @return Lista de productos activos
     */
    List<Producto> findByActivoTrue();

    /**
     * Busca productos inactivos
     * @return Lista de productos inactivos
     */
    List<Producto> findByActivoFalse();

    /**
     * Busca productos por estado (activo/inactivo)
     * @param activo Estado del producto
     * @return Lista de productos con el estado especificado
     */
    List<Producto> findByActivo(Boolean activo);

    /**
     * Busca productos con stock disponible
     * @return Lista de productos con stock > 0
     */
    @Query("SELECT p FROM Producto p WHERE p.stock > 0")
    List<Producto> findProductosConStock();

    /**
     * Busca productos sin stock
     * @return Lista de productos con stock = 0
     */
    @Query("SELECT p FROM Producto p WHERE p.stock = 0")
    List<Producto> findProductosSinStock();

    /**
     * Busca productos con stock menor al mínimo especificado
     * @param stockMinimo Stock mínimo
     * @return Lista de productos con stock bajo
     */
    @Query("SELECT p FROM Producto p WHERE p.stock < :stockMinimo")
    List<Producto> findProductosConStockBajo(@Param("stockMinimo") Integer stockMinimo);

    /**
     * Busca productos por rango de precios
     * @param precioMinimo Precio mínimo
     * @param precioMaximo Precio máximo
     * @return Lista de productos en el rango de precios
     */
    @Query("SELECT p FROM Producto p WHERE p.precio BETWEEN :precioMinimo AND :precioMaximo")
    List<Producto> findByPrecioBetween(
            @Param("precioMinimo") BigDecimal precioMinimo, 
            @Param("precioMaximo") BigDecimal precioMaximo);

    /**
     * Busca productos con precio menor al especificado
     * @param precio Precio máximo
     * @return Lista de productos con precio menor
     */
    List<Producto> findByPrecioLessThan(BigDecimal precio);

    /**
     * Busca productos con precio mayor al especificado
     * @param precio Precio mínimo
     * @return Lista de productos con precio mayor
     */
    List<Producto> findByPrecioGreaterThan(BigDecimal precio);

    /**
     * Busca productos por categoría y que estén activos
     * @param categoria Categoría a buscar
     * @return Lista de productos activos de la categoría
     */
    List<Producto> findByCategoriaAndActivoTrue(String categoria);

    /**
     * Busca productos por marca y que estén activos
     * @param marca Marca a buscar
     * @return Lista de productos activos de la marca
     */
    List<Producto> findByMarcaAndActivoTrue(String marca);

    /**
     * Busca todas las categorías distintas
     * @return Lista de categorías únicas
     */
    @Query("SELECT DISTINCT p.categoria FROM Producto p WHERE p.categoria IS NOT NULL ORDER BY p.categoria")
    List<String> findDistinctCategorias();

    /**
     * Busca todas las marcas distintas
     * @return Lista de marcas únicas
     */
    @Query("SELECT DISTINCT p.marca FROM Producto p WHERE p.marca IS NOT NULL ORDER BY p.marca")
    List<String> findDistinctMarcas();

    /**
     * Cuenta productos por categoría
     * @param categoria Categoría a contar
     * @return Número de productos en la categoría
     */
    long countByCategoria(String categoria);

    /**
     * Cuenta productos por marca
     * @param marca Marca a contar
     * @return Número de productos de la marca
     */
    long countByMarca(String marca);

    /**
     * Busca productos por texto libre (nombre, descripción, categoría o marca)
     * @param texto Texto a buscar
     * @return Lista de productos que coinciden
     */
    @Query("SELECT p FROM Producto p WHERE " +
           "LOWER(p.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(p.categoria) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(p.marca) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Producto> buscarPorTexto(@Param("texto") String texto);

    /**
     * Busca productos más vendidos (con más pedidos)
     * @return Lista de productos ordenados por número de pedidos descendente
     */
    @Query("SELECT p FROM Producto p LEFT JOIN Pedido pe ON p.id = pe.producto.id " +
           "GROUP BY p.id ORDER BY COUNT(pe.id) DESC")
    List<Producto> findProductosMasVendidos();
}

