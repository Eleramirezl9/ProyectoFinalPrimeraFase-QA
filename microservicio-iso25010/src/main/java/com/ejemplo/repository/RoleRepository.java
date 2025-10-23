package com.ejemplo.repository;

import com.ejemplo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Role
 * Proporciona métodos para acceder y manipular roles en la base de datos
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Busca un rol por su nombre
     *
     * @param name nombre del rol
     * @return Optional con el rol si existe
     */
    Optional<Role> findByName(String name);

    /**
     * Verifica si existe un rol con el nombre especificado
     *
     * @param name nombre del rol
     * @return true si existe, false si no
     */
    boolean existsByName(String name);

    /**
     * Busca un rol con sus permisos cargados
     *
     * @param name nombre del rol
     * @return Optional con el rol y sus permisos
     */
    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.permissions WHERE r.name = :name")
    Optional<Role> findByNameWithPermissions(String name);
}
