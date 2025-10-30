package com.ejemplo.config;

import com.ejemplo.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de Spring Security
 * Define la configuración de seguridad, autenticación y autorización
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource;

    /**
     * Configuración de la cadena de filtros de seguridad
     *
     * CSRF está deshabilitado de forma segura porque:
     * - Esta es una API REST stateless que usa autenticación JWT
     * - JWT se envía en el header Authorization, no en cookies
     * - SessionCreationPolicy.STATELESS significa que no hay sesiones de servidor
     * - CSRF solo es relevante para aplicaciones que usan cookies de sesión
     * - La protección contra CSRF no es necesaria para APIs basadas en tokens
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF deshabilitado: API REST stateless con JWT (sin cookies de sesión)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos (sin autenticación) - DEBEN IR PRIMERO
                        .requestMatchers(
                                "/api/auth/**",
                                "/auth/**",
                                "/h2-console/**",
                                "/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/actuator/health",
                                "/actuator/info"
                        ).permitAll()

                        // Permitir OPTIONS requests (CORS preflight) - DEBE IR DESPUÉS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Endpoints de usuarios - requieren roles específicos
                        .requestMatchers(HttpMethod.GET, "/usuarios/**")
                            .hasAnyRole("CLIENTE", "ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.POST, "/usuarios/**")
                            .hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/usuarios/**")
                            .hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.PATCH, "/usuarios/*/roles")
                            .hasRole("ADMIN")  // Solo ADMIN puede asignar roles
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/**")
                            .hasRole("ADMIN")

                        // Endpoints de productos
                        .requestMatchers(HttpMethod.GET, "/productos/**")
                            .hasAnyRole("CLIENTE", "ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.POST, "/productos/**")
                            .hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/productos/**")
                            .hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/productos/**")
                            .hasRole("ADMIN")

                        // Endpoints de pedidos
                        .requestMatchers(HttpMethod.GET, "/pedidos/**")
                            .hasAnyRole("CLIENTE", "ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.POST, "/pedidos/**")
                            .hasAnyRole("CLIENTE", "ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/pedidos/**")
                            .hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/pedidos/**")
                            .hasRole("ADMIN")

                        // Cualquier otra solicitud requiere autenticación
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // Configuración adicional para H2 Console
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                );

        return http.build();
    }

    /**
     * Proveedor de autenticación personalizado
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Manager de autenticación
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Encoder de passwords (BCrypt)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
