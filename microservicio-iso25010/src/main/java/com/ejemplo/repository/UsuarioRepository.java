package com.ejemplo.repository;

import com.ejemplo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Usuario
 * Proporciona operaciones CRUD y consultas personalizadas
 * 
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su email
     * @param email Email del usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Verifica si existe un usuario con el email especificado
     * @param email Email a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);

    /**
     * Busca usuarios por nombre (ignorando mayúsculas/minúsculas)
     * @param nombre Nombre a buscar
     * @return Lista de usuarios que coinciden
     */
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca usuarios por apellido (ignorando mayúsculas/minúsculas)
     * @param apellido Apellido a buscar
     * @return Lista de usuarios que coinciden
     */
    List<Usuario> findByApellidoContainingIgnoreCase(String apellido);

    /**
     * Busca usuarios por nombre o apellido (ignorando mayúsculas/minúsculas)
     * @param nombre Nombre a buscar
     * @param apellido Apellido a buscar
     * @return Lista de usuarios que coinciden
     */
    @Query("SELECT u FROM Usuario u WHERE " +
           "LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) OR " +
           "LOWER(u.apellido) LIKE LOWER(CONCAT('%', :apellido, '%'))")
    List<Usuario> findByNombreOrApellidoContainingIgnoreCase(
            @Param("nombre") String nombre, 
            @Param("apellido") String apellido);

    /**
     * Busca usuarios activos
     * @return Lista de usuarios activos
     */
    List<Usuario> findByActivoTrue();

    /**
     * Busca usuarios inactivos
     * @return Lista de usuarios inactivos
     */
    List<Usuario> findByActivoFalse();

    /**
     * Busca usuarios por estado (activo/inactivo)
     * @param activo Estado del usuario
     * @return Lista de usuarios con el estado especificado
     */
    List<Usuario> findByActivo(Boolean activo);

    /**
     * Busca usuarios por teléfono
     * @param telefono Teléfono a buscar
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> findByTelefono(String telefono);

    /**
     * Cuenta el número total de usuarios activos
     * @return Número de usuarios activos
     */
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.activo = true")
    long countUsuariosActivos();

    /**
     * Busca usuarios con pedidos
     * @return Lista de usuarios que tienen al menos un pedido
     */
    @Query("SELECT DISTINCT u FROM Usuario u JOIN u.pedidos p")
    List<Usuario> findUsuariosConPedidos();

    /**
     * Busca usuarios sin pedidos
     * @return Lista de usuarios que no tienen pedidos
     */
    @Query("SELECT u FROM Usuario u WHERE u.pedidos IS EMPTY")
    List<Usuario> findUsuariosSinPedidos();

    /**
     * Busca usuarios por texto libre (nombre, apellido o email)
     * @param texto Texto a buscar
     * @return Lista de usuarios que coinciden
     */
    @Query("SELECT u FROM Usuario u WHERE " +
           "LOWER(u.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(u.apellido) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Usuario> buscarPorTexto(@Param("texto") String texto);
}

