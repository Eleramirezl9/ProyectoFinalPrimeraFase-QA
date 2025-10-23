package com.ejemplo.dto;

import java.util.List;

/**
 * DTO para respuesta de autenticación
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
public class AuthResponse {

    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String nombre;
    private String apellido;
    private List<String> roles;
    private Long expiresIn; // en milisegundos

    // Constructores
    public AuthResponse() {
    }

    public AuthResponse(String token, Long id, String username, String email,
                        String nombre, String apellido, List<String> roles, Long expiresIn) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.roles = roles;
        this.expiresIn = expiresIn;
    }

    public AuthResponse(String token, String refreshToken, Long id, String username, String email,
                        String nombre, String apellido, List<String> roles, Long expiresIn) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.roles = roles;
        this.expiresIn = expiresIn;
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
