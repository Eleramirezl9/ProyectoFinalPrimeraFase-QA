package com.ejemplo.security;

import com.ejemplo.model.Role;
import com.ejemplo.model.Usuario;
import com.ejemplo.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para CustomUserDetailsService
 * Valida la carga de usuarios desde la base de datos
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CustomUserDetailsService - Pruebas Unitarias")
class CustomUserDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("juan.perez@example.com");
        usuario.setPassword("$2a$10$encodedPassword");
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.addRole(new Role("ROLE_CLIENTE"));
        usuario.setActivo(true);
    }

    @Test
    @DisplayName("Debe cargar usuario por username exitosamente")
    void testLoadUserByUsername_Success() {
        // Arrange
        when(usuarioRepository.findByUsername("juan.perez@example.com"))
                .thenReturn(Optional.of(usuario));

        // Act
        UserDetails result = customUserDetailsService.loadUserByUsername("juan.perez@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("juan.perez@example.com", result.getUsername());
        assertEquals("$2a$10$encodedPassword", result.getPassword());
        assertFalse(result.getAuthorities().isEmpty());

        verify(usuarioRepository, times(1)).findByUsername("juan.perez@example.com");
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando usuario no existe")
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        when(usuarioRepository.findByUsername("noexiste@example.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("noexiste@example.com")
        );

        assertTrue(exception.getMessage().contains("Usuario no encontrado con username: noexiste@example.com"));
        verify(usuarioRepository, times(1)).findByUsername("noexiste@example.com");
    }

    @Test
    @DisplayName("Debe cargar usuario ADMIN correctamente")
    void testLoadUserByUsername_AdminUser() {
        // Arrange
        Usuario adminUser = new Usuario();
        adminUser.setId(2L);
        adminUser.setUsername("admin@example.com");
        adminUser.setPassword("$2a$10$encodedPassword");
        adminUser.setNombre("Admin");
        adminUser.setApellido("User");
        adminUser.addRole(new Role("ROLE_ADMIN"));
        adminUser.setActivo(true);

        when(usuarioRepository.findByUsername("admin@example.com"))
                .thenReturn(Optional.of(adminUser));

        // Act
        UserDetails result = customUserDetailsService.loadUserByUsername("admin@example.com");

        // Assert
        assertNotNull(result);
        assertFalse(result.getAuthorities().isEmpty());

        verify(usuarioRepository, times(1)).findByUsername("admin@example.com");
    }

    @Test
    @DisplayName("Debe cargar usuario inactivo pero con detalles correctos")
    void testLoadUserByUsername_InactiveUser() {
        // Arrange
        usuario.setActivo(false);
        when(usuarioRepository.findByUsername("juan.perez@example.com"))
                .thenReturn(Optional.of(usuario));

        // Act
        UserDetails result = customUserDetailsService.loadUserByUsername("juan.perez@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("juan.perez@example.com", result.getUsername());
        // El usuario inactivo puede ser cargado, pero estará deshabilitado
        assertFalse(result.isEnabled());

        verify(usuarioRepository, times(1)).findByUsername("juan.perez@example.com");
    }

    @Test
    @DisplayName("Debe manejar username con espacios")
    void testLoadUserByUsername_WithSpaces() {
        // Arrange
        String usernameWithSpaces = " juan.perez@example.com ";
        when(usuarioRepository.findByUsername(usernameWithSpaces))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(usernameWithSpaces)
        );

        verify(usuarioRepository, times(1)).findByUsername(usernameWithSpaces);
    }
}
