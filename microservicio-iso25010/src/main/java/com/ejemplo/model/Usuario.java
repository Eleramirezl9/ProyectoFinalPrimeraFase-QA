package com.ejemplo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Entidad Usuario para el sistema de gestión
 * Representa los usuarios del sistema con sus datos básicos
 * Implementa UserDetails para integración con Spring Security
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 2.0.0
 */
@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @NotBlank(message = "El username es obligatorio")
    @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "El password es obligatorio")
    @JsonIgnore
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Size(min = 8, max = 15, message = "El teléfono debe tener entre 8 y 15 caracteres")
    @Column(name = "telefono", length = 15)
    private String telefono;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "cuenta_no_expirada", nullable = false)
    private Boolean cuentaNoExpirada = true;

    @Column(name = "cuenta_no_bloqueada", nullable = false)
    private Boolean cuentaNoBloqueada = true;

    @Column(name = "credenciales_no_expiradas", nullable = false)
    private Boolean credencialesNoExpiradas = true;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Pedido> pedidos = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "usuario_roles",
        joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();

    // Constructores
    public Usuario() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public Usuario(String nombre, String apellido, String username, String email, String password) {
        this();
        this.nombre = nombre;
        this.apellido = apellido;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Métodos de ciclo de vida
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    // Implementación de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        // Agregar roles como autoridades con prefijo ROLE_
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));

            // Agregar permisos como autoridades
            for (Permission permission : role.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return cuentaNoExpirada;
    }

    @Override
    public boolean isAccountNonLocked() {
        return cuentaNoBloqueada;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credencialesNoExpiradas;
    }

    @Override
    public boolean isEnabled() {
        return activo;
    }

    // Getters y Setters adicionales
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getCuentaNoExpirada() {
        return cuentaNoExpirada;
    }

    public void setCuentaNoExpirada(Boolean cuentaNoExpirada) {
        this.cuentaNoExpirada = cuentaNoExpirada;
    }

    public Boolean getCuentaNoBloqueada() {
        return cuentaNoBloqueada;
    }

    public void setCuentaNoBloqueada(Boolean cuentaNoBloqueada) {
        this.cuentaNoBloqueada = cuentaNoBloqueada;
    }

    public Boolean getCredencialesNoExpiradas() {
        return credencialesNoExpiradas;
    }

    public void setCredencialesNoExpiradas(Boolean credencialesNoExpiradas) {
        this.credencialesNoExpiradas = credencialesNoExpiradas;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    // Métodos de utilidad
    public String getNombreCompleto() {
        return this.nombre + " " + this.apellido;
    }

    public void addRole(Role role) {
        this.roles.add(role);
        role.getUsuarios().add(this);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
        role.getUsuarios().remove(this);
    }

    public boolean hasRole(String roleName) {
        return roles.stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }

    public boolean hasPermission(String permissionName) {
        return roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .anyMatch(permission -> permission.getName().equals(permissionName));
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", activo=" + activo +
                ", roles=" + roles.stream().map(Role::getName).collect(Collectors.toList()) +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}

