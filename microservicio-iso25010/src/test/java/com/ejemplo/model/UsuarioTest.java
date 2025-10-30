package com.ejemplo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la entidad Usuario
 * Valida lógica de roles, permisos y métodos de negocio
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@DisplayName("Usuario - Pruebas Unitarias")
class UsuarioTest {

    private Usuario usuario;
    private Role roleCliente;
    private Role roleAdmin;
    private Permission permissionRead;
    private Permission permissionWrite;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("juan.perez@example.com");
        usuario.setPassword("$2a$10$encodedPassword");
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setEmail("juan.perez@example.com");
        usuario.setTelefono("12345678");
        usuario.setActivo(true);
        usuario.setRoles(new HashSet<>());

        roleCliente = new Role("ROLE_CLIENTE");
        roleCliente.setUsuarios(new HashSet<>());
        roleCliente.setPermissions(new HashSet<>());

        roleAdmin = new Role("ROLE_ADMIN");
        roleAdmin.setUsuarios(new HashSet<>());
        roleAdmin.setPermissions(new HashSet<>());

        permissionRead = new Permission("READ");
        permissionRead.setRoles(new HashSet<>());

        permissionWrite = new Permission("WRITE");
        permissionWrite.setRoles(new HashSet<>());
    }

    @Test
    @DisplayName("Debe obtener nombre completo")
    void testGetNombreCompleto() {
        // Act
        String nombreCompleto = usuario.getNombreCompleto();

        // Assert
        assertEquals("Juan Pérez", nombreCompleto);
    }

    @Test
    @DisplayName("Debe agregar rol correctamente")
    void testAddRole() {
        // Act
        usuario.addRole(roleCliente);

        // Assert
        assertTrue(usuario.getRoles().contains(roleCliente));
        assertTrue(roleCliente.getUsuarios().contains(usuario));
    }

    @Test
    @DisplayName("Debe agregar múltiples roles")
    void testAddMultipleRoles() {
        // Act
        usuario.addRole(roleCliente);
        usuario.addRole(roleAdmin);

        // Assert
        assertEquals(2, usuario.getRoles().size());
        assertTrue(usuario.getRoles().contains(roleCliente));
        assertTrue(usuario.getRoles().contains(roleAdmin));
    }

    @Test
    @DisplayName("Debe remover rol correctamente")
    void testRemoveRole() {
        // Arrange
        usuario.addRole(roleCliente);

        // Act
        usuario.removeRole(roleCliente);

        // Assert
        assertFalse(usuario.getRoles().contains(roleCliente));
        assertFalse(roleCliente.getUsuarios().contains(usuario));
    }

    @Test
    @DisplayName("Debe verificar si tiene rol específico")
    void testHasRole() {
        // Arrange
        usuario.addRole(roleCliente);

        // Act & Assert
        assertTrue(usuario.hasRole("ROLE_CLIENTE"));
    }

    @Test
    @DisplayName("Debe verificar que no tiene rol específico")
    void testDoesNotHaveRole() {
        // Arrange
        usuario.addRole(roleCliente);

        // Act & Assert
        assertFalse(usuario.hasRole("ROLE_ADMIN"));
    }

    @Test
    @DisplayName("Debe verificar si tiene permiso específico")
    void testHasPermission() {
        // Arrange
        roleCliente.getPermissions().add(permissionRead);
        usuario.addRole(roleCliente);

        // Act & Assert
        assertTrue(usuario.hasPermission("READ"));
    }

    @Test
    @DisplayName("Debe verificar que no tiene permiso específico")
    void testDoesNotHavePermission() {
        // Arrange
        roleCliente.getPermissions().add(permissionRead);
        usuario.addRole(roleCliente);

        // Act & Assert
        assertFalse(usuario.hasPermission("WRITE"));
    }

    @Test
    @DisplayName("Debe verificar permisos de múltiples roles")
    void testHasPermissionFromMultipleRoles() {
        // Arrange
        roleCliente.getPermissions().add(permissionRead);
        roleAdmin.getPermissions().add(permissionWrite);
        usuario.addRole(roleCliente);
        usuario.addRole(roleAdmin);

        // Act & Assert
        assertTrue(usuario.hasPermission("READ"));
        assertTrue(usuario.hasPermission("WRITE"));
    }

    @Test
    @DisplayName("Debe generar toString correctamente")
    void testToString() {
        // Arrange
        usuario.addRole(roleCliente);

        // Act
        String resultado = usuario.toString();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.contains("id=1"));
        assertTrue(resultado.contains("username='juan.perez@example.com'"));
        assertTrue(resultado.contains("nombre='Juan'"));
        assertTrue(resultado.contains("apellido='Pérez'"));
        assertTrue(resultado.contains("email='juan.perez@example.com'"));
        assertTrue(resultado.contains("activo=true"));
    }

    @Test
    @DisplayName("Debe manejar usuario sin roles")
    void testUsuarioSinRoles() {
        // Act & Assert
        assertTrue(usuario.getRoles().isEmpty());
        assertFalse(usuario.hasRole("ROLE_CLIENTE"));
        assertFalse(usuario.hasPermission("READ"));
    }

    @Test
    @DisplayName("Debe manejar rol sin permisos")
    void testRolSinPermisos() {
        // Arrange
        usuario.addRole(roleCliente);

        // Act & Assert
        assertTrue(usuario.hasRole("ROLE_CLIENTE"));
        assertFalse(usuario.hasPermission("READ"));
    }

    @Test
    @DisplayName("Debe establecer y obtener todos los campos correctamente")
    void testGettersSetters() {
        // Arrange
        Usuario nuevoUsuario = new Usuario();

        // Act
        nuevoUsuario.setId(2L);
        nuevoUsuario.setUsername("maria.garcia@example.com");
        nuevoUsuario.setPassword("password123");
        nuevoUsuario.setNombre("María");
        nuevoUsuario.setApellido("García");
        nuevoUsuario.setEmail("maria.garcia@example.com");
        nuevoUsuario.setTelefono("87654321");
        nuevoUsuario.setActivo(false);
        nuevoUsuario.setCuentaNoBloqueada(true);
        nuevoUsuario.setCredencialesNoExpiradas(true);

        // Assert
        assertEquals(2L, nuevoUsuario.getId());
        assertEquals("maria.garcia@example.com", nuevoUsuario.getUsername());
        assertEquals("password123", nuevoUsuario.getPassword());
        assertEquals("María", nuevoUsuario.getNombre());
        assertEquals("García", nuevoUsuario.getApellido());
        assertEquals("maria.garcia@example.com", nuevoUsuario.getEmail());
        assertEquals("87654321", nuevoUsuario.getTelefono());
        assertFalse(nuevoUsuario.getActivo());
        assertTrue(nuevoUsuario.getCuentaNoBloqueada());
        assertTrue(nuevoUsuario.getCredencialesNoExpiradas());
    }

    @Test
    @DisplayName("Debe manejar nombre completo con apellido vacío")
    void testNombreCompletoApellidoVacio() {
        // Arrange
        usuario.setApellido("");

        // Act
        String nombreCompleto = usuario.getNombreCompleto();

        // Assert
        assertEquals("Juan ", nombreCompleto);
    }

    @Test
    @DisplayName("Debe manejar nombre completo con nombre vacío")
    void testNombreCompletoNombreVacio() {
        // Arrange
        usuario.setNombre("");

        // Act
        String nombreCompleto = usuario.getNombreCompleto();

        // Assert
        assertEquals(" Pérez", nombreCompleto);
    }

    @Test
    @DisplayName("No debe duplicar rol al agregar dos veces")
    void testNoDuplicarRol() {
        // Act
        usuario.addRole(roleCliente);
        usuario.addRole(roleCliente);

        // Assert
        // Sets no permiten duplicados
        assertEquals(1, usuario.getRoles().size());
    }
}
