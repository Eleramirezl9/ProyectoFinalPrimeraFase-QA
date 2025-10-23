package com.ejemplo.service;

import com.ejemplo.dto.AuthResponse;
import com.ejemplo.dto.LoginRequest;
import com.ejemplo.dto.RegisterRequest;
import com.ejemplo.model.Role;
import com.ejemplo.model.Usuario;
import com.ejemplo.repository.RoleRepository;
import com.ejemplo.repository.UsuarioRepository;
import com.ejemplo.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Servicio de autenticación
 * Maneja el registro y login de usuarios
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Registra un nuevo usuario en el sistema
     *
     * @param request Datos del usuario a registrar
     * @return Respuesta con el token JWT y datos del usuario
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Validar que el username no exista
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("El username ya está en uso");
        }

        // Validar que el email no exista
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setUsername(request.getUsername());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setTelefono(request.getTelefono());
        usuario.setActivo(true);
        usuario.setCuentaNoExpirada(true);
        usuario.setCuentaNoBloqueada(true);
        usuario.setCredencialesNoExpiradas(true);

        // Asignar rol por defecto (CLIENTE)
        Role clienteRole = roleRepository.findByName("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Error: Rol CLIENTE no encontrado"));
        Set<Role> roles = new HashSet<>();
        roles.add(clienteRole);
        usuario.setRoles(roles);

        // Guardar usuario
        usuarioRepository.save(usuario);

        // Generar token JWT
        String token = jwtService.generateToken(usuario);
        String refreshToken = jwtService.generateRefreshToken(usuario);

        // Obtener lista de roles
        List<String> rolesList = usuario.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return new AuthResponse(
                token,
                refreshToken,
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getApellido(),
                rolesList,
                jwtService.getExpirationTime()
        );
    }

    /**
     * Autentica un usuario existente
     *
     * @param request Credenciales del usuario
     * @return Respuesta con el token JWT y datos del usuario
     */
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        // Autenticar con Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Buscar usuario
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Generar tokens
        String token = jwtService.generateToken(usuario);
        String refreshToken = jwtService.generateRefreshToken(usuario);

        // Obtener lista de roles
        List<String> rolesList = usuario.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return new AuthResponse(
                token,
                refreshToken,
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getApellido(),
                rolesList,
                jwtService.getExpirationTime()
        );
    }
}
