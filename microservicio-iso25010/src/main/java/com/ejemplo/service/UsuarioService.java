package com.ejemplo.service;

import com.ejemplo.dto.UsuarioDTO;
import com.ejemplo.model.Usuario;
import com.ejemplo.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de usuarios
 * Contiene la lógica de negocio para operaciones CRUD y consultas especializadas
 * 
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@Service
@Transactional
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Obtiene todos los usuarios
     * @return Lista de usuarios DTO
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerTodos() {
        logger.debug("Obteniendo todos los usuarios");
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un usuario por su ID
     * @param id ID del usuario
     * @return Usuario DTO
     * @throws EntityNotFoundException si el usuario no existe
     */
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerPorId(Long id) {
        logger.debug("Obteniendo usuario por ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
        return convertirADTO(usuario);
    }

    /**
     * Crea un nuevo usuario
     * @param usuarioDTO Datos del usuario a crear
     * @return Usuario creado DTO
     * @throws IllegalArgumentException si el email ya existe
     */
    public UsuarioDTO crear(UsuarioDTO usuarioDTO) {
        logger.debug("Creando nuevo usuario con email: {}", usuarioDTO.getEmail());
        
        // Validar que el email no exista
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + usuarioDTO.getEmail());
        }

        Usuario usuario = convertirAEntidad(usuarioDTO);
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setActivo(true);
        
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        logger.info("Usuario creado exitosamente con ID: {}", usuarioGuardado.getId());
        
        return convertirADTO(usuarioGuardado);
    }

    /**
     * Actualiza un usuario existente
     * @param id ID del usuario a actualizar
     * @param usuarioDTO Nuevos datos del usuario
     * @return Usuario actualizado DTO
     * @throws EntityNotFoundException si el usuario no existe
     * @throws IllegalArgumentException si el email ya existe para otro usuario
     */
    public UsuarioDTO actualizar(Long id, UsuarioDTO usuarioDTO) {
        logger.debug("Actualizando usuario con ID: {}", id);
        
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        // Validar que el email no exista para otro usuario
        Optional<Usuario> usuarioConEmail = usuarioRepository.findByEmail(usuarioDTO.getEmail());
        if (usuarioConEmail.isPresent() && !usuarioConEmail.get().getId().equals(id)) {
            throw new IllegalArgumentException("Ya existe otro usuario con el email: " + usuarioDTO.getEmail());
        }

        // Actualizar campos
        usuarioExistente.setNombre(usuarioDTO.getNombre());
        usuarioExistente.setApellido(usuarioDTO.getApellido());
        usuarioExistente.setEmail(usuarioDTO.getEmail());
        usuarioExistente.setTelefono(usuarioDTO.getTelefono());
        
        if (usuarioDTO.getActivo() != null) {
            usuarioExistente.setActivo(usuarioDTO.getActivo());
        }
        
        usuarioExistente.setFechaActualizacion(LocalDateTime.now());
        
        Usuario usuarioActualizado = usuarioRepository.save(usuarioExistente);
        logger.info("Usuario actualizado exitosamente con ID: {}", usuarioActualizado.getId());
        
        return convertirADTO(usuarioActualizado);
    }

    /**
     * Elimina un usuario por su ID
     * @param id ID del usuario a eliminar
     * @throws EntityNotFoundException si el usuario no existe
     */
    public void eliminar(Long id) {
        logger.debug("Eliminando usuario con ID: {}", id);
        
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encontrado con ID: " + id);
        }
        
        usuarioRepository.deleteById(id);
        logger.info("Usuario eliminado exitosamente con ID: {}", id);
    }

    /**
     * Busca usuarios por email
     * @param email Email a buscar
     * @return Usuario DTO si existe
     * @throws EntityNotFoundException si no se encuentra
     */
    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorEmail(String email) {
        logger.debug("Buscando usuario por email: {}", email);
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con email: " + email));
        return convertirADTO(usuario);
    }

    /**
     * Busca usuarios por nombre
     * @param nombre Nombre a buscar
     * @return Lista de usuarios DTO que coinciden
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> buscarPorNombre(String nombre) {
        logger.debug("Buscando usuarios por nombre: {}", nombre);
        List<Usuario> usuarios = usuarioRepository.findByNombreContainingIgnoreCase(nombre);
        return usuarios.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene usuarios activos
     * @return Lista de usuarios activos DTO
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerUsuariosActivos() {
        logger.debug("Obteniendo usuarios activos");
        List<Usuario> usuarios = usuarioRepository.findByActivoTrue();
        return usuarios.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene usuarios inactivos
     * @return Lista de usuarios inactivos DTO
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerUsuariosInactivos() {
        logger.debug("Obteniendo usuarios inactivos");
        List<Usuario> usuarios = usuarioRepository.findByActivoFalse();
        return usuarios.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Activa un usuario
     * @param id ID del usuario a activar
     * @return Usuario activado DTO
     */
    public UsuarioDTO activar(Long id) {
        logger.debug("Activando usuario con ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
        
        usuario.setActivo(true);
        usuario.setFechaActualizacion(LocalDateTime.now());
        
        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        logger.info("Usuario activado exitosamente con ID: {}", id);
        
        return convertirADTO(usuarioActualizado);
    }

    /**
     * Desactiva un usuario
     * @param id ID del usuario a desactivar
     * @return Usuario desactivado DTO
     */
    public UsuarioDTO desactivar(Long id) {
        logger.debug("Desactivando usuario con ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
        
        usuario.setActivo(false);
        usuario.setFechaActualizacion(LocalDateTime.now());
        
        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        logger.info("Usuario desactivado exitosamente con ID: {}", id);
        
        return convertirADTO(usuarioActualizado);
    }

    /**
     * Busca usuarios por texto libre
     * @param texto Texto a buscar en nombre, apellido o email
     * @return Lista de usuarios DTO que coinciden
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> buscarPorTexto(String texto) {
        logger.debug("Buscando usuarios por texto: {}", texto);
        List<Usuario> usuarios = usuarioRepository.buscarPorTexto(texto);
        return usuarios.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene el conteo de usuarios activos
     * @return Número de usuarios activos
     */
    @Transactional(readOnly = true)
    public long contarUsuariosActivos() {
        return usuarioRepository.countUsuariosActivos();
    }

    // Métodos de conversión
    private UsuarioDTO convertirADTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setTelefono(usuario.getTelefono());
        dto.setActivo(usuario.getActivo());
        dto.setFechaCreacion(usuario.getFechaCreacion());
        dto.setFechaActualizacion(usuario.getFechaActualizacion());
        dto.setNombreCompleto(usuario.getNombreCompleto());
        
        // Agregar información adicional
        if (usuario.getPedidos() != null) {
            dto.setTotalPedidos(usuario.getPedidos().size());
        }
        
        return dto;
    }

    private Usuario convertirAEntidad(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        
        if (dto.getActivo() != null) {
            usuario.setActivo(dto.getActivo());
        }
        
        return usuario;
    }
}

