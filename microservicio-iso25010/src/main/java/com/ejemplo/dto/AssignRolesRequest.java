package com.ejemplo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

/**
 * DTO para asignar roles a un usuario
 * Solo administradores pueden usar este endpoint
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
public class AssignRolesRequest {

    @NotNull(message = "La lista de roles no puede ser nula")
    @NotEmpty(message = "Debe especificar al menos un rol")
    private Set<String> roles;

    public AssignRolesRequest() {
    }

    public AssignRolesRequest(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "AssignRolesRequest{" +
                "roles=" + roles +
                '}';
    }
}
