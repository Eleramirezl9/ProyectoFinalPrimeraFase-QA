package com.ejemplo.controller;

import com.ejemplo.dto.AuthResponse;
import com.ejemplo.dto.LoginRequest;
import com.ejemplo.dto.RegisterRequest;
import com.ejemplo.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración para AuthController
 * Valida endpoints de registro y autenticación
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("AuthController - Pruebas de Integración")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
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

        // Configurar response de autenticación
        authResponse = new AuthResponse(
                "jwt.token.here",
                "refresh.token.here",
                1L,
                "jperez",
                "juan@example.com",
                "Juan",
                "Pérez",
                Arrays.asList("CLIENTE"),
                3600000L
        );
    }

    // ==================== Tests para POST /auth/register ====================

    @Test
    @DisplayName("POST /auth/register - Debe registrar usuario exitosamente")
    void testRegister_Success() throws Exception {
        // Arrange
        when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", is("jwt.token.here")))
                .andExpect(jsonPath("$.refreshToken", is("refresh.token.here")))
                .andExpect(jsonPath("$.username", is("jperez")))
                .andExpect(jsonPath("$.email", is("juan@example.com")))
                .andExpect(jsonPath("$.nombre", is("Juan")))
                .andExpect(jsonPath("$.apellido", is("Pérez")))
                .andExpect(jsonPath("$.roles", hasSize(1)))
                .andExpect(jsonPath("$.roles[0]", is("CLIENTE")))
                .andExpect(jsonPath("$.expiresIn", is(3600000)));

        verify(authService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    @DisplayName("POST /auth/register - Debe fallar con datos inválidos (nombre vacío)")
    void testRegister_InvalidData_EmptyName() throws Exception {
        // Arrange
        registerRequest.setNombre("");

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(any(RegisterRequest.class));
    }

    @Test
    @DisplayName("POST /auth/register - Debe fallar con nombre muy corto")
    void testRegister_InvalidData_ShortName() throws Exception {
        // Arrange
        registerRequest.setNombre("J");

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(any(RegisterRequest.class));
    }

    @Test
    @DisplayName("POST /auth/register - Debe fallar con email inválido")
    void testRegister_InvalidData_InvalidEmail() throws Exception {
        // Arrange
        registerRequest.setEmail("email-invalido");

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(any(RegisterRequest.class));
    }

    @Test
    @DisplayName("POST /auth/register - Debe fallar con password muy corta")
    void testRegister_InvalidData_ShortPassword() throws Exception {
        // Arrange
        registerRequest.setPassword("123");

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(any(RegisterRequest.class));
    }

    @Test
    @DisplayName("POST /auth/register - Debe fallar con username muy corto")
    void testRegister_InvalidData_ShortUsername() throws Exception {
        // Arrange
        registerRequest.setUsername("ab");

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(any(RegisterRequest.class));
    }

    @Test
    @DisplayName("POST /auth/register - Debe fallar cuando username ya existe")
    void testRegister_UsernameExists() throws Exception {
        // Arrange
        when(authService.register(any(RegisterRequest.class)))
                .thenThrow(new IllegalArgumentException("El username ya está en uso"));

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    @DisplayName("POST /auth/register - Debe fallar cuando email ya existe")
    void testRegister_EmailExists() throws Exception {
        // Arrange
        when(authService.register(any(RegisterRequest.class)))
                .thenThrow(new IllegalArgumentException("El email ya está registrado"));

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    @DisplayName("POST /auth/register - Debe fallar sin nombre")
    void testRegister_MissingName() throws Exception {
        // Arrange
        registerRequest.setNombre(null);

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(any(RegisterRequest.class));
    }

    @Test
    @DisplayName("POST /auth/register - Debe fallar sin email")
    void testRegister_MissingEmail() throws Exception {
        // Arrange
        registerRequest.setEmail(null);

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(any(RegisterRequest.class));
    }

    @Test
    @DisplayName("POST /auth/register - Debe fallar sin password")
    void testRegister_MissingPassword() throws Exception {
        // Arrange
        registerRequest.setPassword(null);

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(any(RegisterRequest.class));
    }

    // ==================== Tests para POST /auth/login ====================

    @Test
    @DisplayName("POST /auth/login - Debe autenticar usuario exitosamente")
    void testLogin_Success() throws Exception {
        // Arrange
        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("jwt.token.here")))
                .andExpect(jsonPath("$.refreshToken", is("refresh.token.here")))
                .andExpect(jsonPath("$.username", is("jperez")))
                .andExpect(jsonPath("$.email", is("juan@example.com")))
                .andExpect(jsonPath("$.roles", hasSize(1)))
                .andExpect(jsonPath("$.roles[0]", is("CLIENTE")));

        verify(authService, times(1)).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("POST /auth/login - Debe fallar con credenciales inválidas")
    void testLogin_InvalidCredentials() throws Exception {
        // Arrange
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());

        verify(authService, times(1)).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("POST /auth/login - Debe fallar sin username")
    void testLogin_MissingUsername() throws Exception {
        // Arrange
        loginRequest.setUsername(null);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("POST /auth/login - Debe fallar sin password")
    void testLogin_MissingPassword() throws Exception {
        // Arrange
        loginRequest.setPassword(null);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("POST /auth/login - Debe fallar con username vacío")
    void testLogin_EmptyUsername() throws Exception {
        // Arrange
        loginRequest.setUsername("");

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("POST /auth/login - Debe fallar con password vacía")
    void testLogin_EmptyPassword() throws Exception {
        // Arrange
        loginRequest.setPassword("");

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("POST /auth/login - Debe fallar con body vacío")
    void testLogin_EmptyBody() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());

        verify(authService, never()).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("POST /auth/login - Debe fallar con JSON malformado")
    void testLogin_MalformedJson() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json}"))
                .andExpect(status().isBadRequest());

        verify(authService, never()).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("POST /auth/login - Debe retornar usuario no encontrado")
    void testLogin_UserNotFound() throws Exception {
        // Arrange
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("Usuario no encontrado"));

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isInternalServerError());

        verify(authService, times(1)).login(any(LoginRequest.class));
    }

    // ==================== Tests de validación de respuesta ====================

    @Test
    @DisplayName("POST /auth/register - Debe incluir todos los campos en respuesta")
    void testRegister_ResponseStructure() throws Exception {
        // Arrange
        when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.nombre").exists())
                .andExpect(jsonPath("$.apellido").exists())
                .andExpect(jsonPath("$.roles").exists())
                .andExpect(jsonPath("$.expiresIn").exists());

        verify(authService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    @DisplayName("POST /auth/login - Debe incluir todos los campos en respuesta")
    void testLogin_ResponseStructure() throws Exception {
        // Arrange
        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.nombre").exists())
                .andExpect(jsonPath("$.apellido").exists())
                .andExpect(jsonPath("$.roles").exists())
                .andExpect(jsonPath("$.expiresIn").exists());

        verify(authService, times(1)).login(any(LoginRequest.class));
    }
}
