package com.ejemplo.controller;

import com.ejemplo.model.Producto;
import com.ejemplo.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controlador REST para la gestión de productos
 * Proporciona endpoints para operaciones CRUD y consultas especializadas
 * 
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@RestController
@RequestMapping("/productos")
@Tag(name = "Productos", description = "API para la gestión del catálogo de productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoService productoService;

    /**
     * Obtiene todos los productos
     */
    @GetMapping
    @Operation(summary = "Obtener todos los productos", 
               description = "Retorna una lista con todos los productos del catálogo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Producto>> obtenerTodos() {
        logger.info("GET /productos - Obteniendo todos los productos");
        List<Producto> productos = productoService.obtenerTodos();
        logger.info("Se encontraron {} productos", productos.size());
        return ResponseEntity.ok(productos);
    }

    /**
     * Obtiene un producto por su ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", 
               description = "Retorna un producto específico basado en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Producto> obtenerPorId(
            @Parameter(description = "ID único del producto", required = true, example = "1")
            @PathVariable Long id) {
        logger.info("GET /productos/{} - Obteniendo producto por ID", id);
        Producto producto = productoService.obtenerPorId(id);
        logger.info("Producto encontrado: {}", producto.getNombre());
        return ResponseEntity.ok(producto);
    }

    /**
     * Crea un nuevo producto
     */
    @PostMapping
    @Operation(summary = "Crear nuevo producto", 
               description = "Crea un nuevo producto en el catálogo con los datos proporcionados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Producto> crear(
            @Parameter(description = "Datos del producto a crear", required = true)
            @Valid @RequestBody Producto producto) {
        logger.info("POST /productos - Creando nuevo producto: {}", producto.getNombre());
        Producto productoCreado = productoService.crear(producto);
        logger.info("Producto creado exitosamente con ID: {}", productoCreado.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(productoCreado);
    }

    /**
     * Actualiza un producto existente
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", 
               description = "Actualiza los datos de un producto existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Producto> actualizar(
            @Parameter(description = "ID único del producto", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos del producto", required = true)
            @Valid @RequestBody Producto producto) {
        logger.info("PUT /productos/{} - Actualizando producto", id);
        Producto productoActualizado = productoService.actualizar(id, producto);
        logger.info("Producto actualizado exitosamente: {}", productoActualizado.getNombre());
        return ResponseEntity.ok(productoActualizado);
    }

    /**
     * Elimina un producto
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", 
               description = "Elimina un producto del catálogo de forma permanente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID único del producto", required = true, example = "1")
            @PathVariable Long id) {
        logger.info("DELETE /productos/{} - Eliminando producto", id);
        productoService.eliminar(id);
        logger.info("Producto eliminado exitosamente con ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca productos por nombre
     */
    @GetMapping("/buscar")
    @Operation(summary = "Buscar productos por nombre", 
               description = "Busca productos que contengan el texto especificado en su nombre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "400", description = "Parámetro de búsqueda inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Producto>> buscarPorNombre(
            @Parameter(description = "Nombre a buscar", required = true, example = "Laptop")
            @RequestParam String nombre) {
        logger.info("GET /productos/buscar?nombre={} - Buscando productos por nombre", nombre);
        List<Producto> productos = productoService.buscarPorNombre(nombre);
        logger.info("Se encontraron {} productos con nombre: {}", productos.size(), nombre);
        return ResponseEntity.ok(productos);
    }

    /**
     * Busca productos por categoría
     */
    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar productos por categoría", 
               description = "Retorna todos los productos de una categoría específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Productos de la categoría obtenidos",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Producto>> buscarPorCategoria(
            @Parameter(description = "Categoría del producto", required = true, example = "Electrónicos")
            @PathVariable String categoria) {
        logger.info("GET /productos/categoria/{} - Buscando productos por categoría", categoria);
        List<Producto> productos = productoService.buscarPorCategoria(categoria);
        logger.info("Se encontraron {} productos en categoría: {}", productos.size(), categoria);
        return ResponseEntity.ok(productos);
    }

    /**
     * Busca productos por marca
     */
    @GetMapping("/marca/{marca}")
    @Operation(summary = "Buscar productos por marca", 
               description = "Retorna todos los productos de una marca específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Productos de la marca obtenidos",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Producto>> buscarPorMarca(
            @Parameter(description = "Marca del producto", required = true, example = "Samsung")
            @PathVariable String marca) {
        logger.info("GET /productos/marca/{} - Buscando productos por marca", marca);
        List<Producto> productos = productoService.buscarPorMarca(marca);
        logger.info("Se encontraron {} productos de marca: {}", productos.size(), marca);
        return ResponseEntity.ok(productos);
    }

    /**
     * Obtiene productos activos
     */
    @GetMapping("/activos")
    @Operation(summary = "Obtener productos activos", 
               description = "Retorna todos los productos que están activos en el catálogo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos activos obtenida",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Producto>> obtenerProductosActivos() {
        logger.info("GET /productos/activos - Obteniendo productos activos");
        List<Producto> productos = productoService.obtenerProductosActivos();
        logger.info("Se encontraron {} productos activos", productos.size());
        return ResponseEntity.ok(productos);
    }

    /**
     * Obtiene productos con stock
     */
    @GetMapping("/con-stock")
    @Operation(summary = "Obtener productos con stock", 
               description = "Retorna todos los productos que tienen stock disponible")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos con stock obtenida",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Producto>> obtenerProductosConStock() {
        logger.info("GET /productos/con-stock - Obteniendo productos con stock");
        List<Producto> productos = productoService.obtenerProductosConStock();
        logger.info("Se encontraron {} productos con stock", productos.size());
        return ResponseEntity.ok(productos);
    }

    /**
     * Obtiene productos sin stock
     */
    @GetMapping("/sin-stock")
    @Operation(summary = "Obtener productos sin stock", 
               description = "Retorna todos los productos que no tienen stock disponible")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos sin stock obtenida",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Producto>> obtenerProductosSinStock() {
        logger.info("GET /productos/sin-stock - Obteniendo productos sin stock");
        List<Producto> productos = productoService.obtenerProductosSinStock();
        logger.info("Se encontraron {} productos sin stock", productos.size());
        return ResponseEntity.ok(productos);
    }

    /**
     * Busca productos por rango de precios
     */
    @GetMapping("/precio")
    @Operation(summary = "Buscar productos por rango de precios", 
               description = "Busca productos dentro de un rango de precios específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda por precio realizada exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "400", description = "Parámetros de precio inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Producto>> buscarPorRangoPrecios(
            @Parameter(description = "Precio mínimo", required = true, example = "100.00")
            @RequestParam BigDecimal precioMinimo,
            @Parameter(description = "Precio máximo", required = true, example = "1000.00")
            @RequestParam BigDecimal precioMaximo) {
        logger.info("GET /productos/precio?precioMinimo={}&precioMaximo={} - Buscando por rango de precios", 
                   precioMinimo, precioMaximo);
        List<Producto> productos = productoService.buscarPorRangoPrecios(precioMinimo, precioMaximo);
        logger.info("Se encontraron {} productos en rango de precios", productos.size());
        return ResponseEntity.ok(productos);
    }

    /**
     * Búsqueda libre de productos
     */
    @GetMapping("/buscar-texto")
    @Operation(summary = "Búsqueda libre de productos", 
               description = "Busca productos por texto libre en nombre, descripción, categoría o marca")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "400", description = "Parámetro de búsqueda inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Producto>> buscarPorTexto(
            @Parameter(description = "Texto a buscar", required = true, example = "smartphone")
            @RequestParam String texto) {
        logger.info("GET /productos/buscar-texto?texto={} - Búsqueda libre de productos", texto);
        List<Producto> productos = productoService.buscarPorTexto(texto);
        logger.info("Se encontraron {} productos con texto: {}", productos.size(), texto);
        return ResponseEntity.ok(productos);
    }

    /**
     * Obtiene todas las categorías
     */
    @GetMapping("/categorias")
    @Operation(summary = "Obtener todas las categorías", 
               description = "Retorna una lista con todas las categorías de productos disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<String>> obtenerCategorias() {
        logger.info("GET /productos/categorias - Obteniendo todas las categorías");
        List<String> categorias = productoService.obtenerCategorias();
        logger.info("Se encontraron {} categorías", categorias.size());
        return ResponseEntity.ok(categorias);
    }

    /**
     * Obtiene todas las marcas
     */
    @GetMapping("/marcas")
    @Operation(summary = "Obtener todas las marcas", 
               description = "Retorna una lista con todas las marcas de productos disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de marcas obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<String>> obtenerMarcas() {
        logger.info("GET /productos/marcas - Obteniendo todas las marcas");
        List<String> marcas = productoService.obtenerMarcas();
        logger.info("Se encontraron {} marcas", marcas.size());
        return ResponseEntity.ok(marcas);
    }

    /**
     * Actualiza el stock de un producto
     */
    @PatchMapping("/{id}/stock")
    @Operation(summary = "Actualizar stock de producto", 
               description = "Actualiza la cantidad de stock disponible de un producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock actualizado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "400", description = "Stock inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Producto> actualizarStock(
            @Parameter(description = "ID único del producto", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nuevo stock", required = true, example = "50")
            @RequestParam Integer stock) {
        logger.info("PATCH /productos/{}/stock?stock={} - Actualizando stock", id, stock);
        Producto producto = productoService.actualizarStock(id, stock);
        logger.info("Stock actualizado para producto: {}", producto.getNombre());
        return ResponseEntity.ok(producto);
    }

    /**
     * Activa un producto
     */
    @PatchMapping("/{id}/activar")
    @Operation(summary = "Activar producto", 
               description = "Activa un producto que estaba previamente inactivo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto activado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Producto> activar(
            @Parameter(description = "ID único del producto", required = true, example = "1")
            @PathVariable Long id) {
        logger.info("PATCH /productos/{}/activar - Activando producto", id);
        Producto producto = productoService.activar(id);
        logger.info("Producto activado exitosamente: {}", producto.getNombre());
        return ResponseEntity.ok(producto);
    }

    /**
     * Desactiva un producto
     */
    @PatchMapping("/{id}/desactivar")
    @Operation(summary = "Desactivar producto", 
               description = "Desactiva un producto sin eliminarlo del catálogo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto desactivado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Producto> desactivar(
            @Parameter(description = "ID único del producto", required = true, example = "1")
            @PathVariable Long id) {
        logger.info("PATCH /productos/{}/desactivar - Desactivando producto", id);
        Producto producto = productoService.desactivar(id);
        logger.info("Producto desactivado exitosamente: {}", producto.getNombre());
        return ResponseEntity.ok(producto);
    }
}

