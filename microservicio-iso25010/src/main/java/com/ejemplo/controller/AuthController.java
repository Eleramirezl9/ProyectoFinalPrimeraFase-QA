package com.ejemplo.controller;

import com.ejemplo.dto.AuthResponse;
import com.ejemplo.dto.LoginRequest;
import com.ejemplo.dto.RegisterRequest;
import com.ejemplo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para autenticación
 * Maneja las operaciones de login y registro de usuarios
 *
 * NOTA: CORS está configurado globalmente en CorsConfig.java
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Endpoints para autenticación y registro de usuarios")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Endpoint para registrar un nuevo usuario
     *
     * @param request Datos del usuario a registrar
     * @return ResponseEntity con el token JWT y datos del usuario
     */
    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario",
               description = "Crea un nuevo usuario en el sistema con rol CLIENTE por defecto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario ya existe",
                    content = @Content)
    })
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint para autenticar un usuario
     *
     * @param request Credenciales del usuario
     * @return ResponseEntity con el token JWT y datos del usuario
     */
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión",
               description = "Autentica un usuario y devuelve un token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas",
                    content = @Content)
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
