package com.ejemplo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad Permission para el sistema de seguridad
 * Representa los permisos específicos que se pueden asignar a los roles
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@Entity
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del permiso es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre del permiso debe tener entre 3 y 100 caracteres")
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    @Column(name = "description", length = 255)
    private String description;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    @JsonIgnore  // Evita ciclo infinito de serialización JSON
    private Set<Role> roles = new HashSet<>();

    // Constructores
    public Permission() {
    }

    public Permission(String name) {
        this.name = name;
    }

    public Permission(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission)) return false;
        Permission that = (Permission) o;
        return name != null && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
