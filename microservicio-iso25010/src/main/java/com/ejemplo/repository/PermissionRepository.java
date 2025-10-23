package com.ejemplo.repository;

import com.ejemplo.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Permission
 * Proporciona métodos para acceder y manipular permisos en la base de datos
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    /**
     * Busca un permiso por su nombre
     *
     * @param name nombre del permiso
     * @return Optional con el permiso si existe
     */
    Optional<Permission> findByName(String name);

    /**
     * Verifica si existe un permiso con el nombre especificado
     *
     * @param name nombre del permiso
     * @return true si existe, false si no
     */
    boolean existsByName(String name);
}
