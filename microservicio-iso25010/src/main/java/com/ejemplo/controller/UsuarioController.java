package com.ejemplo.controller;

import com.ejemplo.dto.AssignRolesRequest;
import com.ejemplo.dto.UsuarioDTO;
import com.ejemplo.service.UsuarioService;
import com.ejemplo.util.LogSanitizer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de usuarios
 * Proporciona endpoints para operaciones CRUD y consultas especializadas
 * 
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "API para la gestión de usuarios del sistema")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Record para estadísticas de usuarios
     */
    public record EstadisticasUsuarios(
        long total,
        long activos,
        long inactivos,
        double porcentajeActivos
    ) {}

    /**
     * Obtiene todos los usuarios
     */
    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", 
               description = "Retorna una lista con todos los usuarios registrados en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<UsuarioDTO>> obtenerTodos() {
        logger.info("GET /usuarios - Obteniendo todos los usuarios");
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodos();
        logger.info("Se encontraron {} usuarios", usuarios.size());
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Obtiene un usuario por su ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", 
               description = "Retorna un usuario específico basado en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<UsuarioDTO> obtenerPorId(
            @Parameter(description = "ID único del usuario", required = true, example = "1")
            @PathVariable Long id) {
        logger.info("GET /usuarios/{} - Obteniendo usuario por ID", id);
        UsuarioDTO usuario = usuarioService.obtenerPorId(id);
        if (logger.isInfoEnabled()) {
            logger.info("Usuario encontrado: {}", LogSanitizer.sanitize(usuario.getEmail()));
        }
        return ResponseEntity.ok(usuario);
    }

    /**
     * Crea un nuevo usuario
     */
    @PostMapping
    @Operation(summary = "Crear nuevo usuario", 
               description = "Crea un nuevo usuario en el sistema con los datos proporcionados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o email ya existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<UsuarioDTO> crear(
            @Parameter(description = "Datos del usuario a crear", required = true)
            @Valid @RequestBody UsuarioDTO usuarioDTO) {
        if (logger.isInfoEnabled()) {
            logger.info("POST /usuarios - Creando nuevo usuario con email: {}", LogSanitizer.sanitize(usuarioDTO.getEmail()));
        }
        UsuarioDTO usuarioCreado = usuarioService.crear(usuarioDTO);
        logger.info("Usuario creado exitosamente con ID: {}", usuarioCreado.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
    }

    /**
     * Actualiza un usuario existente
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", 
               description = "Actualiza los datos de un usuario existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o email ya existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<UsuarioDTO> actualizar(
            @Parameter(description = "ID único del usuario", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos del usuario", required = true)
            @Valid @RequestBody UsuarioDTO usuarioDTO) {
        logger.info("PUT /usuarios/{} - Actualizando usuario", id);
        UsuarioDTO usuarioActualizado = usuarioService.actualizar(id, usuarioDTO);
        if (logger.isInfoEnabled()) {
            logger.info("Usuario actualizado exitosamente: {}", LogSanitizer.sanitize(usuarioActualizado.getEmail()));
        }
        return ResponseEntity.ok(usuarioActualizado);
    }

    /**
     * Elimina un usuario
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", 
               description = "Elimina un usuario del sistema de forma permanente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID único del usuario", required = true, example = "1")
            @PathVariable Long id) {
        logger.info("DELETE /usuarios/{} - Eliminando usuario", id);
        usuarioService.eliminar(id);
        logger.info("Usuario eliminado exitosamente con ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca usuarios por email
     */
    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar usuario por email", 
               description = "Busca un usuario específico por su dirección de email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<UsuarioDTO> buscarPorEmail(
            @Parameter(description = "Email del usuario", required = true, example = "usuario@ejemplo.com")
            @PathVariable @Email(message = "El formato del email no es válido")
            @NotBlank(message = "El email no puede estar vacío") String email) {
        if (logger.isInfoEnabled()) {
            logger.info("GET /usuarios/email/{} - Buscando usuario por email", LogSanitizer.sanitize(email));
        }
        UsuarioDTO usuario = usuarioService.buscarPorEmail(email);
        return ResponseEntity.ok(usuario);
    }

    /**
     * Busca usuarios por nombre
     */
    @GetMapping("/buscar")
    @Operation(summary = "Buscar usuarios por nombre", 
               description = "Busca usuarios que contengan el texto especificado en su nombre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "400", description = "Parámetro de búsqueda inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<UsuarioDTO>> buscarPorNombre(
            @Parameter(description = "Nombre a buscar", required = true, example = "Juan")
            @RequestParam @NotBlank(message = "El nombre a buscar no puede estar vacío")
            @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres") String nombre) {
        if (logger.isInfoEnabled()) {
            logger.info("GET /usuarios/buscar?nombre={} - Buscando usuarios por nombre", LogSanitizer.sanitize(nombre));
        }
        List<UsuarioDTO> usuarios = usuarioService.buscarPorNombre(nombre);
        if (logger.isInfoEnabled()) {
            logger.info("Se encontraron {} usuarios con nombre: {}", usuarios.size(), LogSanitizer.sanitize(nombre));
        }
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Obtiene usuarios activos
     */
    @GetMapping("/activos")
    @Operation(summary = "Obtener usuarios activos", 
               description = "Retorna todos los usuarios que están activos en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios activos obtenida",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<UsuarioDTO>> obtenerUsuariosActivos() {
        logger.info("GET /usuarios/activos - Obteniendo usuarios activos");
        List<UsuarioDTO> usuarios = usuarioService.obtenerUsuariosActivos();
        logger.info("Se encontraron {} usuarios activos", usuarios.size());
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Obtiene usuarios inactivos
     */
    @GetMapping("/inactivos")
    @Operation(summary = "Obtener usuarios inactivos", 
               description = "Retorna todos los usuarios que están inactivos en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios inactivos obtenida",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<UsuarioDTO>> obtenerUsuariosInactivos() {
        logger.info("GET /usuarios/inactivos - Obteniendo usuarios inactivos");
        List<UsuarioDTO> usuarios = usuarioService.obtenerUsuariosInactivos();
        logger.info("Se encontraron {} usuarios inactivos", usuarios.size());
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Activa un usuario
     */
    @PatchMapping("/{id}/activar")
    @Operation(summary = "Activar usuario", 
               description = "Activa un usuario que estaba previamente inactivo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario activado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<UsuarioDTO> activar(
            @Parameter(description = "ID único del usuario", required = true, example = "1")
            @PathVariable Long id) {
        logger.info("PATCH /usuarios/{}/activar - Activando usuario", id);
        UsuarioDTO usuario = usuarioService.activar(id);
        if (logger.isInfoEnabled()) {
            logger.info("Usuario activado exitosamente: {}", LogSanitizer.sanitize(usuario.getEmail()));
        }
        return ResponseEntity.ok(usuario);
    }

    /**
     * Desactiva un usuario
     */
    @PatchMapping("/{id}/desactivar")
    @Operation(summary = "Desactivar usuario", 
               description = "Desactiva un usuario sin eliminarlo del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario desactivado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<UsuarioDTO> desactivar(
            @Parameter(description = "ID único del usuario", required = true, example = "1")
            @PathVariable Long id) {
        logger.info("PATCH /usuarios/{}/desactivar - Desactivando usuario", id);
        UsuarioDTO usuario = usuarioService.desactivar(id);
        if (logger.isInfoEnabled()) {
            logger.info("Usuario desactivado exitosamente: {}", LogSanitizer.sanitize(usuario.getEmail()));
        }
        return ResponseEntity.ok(usuario);
    }

    /**
     * Búsqueda libre de usuarios
     */
    @GetMapping("/buscar-texto")
    @Operation(summary = "Búsqueda libre de usuarios", 
               description = "Busca usuarios por texto libre en nombre, apellido o email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "400", description = "Parámetro de búsqueda inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<UsuarioDTO>> buscarPorTexto(
            @Parameter(description = "Texto a buscar", required = true, example = "juan@ejemplo.com")
            @RequestParam String texto) {
        if (logger.isInfoEnabled()) {
            logger.info("GET /usuarios/buscar-texto?texto={} - Búsqueda libre de usuarios", LogSanitizer.sanitize(texto));
        }
        List<UsuarioDTO> usuarios = usuarioService.buscarPorTexto(texto);
        if (logger.isInfoEnabled()) {
            logger.info("Se encontraron {} usuarios con texto: {}", usuarios.size(), LogSanitizer.sanitize(texto));
        }
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Obtiene estadísticas de usuarios
     */
    @GetMapping("/estadisticas")
    @Operation(summary = "Obtener estadísticas de usuarios", 
               description = "Retorna estadísticas básicas sobre los usuarios del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EstadisticasUsuarios> obtenerEstadisticas() {
        logger.info("GET /usuarios/estadisticas - Obteniendo estadísticas de usuarios");
        
        long totalUsuarios = usuarioService.obtenerTodos().size();
        long usuariosActivos = usuarioService.contarUsuariosActivos();
        long usuariosInactivos = totalUsuarios - usuariosActivos;
        double porcentajeActivos = totalUsuarios > 0 ? (double) usuariosActivos / totalUsuarios * 100 : 0;
        
        EstadisticasUsuarios estadisticas = new EstadisticasUsuarios(
            totalUsuarios,
            usuariosActivos, 
            usuariosInactivos,
            porcentajeActivos
        );
        
        logger.info("Estadísticas: Total={}, Activos={}, Inactivos={}", totalUsuarios, usuariosActivos, usuariosInactivos);
        return ResponseEntity.ok(estadisticas);
    }

    /**
     * Asigna roles a un usuario
     * Solo ADMIN puede ejecutar esta operación
     */
    @PatchMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Asignar roles a usuario",
               description = "Asigna uno o más roles a un usuario específico. Solo administradores pueden ejecutar esta operación.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Roles asignados exitosamente",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "400", description = "Roles inválidos o datos incorrectos"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Solo ADMIN"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<UsuarioDTO> asignarRoles(
            @Parameter(description = "ID único del usuario", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Lista de roles a asignar (ADMIN, MANAGER, CLIENTE)", required = true)
            @Valid @RequestBody AssignRolesRequest request) {
        logger.info("PATCH /usuarios/{}/roles - Asignando roles: {}", id, request.getRoles());
        UsuarioDTO usuario = usuarioService.asignarRoles(id, request);
        logger.info("Roles asignados exitosamente al usuario ID: {}", id);
        return ResponseEntity.ok(usuario);
    }
}

