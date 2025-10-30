package com.ejemplo.service;

import com.ejemplo.dto.AuthResponse;
import com.ejemplo.dto.LoginRequest;
import com.ejemplo.dto.RegisterRequest;
import com.ejemplo.model.Role;
import com.ejemplo.model.Usuario;
import com.ejemplo.repository.RoleRepository;
import com.ejemplo.repository.UsuarioRepository;
import com.ejemplo.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para AuthService
 * Valida registro, login y generación de tokens JWT
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService - Pruebas Unitarias")
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private Usuario usuario;
    private Role clienteRole;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        // Configurar rol de cliente
        clienteRole = new Role();
        clienteRole.setId(1L);
        clienteRole.setName("CLIENTE");

        // Configurar usuario
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setUsername("jperez");
        usuario.setEmail("juan@example.com");
        usuario.setPassword("$2a$10$encodedPassword");
        usuario.setTelefono("12345678");
        usuario.setActivo(true);
        usuario.setCuentaNoExpirada(true);
        usuario.setCuentaNoBloqueada(true);
        usuario.setCredencialesNoExpiradas(true);

        Set<Role> roles = new HashSet<>();
        roles.add(clienteRole);
        usuario.setRoles(roles);

        // Configurar request de registro
        registerRequest = new RegisterRequest();
        registerRequest.setNombre("Juan");
        registerRequest.setApellido("Pérez");
        registerRequest.setUsername("jperez");
        registerRequest.setEmail("juan@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setTelefono("12345678");

        // Configurar request de login
        loginRequest = new LoginRequest();
        loginRequest.setUsername("jperez");
        loginRequest.setPassword("password123");
    }

    // ==================== Tests para register ====================

    @Test
    @DisplayName("register() - Debe registrar usuario exitosamente")
    void testRegister_Success() {
        // Arrange
        when(usuarioRepository.existsByUsername("jperez")).thenReturn(false);
        when(usuarioRepository.existsByEmail("juan@example.com")).thenReturn(false);
        when(roleRepository.findByName("CLIENTE")).thenReturn(Optional.of(clienteRole));
        when(passwordEncoder.encode("password123")).thenReturn("$2a$10$encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(jwtService.generateToken(any(Usuario.class))).thenReturn("jwt.token.here");
        when(jwtService.generateRefreshToken(any(Usuario.class))).thenReturn("refresh.token.here");
        when(jwtService.getExpirationTime()).thenReturn(3600000L);

        // Act
        AuthResponse response = authService.register(registerRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("jwt.token.here");
        assertThat(response.getRefreshToken()).isEqualTo("refresh.token.here");
        assertThat(response.getUsername()).isEqualTo("jperez");
        assertThat(response.getEmail()).isEqualTo("juan@example.com");
        assertThat(response.getNombre()).isEqualTo("Juan");
        assertThat(response.getApellido()).isEqualTo("Pérez");
        assertThat(response.getRoles()).contains("CLIENTE");
        assertThat(response.getExpiresIn()).isEqualTo(3600000L);

        verify(usuarioRepository, times(1)).existsByUsername("jperez");
        verify(usuarioRepository, times(1)).existsByEmail("juan@example.com");
        verify(roleRepository, times(1)).findByName("CLIENTE");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(jwtService, times(1)).generateToken(any(Usuario.class));
        verify(jwtService, times(1)).generateRefreshToken(any(Usuario.class));
    }

    @Test
    @DisplayName("register() - Debe fallar cuando username ya existe")
    void testRegister_UsernameExists() {
        // Arrange
        when(usuarioRepository.existsByUsername("jperez")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El username ya está en uso");

        verify(usuarioRepository, times(1)).existsByUsername("jperez");
        verify(usuarioRepository, never()).existsByEmail(any());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("register() - Debe fallar cuando email ya existe")
    void testRegister_EmailExists() {
        // Arrange
        when(usuarioRepository.existsByUsername("jperez")).thenReturn(false);
        when(usuarioRepository.existsByEmail("juan@example.com")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El email ya está registrado");

        verify(usuarioRepository, times(1)).existsByUsername("jperez");
        verify(usuarioRepository, times(1)).existsByEmail("juan@example.com");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("register() - Debe fallar cuando rol CLIENTE no existe")
    void testRegister_RoleNotFound() {
        // Arrange
        when(usuarioRepository.existsByUsername("jperez")).thenReturn(false);
        when(usuarioRepository.existsByEmail("juan@example.com")).thenReturn(false);
        when(roleRepository.findByName("CLIENTE")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error: Rol CLIENTE no encontrado");

        verify(usuarioRepository, times(1)).existsByUsername("jperez");
        verify(usuarioRepository, times(1)).existsByEmail("juan@example.com");
        verify(roleRepository, times(1)).findByName("CLIENTE");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("register() - Debe encriptar la contraseña")
    void testRegister_PasswordEncryption() {
        // Arrange
        when(usuarioRepository.existsByUsername("jperez")).thenReturn(false);
        when(usuarioRepository.existsByEmail("juan@example.com")).thenReturn(false);
        when(roleRepository.findByName("CLIENTE")).thenReturn(Optional.of(clienteRole));
        when(passwordEncoder.encode("password123")).thenReturn("$2a$10$encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(jwtService.generateToken(any(Usuario.class))).thenReturn("jwt.token.here");
        when(jwtService.generateRefreshToken(any(Usuario.class))).thenReturn("refresh.token.here");
        when(jwtService.getExpirationTime()).thenReturn(3600000L);

        // Act
        authService.register(registerRequest);

        // Assert
        verify(passwordEncoder, times(1)).encode("password123");
    }

    @Test
    @DisplayName("register() - Debe asignar rol CLIENTE por defecto")
    void testRegister_DefaultRole() {
        // Arrange
        when(usuarioRepository.existsByUsername("jperez")).thenReturn(false);
        when(usuarioRepository.existsByEmail("juan@example.com")).thenReturn(false);
        when(roleRepository.findByName("CLIENTE")).thenReturn(Optional.of(clienteRole));
        when(passwordEncoder.encode("password123")).thenReturn("$2a$10$encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(jwtService.generateToken(any(Usuario.class))).thenReturn("jwt.token.here");
        when(jwtService.generateRefreshToken(any(Usuario.class))).thenReturn("refresh.token.here");
        when(jwtService.getExpirationTime()).thenReturn(3600000L);

        // Act
        AuthResponse response = authService.register(registerRequest);

        // Assert
        assertThat(response.getRoles()).contains("CLIENTE");
        verify(roleRepository, times(1)).findByName("CLIENTE");
    }

    // ==================== Tests para login ====================

    @Test
    @DisplayName("login() - Debe autenticar usuario exitosamente")
    void testLogin_Success() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(usuarioRepository.findByUsername("jperez")).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(usuario)).thenReturn("jwt.token.here");
        when(jwtService.generateRefreshToken(usuario)).thenReturn("refresh.token.here");
        when(jwtService.getExpirationTime()).thenReturn(3600000L);

        // Act
        AuthResponse response = authService.login(loginRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("jwt.token.here");
        assertThat(response.getRefreshToken()).isEqualTo("refresh.token.here");
        assertThat(response.getUsername()).isEqualTo("jperez");
        assertThat(response.getEmail()).isEqualTo("juan@example.com");
        assertThat(response.getNombre()).isEqualTo("Juan");
        assertThat(response.getApellido()).isEqualTo("Pérez");
        assertThat(response.getRoles()).contains("CLIENTE");

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(usuarioRepository, times(1)).findByUsername("jperez");
        verify(jwtService, times(1)).generateToken(usuario);
        verify(jwtService, times(1)).generateRefreshToken(usuario);
    }

    @Test
    @DisplayName("login() - Debe fallar con credenciales inválidas")
    void testLogin_InvalidCredentials() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // Act & Assert
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Bad credentials");

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(usuarioRepository, never()).findByUsername(any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("login() - Debe fallar cuando usuario no existe después de autenticación")
    void testLogin_UserNotFoundAfterAuth() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(usuarioRepository.findByUsername("jperez")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(usuarioRepository, times(1)).findByUsername("jperez");
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("login() - Debe generar tokens JWT y refresh")
    void testLogin_GeneratesTokens() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(usuarioRepository.findByUsername("jperez")).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(usuario)).thenReturn("jwt.token.here");
        when(jwtService.generateRefreshToken(usuario)).thenReturn("refresh.token.here");
        when(jwtService.getExpirationTime()).thenReturn(3600000L);

        // Act
        AuthResponse response = authService.login(loginRequest);

        // Assert
        assertThat(response.getToken()).isNotNull();
        assertThat(response.getRefreshToken()).isNotNull();
        verify(jwtService, times(1)).generateToken(usuario);
        verify(jwtService, times(1)).generateRefreshToken(usuario);
    }

    @Test
    @DisplayName("login() - Debe incluir roles del usuario")
    void testLogin_IncludesUserRoles() {
        // Arrange
        Role adminRole = new Role();
        adminRole.setId(2L);
        adminRole.setName("ADMIN");

        Set<Role> roles = new HashSet<>();
        roles.add(clienteRole);
        roles.add(adminRole);
        usuario.setRoles(roles);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(usuarioRepository.findByUsername("jperez")).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(usuario)).thenReturn("jwt.token.here");
        when(jwtService.generateRefreshToken(usuario)).thenReturn("refresh.token.here");
        when(jwtService.getExpirationTime()).thenReturn(3600000L);

        // Act
        AuthResponse response = authService.login(loginRequest);

        // Assert
        assertThat(response.getRoles()).hasSize(2);
        assertThat(response.getRoles()).contains("CLIENTE", "ADMIN");
    }

    @Test
    @DisplayName("login() - Debe retornar tiempo de expiración correcto")
    void testLogin_ExpirationTime() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(usuarioRepository.findByUsername("jperez")).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(usuario)).thenReturn("jwt.token.here");
        when(jwtService.generateRefreshToken(usuario)).thenReturn("refresh.token.here");
        when(jwtService.getExpirationTime()).thenReturn(3600000L);

        // Act
        AuthResponse response = authService.login(loginRequest);

        // Assert
        assertThat(response.getExpiresIn()).isEqualTo(3600000L);
        verify(jwtService, times(1)).getExpirationTime();
    }

    // ==================== Tests de integración entre register y login ====================

    @Test
    @DisplayName("register() y login() - Debe poder registrar y luego hacer login")
    void testRegisterAndLogin_Integration() {
        // Arrange - Register
        when(usuarioRepository.existsByUsername("jperez")).thenReturn(false);
        when(usuarioRepository.existsByEmail("juan@example.com")).thenReturn(false);
        when(roleRepository.findByName("CLIENTE")).thenReturn(Optional.of(clienteRole));
        when(passwordEncoder.encode("password123")).thenReturn("$2a$10$encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(jwtService.generateToken(any(Usuario.class))).thenReturn("jwt.token.register");
        when(jwtService.generateRefreshToken(any(Usuario.class))).thenReturn("refresh.token.register");
        when(jwtService.getExpirationTime()).thenReturn(3600000L);

        // Act - Register
        AuthResponse registerResponse = authService.register(registerRequest);

        // Assert - Register
        assertThat(registerResponse).isNotNull();
        assertThat(registerResponse.getUsername()).isEqualTo("jperez");

        // Arrange - Login
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(usuarioRepository.findByUsername("jperez")).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(usuario)).thenReturn("jwt.token.login");
        when(jwtService.generateRefreshToken(usuario)).thenReturn("refresh.token.login");

        // Act - Login
        AuthResponse loginResponse = authService.login(loginRequest);

        // Assert - Login
        assertThat(loginResponse).isNotNull();
        assertThat(loginResponse.getUsername()).isEqualTo("jperez");
        assertThat(loginResponse.getToken()).isEqualTo("jwt.token.login");
    }
}
