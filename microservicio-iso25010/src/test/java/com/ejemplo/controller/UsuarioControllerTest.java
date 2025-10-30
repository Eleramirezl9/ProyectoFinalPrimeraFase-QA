package com.ejemplo.controller;

import com.ejemplo.dto.AssignRolesRequest;
import com.ejemplo.dto.UsuarioDTO;
import com.ejemplo.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración para UsuarioController
 * Utiliza MockMvc con contexto completo de Spring Boot
 * Spring Security configurado con usuarios mock
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("UsuarioController - Pruebas de Integración")
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNombre("Juan");
        usuarioDTO.setApellido("Pérez");
        usuarioDTO.setEmail("juan.perez@example.com");
        usuarioDTO.setTelefono("555-1234");
        usuarioDTO.setActivo(true);
        usuarioDTO.setFechaCreacion(LocalDateTime.now());
    }

    // ==================== Tests para GET /usuarios ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /usuarios - Debe retornar lista de usuarios")
    void testObtenerTodos_Success() throws Exception {
        // Arrange
        when(usuarioService.obtenerTodos()).thenReturn(Arrays.asList(usuarioDTO, usuarioDTO));

        // Act & Assert
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Juan")));

        verify(usuarioService, times(1)).obtenerTodos();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /usuarios - Debe retornar lista vacía")
    void testObtenerTodos_EmptyList() throws Exception {
        // Arrange
        when(usuarioService.obtenerTodos()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(usuarioService, times(1)).obtenerTodos();
    }

    // ==================== Tests para GET /usuarios/{id} ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /usuarios/{id} - Debe retornar usuario por ID")
    void testObtenerPorId_Success() throws Exception {
        // Arrange
        when(usuarioService.obtenerPorId(1L)).thenReturn(usuarioDTO);

        // Act & Assert
        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Juan")))
                .andExpect(jsonPath("$.email", is("juan.perez@example.com")));

        verify(usuarioService, times(1)).obtenerPorId(1L);
    }

    // ==================== Tests para POST /usuarios ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /usuarios - Debe crear usuario exitosamente")
    void testCrear_Success() throws Exception {
        // Arrange
        when(usuarioService.crear(any(UsuarioDTO.class))).thenReturn(usuarioDTO);

        // Act & Assert
        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Juan")));

        verify(usuarioService, times(1)).crear(any(UsuarioDTO.class));
    }

    // ==================== Tests para PUT /usuarios/{id} ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /usuarios/{id} - Debe actualizar usuario")
    void testActualizar_Success() throws Exception {
        // Arrange
        when(usuarioService.actualizar(eq(1L), any(UsuarioDTO.class))).thenReturn(usuarioDTO);

        // Act & Assert
        mockMvc.perform(put("/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Juan")));

        verify(usuarioService, times(1)).actualizar(eq(1L), any(UsuarioDTO.class));
    }

    // ==================== Tests para DELETE /usuarios/{id} ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /usuarios/{id} - Debe eliminar usuario")
    void testEliminar_Success() throws Exception {
        // Arrange
        doNothing().when(usuarioService).eliminar(1L);

        // Act & Assert
        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).eliminar(1L);
    }

    // ==================== Tests para GET /usuarios/buscar ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /usuarios/buscar - Debe buscar por nombre")
    void testBuscarPorNombre_Success() throws Exception {
        // Arrange
        when(usuarioService.buscarPorNombre(anyString())).thenReturn(Arrays.asList(usuarioDTO));

        // Act & Assert
        mockMvc.perform(get("/usuarios/buscar")
                .param("nombre", "Juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(usuarioService, times(1)).buscarPorNombre("Juan");
    }

    // ==================== Tests para GET /usuarios/email ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /usuarios/email - Debe buscar por email")
    void testBuscarPorEmail_Success() throws Exception {
        // Arrange
        when(usuarioService.buscarPorEmail(anyString())).thenReturn(usuarioDTO);

        // Act & Assert
        mockMvc.perform(get("/usuarios/email/{email}", "juan.perez@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("juan.perez@example.com")));

        verify(usuarioService, times(1)).buscarPorEmail("juan.perez@example.com");
    }

    // ==================== Tests para GET /usuarios/activos ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /usuarios/activos - Debe retornar usuarios activos")
    void testObtenerUsuariosActivos_Success() throws Exception {
        // Arrange
        when(usuarioService.obtenerUsuariosActivos()).thenReturn(Arrays.asList(usuarioDTO));

        // Act & Assert
        mockMvc.perform(get("/usuarios/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(usuarioService, times(1)).obtenerUsuariosActivos();
    }

    // ==================== Tests para GET /usuarios/inactivos ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /usuarios/inactivos - Debe retornar usuarios inactivos")
    void testObtenerUsuariosInactivos_Success() throws Exception {
        // Arrange
        when(usuarioService.obtenerUsuariosInactivos()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/usuarios/inactivos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(usuarioService, times(1)).obtenerUsuariosInactivos();
    }

    // ==================== Tests para GET /usuarios/buscar-texto ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /usuarios/buscar-texto - Debe buscar por texto libre")
    void testBuscarPorTexto_Success() throws Exception {
        // Arrange
        when(usuarioService.buscarPorTexto(anyString())).thenReturn(Arrays.asList(usuarioDTO));

        // Act & Assert
        mockMvc.perform(get("/usuarios/buscar-texto")
                .param("texto", "Juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(usuarioService, times(1)).buscarPorTexto("Juan");
    }

    // ==================== Tests para PATCH /usuarios/{id}/activar ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PATCH /usuarios/{id}/activar - Debe activar usuario")
    void testActivar_Success() throws Exception {
        // Arrange
        when(usuarioService.activar(1L)).thenReturn(usuarioDTO);

        // Act & Assert
        mockMvc.perform(patch("/usuarios/1/activar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo", is(true)));

        verify(usuarioService, times(1)).activar(1L);
    }

    // ==================== Tests para PATCH /usuarios/{id}/desactivar ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PATCH /usuarios/{id}/desactivar - Debe desactivar usuario")
    void testDesactivar_Success() throws Exception {
        // Arrange
        usuarioDTO.setActivo(false);
        when(usuarioService.desactivar(1L)).thenReturn(usuarioDTO);

        // Act & Assert
        mockMvc.perform(patch("/usuarios/1/desactivar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo", is(false)));

        verify(usuarioService, times(1)).desactivar(1L);
    }

    // ==================== Tests para PATCH /usuarios/{id}/roles ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PATCH /usuarios/{id}/roles - Debe asignar roles")
    void testAsignarRoles_Success() throws Exception {
        // Arrange
        AssignRolesRequest request = new AssignRolesRequest();
        request.setRoles(new HashSet<>(Arrays.asList("ADMIN", "CLIENTE")));
        when(usuarioService.asignarRoles(eq(1L), any(AssignRolesRequest.class))).thenReturn(usuarioDTO);

        // Act & Assert
        mockMvc.perform(patch("/usuarios/1/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Juan")));

        verify(usuarioService, times(1)).asignarRoles(eq(1L), any(AssignRolesRequest.class));
    }

    // ==================== Tests de Validación y Excepciones ====================

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /usuarios - Debe fallar con email duplicado")
    void testCrear_EmailDuplicado() throws Exception {
        // Arrange
        when(usuarioService.crear(any(UsuarioDTO.class)))
                .thenThrow(new IllegalArgumentException("Ya existe un usuario con el email"));

        // Act & Assert
        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isBadRequest());

        verify(usuarioService, times(1)).crear(any(UsuarioDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /usuarios/{id} - Debe retornar 404 cuando no existe")
    void testObtenerPorId_NotFound() throws Exception {
        // Arrange
        when(usuarioService.obtenerPorId(999L))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Usuario no encontrado"));

        // Act & Assert
        mockMvc.perform(get("/usuarios/999"))
                .andExpect(status().isNotFound());

        verify(usuarioService, times(1)).obtenerPorId(999L);
    }
}
