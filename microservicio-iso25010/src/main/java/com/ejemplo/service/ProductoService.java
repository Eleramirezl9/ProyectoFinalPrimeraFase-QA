package com.ejemplo.service;

import com.ejemplo.model.Producto;
import com.ejemplo.repository.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para la gestión de productos
 * Contiene la lógica de negocio para operaciones CRUD y consultas especializadas
 * 
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@Service
@Transactional
public class ProductoService {

    private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);

    @Autowired
    private ProductoRepository productoRepository;

    /**
     * Obtiene todos los productos
     * @return Lista de productos
     */
    @Transactional(readOnly = true)
    public List<Producto> obtenerTodos() {
        logger.debug("Obteniendo todos los productos");
        return productoRepository.findAll();
    }

    /**
     * Obtiene un producto por su ID
     * @param id ID del producto
     * @return Producto encontrado
     * @throws EntityNotFoundException si el producto no existe
     */
    @Transactional(readOnly = true)
    public Producto obtenerPorId(Long id) {
        logger.debug("Obteniendo producto por ID: {}", id);
        return productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
    }

    /**
     * Crea un nuevo producto
     * @param producto Producto a crear
     * @return Producto creado
     */
    public Producto crear(Producto producto) {
        logger.debug("Creando nuevo producto: {}", producto.getNombre());
        
        // Validaciones de negocio
        validarProducto(producto);
        
        producto.setFechaCreacion(LocalDateTime.now());
        producto.setActivo(true);
        
        Producto productoGuardado = productoRepository.save(producto);
        logger.info("Producto creado exitosamente con ID: {}", productoGuardado.getId());
        
        return productoGuardado;
    }

    /**
     * Actualiza un producto existente
     * @param id ID del producto a actualizar
     * @param producto Nuevos datos del producto
     * @return Producto actualizado
     * @throws EntityNotFoundException si el producto no existe
     */
    public Producto actualizar(Long id, Producto producto) {
        logger.debug("Actualizando producto con ID: {}", id);
        
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));

        // Validaciones de negocio
        validarProducto(producto);

        // Actualizar campos
        productoExistente.setNombre(producto.getNombre());
        productoExistente.setDescripcion(producto.getDescripcion());
        productoExistente.setPrecio(producto.getPrecio());
        productoExistente.setStock(producto.getStock());
        productoExistente.setCategoria(producto.getCategoria());
        productoExistente.setMarca(producto.getMarca());
        
        if (producto.getActivo() != null) {
            productoExistente.setActivo(producto.getActivo());
        }
        
        productoExistente.setFechaActualizacion(LocalDateTime.now());
        
        Producto productoActualizado = productoRepository.save(productoExistente);
        logger.info("Producto actualizado exitosamente con ID: {}", productoActualizado.getId());
        
        return productoActualizado;
    }

    /**
     * Elimina un producto por su ID
     * @param id ID del producto a eliminar
     * @throws EntityNotFoundException si el producto no existe
     */
    public void eliminar(Long id) {
        logger.debug("Eliminando producto con ID: {}", id);
        
        if (!productoRepository.existsById(id)) {
            throw new EntityNotFoundException("Producto no encontrado con ID: " + id);
        }
        
        productoRepository.deleteById(id);
        logger.info("Producto eliminado exitosamente con ID: {}", id);
    }

    /**
     * Busca productos por nombre
     * @param nombre Nombre a buscar
     * @return Lista de productos que coinciden
     */
    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombre(String nombre) {
        logger.debug("Buscando productos por nombre: {}", nombre);
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Busca productos por categoría
     * @param categoria Categoría a buscar
     * @return Lista de productos de la categoría
     */
    @Transactional(readOnly = true)
    public List<Producto> buscarPorCategoria(String categoria) {
        logger.debug("Buscando productos por categoría: {}", categoria);
        return productoRepository.findByCategoria(categoria);
    }

    /**
     * Busca productos por marca
     * @param marca Marca a buscar
     * @return Lista de productos de la marca
     */
    @Transactional(readOnly = true)
    public List<Producto> buscarPorMarca(String marca) {
        logger.debug("Buscando productos por marca: {}", marca);
        return productoRepository.findByMarca(marca);
    }

    /**
     * Obtiene productos activos
     * @return Lista de productos activos
     */
    @Transactional(readOnly = true)
    public List<Producto> obtenerProductosActivos() {
        logger.debug("Obteniendo productos activos");
        return productoRepository.findByActivoTrue();
    }

    /**
     * Obtiene productos con stock disponible
     * @return Lista de productos con stock
     */
    @Transactional(readOnly = true)
    public List<Producto> obtenerProductosConStock() {
        logger.debug("Obteniendo productos con stock");
        return productoRepository.findProductosConStock();
    }

    /**
     * Obtiene productos sin stock
     * @return Lista de productos sin stock
     */
    @Transactional(readOnly = true)
    public List<Producto> obtenerProductosSinStock() {
        logger.debug("Obteniendo productos sin stock");
        return productoRepository.findProductosSinStock();
    }

    /**
     * Busca productos por rango de precios
     * @param precioMinimo Precio mínimo
     * @param precioMaximo Precio máximo
     * @return Lista de productos en el rango
     */
    @Transactional(readOnly = true)
    public List<Producto> buscarPorRangoPrecios(BigDecimal precioMinimo, BigDecimal precioMaximo) {
        logger.debug("Buscando productos por rango de precios: {} - {}", precioMinimo, precioMaximo);
        return productoRepository.findByPrecioBetween(precioMinimo, precioMaximo);
    }

    /**
     * Busca productos por texto libre
     * @param texto Texto a buscar
     * @return Lista de productos que coinciden
     */
    @Transactional(readOnly = true)
    public List<Producto> buscarPorTexto(String texto) {
        logger.debug("Buscando productos por texto: {}", texto);
        return productoRepository.buscarPorTexto(texto);
    }

    /**
     * Obtiene todas las categorías disponibles
     * @return Lista de categorías únicas
     */
    @Transactional(readOnly = true)
    public List<String> obtenerCategorias() {
        logger.debug("Obteniendo todas las categorías");
        return productoRepository.findDistinctCategorias();
    }

    /**
     * Obtiene todas las marcas disponibles
     * @return Lista de marcas únicas
     */
    @Transactional(readOnly = true)
    public List<String> obtenerMarcas() {
        logger.debug("Obteniendo todas las marcas");
        return productoRepository.findDistinctMarcas();
    }

    /**
     * Actualiza el stock de un producto
     * @param id ID del producto
     * @param nuevoStock Nuevo stock
     * @return Producto actualizado
     */
    public Producto actualizarStock(Long id, Integer nuevoStock) {
        logger.debug("Actualizando stock del producto ID: {} a {}", id, nuevoStock);
        
        Producto producto = obtenerPorId(id);
        
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        
        producto.setStock(nuevoStock);
        producto.setFechaActualizacion(LocalDateTime.now());
        
        Producto productoActualizado = productoRepository.save(producto);
        logger.info("Stock actualizado exitosamente para producto ID: {}", id);
        
        return productoActualizado;
    }

    /**
     * Reduce el stock de un producto
     * @param id ID del producto
     * @param cantidad Cantidad a reducir
     * @return Producto actualizado
     */
    public Producto reducirStock(Long id, Integer cantidad) {
        logger.debug("Reduciendo stock del producto ID: {} en {}", id, cantidad);
        
        Producto producto = obtenerPorId(id);
        
        if (!producto.tieneStock(cantidad)) {
            throw new IllegalArgumentException("Stock insuficiente. Stock actual: " + producto.getStock());
        }
        
        producto.reducirStock(cantidad);
        producto.setFechaActualizacion(LocalDateTime.now());
        
        Producto productoActualizado = productoRepository.save(producto);
        logger.info("Stock reducido exitosamente para producto ID: {}", id);
        
        return productoActualizado;
    }

    /**
     * Aumenta el stock de un producto
     * @param id ID del producto
     * @param cantidad Cantidad a aumentar
     * @return Producto actualizado
     */
    public Producto aumentarStock(Long id, Integer cantidad) {
        logger.debug("Aumentando stock del producto ID: {} en {}", id, cantidad);
        
        Producto producto = obtenerPorId(id);
        
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        
        producto.aumentarStock(cantidad);
        producto.setFechaActualizacion(LocalDateTime.now());
        
        Producto productoActualizado = productoRepository.save(producto);
        logger.info("Stock aumentado exitosamente para producto ID: {}", id);
        
        return productoActualizado;
    }

    /**
     * Activa un producto
     * @param id ID del producto a activar
     * @return Producto activado
     */
    public Producto activar(Long id) {
        logger.debug("Activando producto con ID: {}", id);
        Producto producto = obtenerPorId(id);
        
        producto.setActivo(true);
        producto.setFechaActualizacion(LocalDateTime.now());
        
        Producto productoActualizado = productoRepository.save(producto);
        logger.info("Producto activado exitosamente con ID: {}", id);
        
        return productoActualizado;
    }

    /**
     * Desactiva un producto
     * @param id ID del producto a desactivar
     * @return Producto desactivado
     */
    public Producto desactivar(Long id) {
        logger.debug("Desactivando producto con ID: {}", id);
        Producto producto = obtenerPorId(id);
        
        producto.setActivo(false);
        producto.setFechaActualizacion(LocalDateTime.now());
        
        Producto productoActualizado = productoRepository.save(producto);
        logger.info("Producto desactivado exitosamente con ID: {}", id);
        
        return productoActualizado;
    }

    // Métodos de validación privados
    private void validarProducto(Producto producto) {
        if (producto.getPrecio() != null && producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        
        if (producto.getStock() != null && producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }
}

