package com.ejemplo.service;

import com.ejemplo.dto.AssignRolesRequest;
import com.ejemplo.dto.UsuarioDTO;
import com.ejemplo.model.Role;
import com.ejemplo.model.Usuario;
import com.ejemplo.repository.RoleRepository;
import com.ejemplo.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para UsuarioService
 * Valida la lógica de negocio para operaciones CRUD de usuarios
 * Cobertura objetivo: 80%+
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioService - Pruebas Unitarias")
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioMock;
    private UsuarioDTO usuarioDTOMock;
    private Role roleMock;

    @BeforeEach
    void setUp() {
        // Setup Usuario mock
        usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setNombre("Juan");
        usuarioMock.setApellido("Pérez");
        usuarioMock.setEmail("juan.perez@example.com");
        usuarioMock.setTelefono("555-1234");
        usuarioMock.setActivo(true);
        usuarioMock.setFechaCreacion(LocalDateTime.now());

        // Setup UsuarioDTO mock
        usuarioDTOMock = new UsuarioDTO();
        usuarioDTOMock.setNombre("Juan");
        usuarioDTOMock.setApellido("Pérez");
        usuarioDTOMock.setEmail("juan.perez@example.com");
        usuarioDTOMock.setTelefono("555-1234");

        // Setup Role mock
        roleMock = new Role();
        roleMock.setId(1L);
        roleMock.setName("CLIENTE");
    }

    // ==================== Tests para obtenerTodos ====================
    @Test
    @DisplayName("obtenerTodos - Debe retornar lista de todos los usuarios")
    void testObtenerTodos_Success() {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(usuarioMock, new Usuario());
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // Act
        List<UsuarioDTO> resultado = usuarioService.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodos - Debe retornar lista vacía cuando no hay usuarios")
    void testObtenerTodos_EmptyList() {
        // Arrange
        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<UsuarioDTO> resultado = usuarioService.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(usuarioRepository, times(1)).findAll();
    }

    // ==================== Tests para obtenerPorId ====================
    @Test
    @DisplayName("obtenerPorId - Debe retornar usuario cuando existe")
    void testObtenerPorId_Success() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));

        // Act
        UsuarioDTO resultado = usuarioService.obtenerPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        assertEquals("juan.perez@example.com", resultado.getEmail());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorId - Debe lanzar excepción cuando usuario no existe")
    void testObtenerPorId_NotFound() {
        // Arrange
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> usuarioService.obtenerPorId(999L));
        verify(usuarioRepository, times(1)).findById(999L);
    }

    // ==================== Tests para crear ====================
    @Test
    @DisplayName("crear - Debe crear usuario exitosamente")
    void testCrear_Success() {
        // Arrange
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMock);

        // Act
        UsuarioDTO resultado = usuarioService.crear(usuarioDTOMock);

        // Assert
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        assertEquals("juan.perez@example.com", resultado.getEmail());
        verify(usuarioRepository, times(1)).existsByEmail(usuarioDTOMock.getEmail());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("crear - Debe lanzar excepción cuando email ya existe")
    void testCrear_EmailExists() {
        // Arrange
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> usuarioService.crear(usuarioDTOMock));
        verify(usuarioRepository, times(1)).existsByEmail(usuarioDTOMock.getEmail());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    // ==================== Tests para actualizar ====================
    @Test
    @DisplayName("actualizar - Debe actualizar usuario exitosamente")
    void testActualizar_Success() {
        // Arrange
        usuarioDTOMock.setNombre("Juan Carlos");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMock);

        // Act
        UsuarioDTO resultado = usuarioService.actualizar(1L, usuarioDTOMock);

        // Assert
        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("actualizar - Debe lanzar excepción cuando usuario no existe")
    void testActualizar_NotFound() {
        // Arrange
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> usuarioService.actualizar(999L, usuarioDTOMock));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("actualizar - Debe lanzar excepción cuando email existe para otro usuario")
    void testActualizar_EmailExistsForOther() {
        // Arrange
        Usuario otroUsuario = new Usuario();
        otroUsuario.setId(2L);
        otroUsuario.setEmail("juan.perez@example.com");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(otroUsuario));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> usuarioService.actualizar(1L, usuarioDTOMock));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    // ==================== Tests para eliminar ====================
    @Test
    @DisplayName("eliminar - Debe eliminar usuario exitosamente")
    void testEliminar_Success() {
        // Arrange
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        // Act
        usuarioService.eliminar(1L);

        // Assert
        verify(usuarioRepository, times(1)).existsById(1L);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar - Debe lanzar excepción cuando usuario no existe")
    void testEliminar_NotFound() {
        // Arrange
        when(usuarioRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> usuarioService.eliminar(999L));
        verify(usuarioRepository, never()).deleteById(anyLong());
    }

    // ==================== Tests para buscarPorEmail ====================
    @Test
    @DisplayName("buscarPorEmail - Debe encontrar usuario por email")
    void testBuscarPorEmail_Success() {
        // Arrange
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioMock));

        // Act
        UsuarioDTO resultado = usuarioService.buscarPorEmail("juan.perez@example.com");

        // Assert
        assertNotNull(resultado);
        assertEquals("juan.perez@example.com", resultado.getEmail());
        verify(usuarioRepository, times(1)).findByEmail("juan.perez@example.com");
    }

    @Test
    @DisplayName("buscarPorEmail - Debe lanzar excepción cuando email no existe")
    void testBuscarPorEmail_NotFound() {
        // Arrange
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                    () -> usuarioService.buscarPorEmail("noexiste@example.com"));
        verify(usuarioRepository, times(1)).findByEmail("noexiste@example.com");
    }

    // ==================== Tests para buscarPorNombre ====================
    @Test
    @DisplayName("buscarPorNombre - Debe retornar usuarios que coincidan con nombre")
    void testBuscarPorNombre_Success() {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(usuarioMock);
        when(usuarioRepository.findByNombreContainingIgnoreCase(anyString())).thenReturn(usuarios);

        // Act
        List<UsuarioDTO> resultado = usuarioService.buscarPorNombre("Juan");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(usuarioRepository, times(1)).findByNombreContainingIgnoreCase("Juan");
    }

    // ==================== Tests para activar/desactivar ====================
    @Test
    @DisplayName("activar - Debe activar usuario exitosamente")
    void testActivar_Success() {
        // Arrange
        usuarioMock.setActivo(false);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMock);

        // Act
        UsuarioDTO resultado = usuarioService.activar(1L);

        // Assert
        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("desactivar - Debe desactivar usuario exitosamente")
    void testDesactivar_Success() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMock);

        // Act
        UsuarioDTO resultado = usuarioService.desactivar(1L);

        // Assert
        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    // ==================== Tests para obtener usuarios activos/inactivos ====================
    @Test
    @DisplayName("obtenerUsuariosActivos - Debe retornar solo usuarios activos")
    void testObtenerUsuariosActivos_Success() {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(usuarioMock);
        when(usuarioRepository.findByActivoTrue()).thenReturn(usuarios);

        // Act
        List<UsuarioDTO> resultado = usuarioService.obtenerUsuariosActivos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(usuarioRepository, times(1)).findByActivoTrue();
    }

    @Test
    @DisplayName("obtenerUsuariosInactivos - Debe retornar solo usuarios inactivos")
    void testObtenerUsuariosInactivos_Success() {
        // Arrange
        usuarioMock.setActivo(false);
        List<Usuario> usuarios = Arrays.asList(usuarioMock);
        when(usuarioRepository.findByActivoFalse()).thenReturn(usuarios);

        // Act
        List<UsuarioDTO> resultado = usuarioService.obtenerUsuariosInactivos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(usuarioRepository, times(1)).findByActivoFalse();
    }

    // ==================== Tests para buscarPorTexto ====================
    @Test
    @DisplayName("buscarPorTexto - Debe buscar usuarios por texto libre")
    void testBuscarPorTexto_Success() {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(usuarioMock);
        when(usuarioRepository.buscarPorTexto(anyString())).thenReturn(usuarios);

        // Act
        List<UsuarioDTO> resultado = usuarioService.buscarPorTexto("Juan");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(usuarioRepository, times(1)).buscarPorTexto("Juan");
    }

    // ==================== Tests para contarUsuariosActivos ====================
    @Test
    @DisplayName("contarUsuariosActivos - Debe retornar conteo de usuarios activos")
    void testContarUsuariosActivos_Success() {
        // Arrange
        when(usuarioRepository.countUsuariosActivos()).thenReturn(5L);

        // Act
        long resultado = usuarioService.contarUsuariosActivos();

        // Assert
        assertEquals(5L, resultado);
        verify(usuarioRepository, times(1)).countUsuariosActivos();
    }

    // ==================== Tests para asignarRoles ====================
    @Test
    @DisplayName("asignarRoles - Debe asignar roles exitosamente")
    void testAsignarRoles_Success() {
        // Arrange
        usuarioMock.setRoles(new HashSet<>());
        AssignRolesRequest request = new AssignRolesRequest();
        request.setRoles(new HashSet<>(Arrays.asList("CLIENTE", "MANAGER")));

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));
        when(roleRepository.findByName("CLIENTE")).thenReturn(Optional.of(roleMock));

        Role managerRole = new Role();
        managerRole.setId(2L);
        managerRole.setName("MANAGER");
        when(roleRepository.findByName("MANAGER")).thenReturn(Optional.of(managerRole));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMock);

        // Act
        UsuarioDTO resultado = usuarioService.asignarRoles(1L, request);

        // Assert
        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).findById(1L);
        verify(roleRepository, times(1)).findByName("CLIENTE");
        verify(roleRepository, times(1)).findByName("MANAGER");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("asignarRoles - Debe lanzar excepción cuando usuario no existe")
    void testAsignarRoles_UsuarioNotFound() {
        // Arrange
        AssignRolesRequest request = new AssignRolesRequest();
        request.setRoles(new HashSet<>(Arrays.asList("CLIENTE")));
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                    () -> usuarioService.asignarRoles(999L, request));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("asignarRoles - Debe lanzar excepción cuando rol no existe")
    void testAsignarRoles_RoleNotFound() {
        // Arrange
        usuarioMock.setRoles(new HashSet<>());
        AssignRolesRequest request = new AssignRolesRequest();
        request.setRoles(new HashSet<>(Arrays.asList("ROL_INEXISTENTE")));

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));
        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                    () -> usuarioService.asignarRoles(1L, request));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}
