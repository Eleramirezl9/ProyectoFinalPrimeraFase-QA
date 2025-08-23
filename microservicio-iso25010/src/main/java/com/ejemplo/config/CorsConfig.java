package com.ejemplo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import org.springframework.lang.NonNull;

/**
 * Configuración de CORS (Cross-Origin Resource Sharing) para el microservicio
 * 
 * Esta configuración centraliza todas las políticas de CORS del sistema,
 * siguiendo las mejores prácticas de seguridad para APIs REST.
 * 
 * CORS es un mecanismo de seguridad que permite o restringe el acceso a recursos
 * desde diferentes dominios, protocolos o puertos.
 * 
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * URLs permitidas para desarrollo local.
     * En producción, estas deben ser reemplazadas por las URLs reales del frontend.
     */
    private final String[] allowedOrigins = {
        "http://localhost:3000",    // React/Next.js
        "http://localhost:4200",    // Angular
        "http://localhost:8080",    // Vue.js/Desarrollo local
        "http://127.0.0.1:3000",    // React/Next.js (IP local)
        "http://127.0.0.1:4200",    // Angular (IP local)
        "http://127.0.0.1:8080"     // Vue.js/Desarrollo local (IP local)
    };

    /**
     * Configuración global de CORS para todos los endpoints de la aplicación.
     * 
     * Esta configuración se aplica automáticamente a todos los controladores
     * sin necesidad de usar @CrossOrigin en cada uno.
     * 
     * @param registry Registro de configuraciones CORS
     */
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/api/**")  // Aplica a todos los endpoints bajo /api
                .allowedOriginPatterns(allowedOrigins)  // Orígenes permitidos
                .allowedMethods(
                    "GET",      // Lectura de datos
                    "POST",     // Creación de recursos
                    "PUT",      // Actualización completa
                    "PATCH",    // Actualización parcial
                    "DELETE",   // Eliminación de recursos
                    "OPTIONS"   // Preflight requests
                )
                .allowedHeaders(
                    "Content-Type",         // Tipo de contenido (application/json)
                    "Authorization",        // Token de autorización (Bearer, Basic, etc.)
                    "X-Requested-With",     // Para identificar peticiones AJAX
                    "Accept",              // Tipos de respuesta aceptados
                    "Origin",              // Origen de la petición
                    "Access-Control-Request-Method",    // Método solicitado en preflight
                    "Access-Control-Request-Headers"    // Headers solicitados en preflight
                )
                .exposedHeaders(
                    "Access-Control-Allow-Origin",      // Para debugging
                    "Access-Control-Allow-Credentials", // Credenciales permitidas
                    "Location",                         // URL del recurso creado (POST)
                    "X-Total-Count"                     // Para paginación
                )
                .allowCredentials(true)     // Permite cookies y headers de autorización
                .maxAge(3600);             // Cache del preflight por 1 hora
    }

    /**
     * Bean adicional para configuración más granular de CORS
     * 
     * Esta configuración se usa cuando necesitamos mayor control
     * sobre políticas específicas por endpoint.
     * 
     * @return Fuente de configuración CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Configuración para desarrollo - EN PRODUCCIÓN CAMBIAR POR URLs ESPECÍFICAS
        configuration.setAllowedOriginPatterns(Arrays.asList(allowedOrigins));
        
        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));
        
        // Headers permitidos en las peticiones
        configuration.setAllowedHeaders(Arrays.asList(
            "Content-Type", 
            "Authorization", 
            "X-Requested-With", 
            "Accept", 
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));
        
        // Headers que el navegador puede leer en las respuestas
        configuration.setExposedHeaders(Arrays.asList(
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials",
            "Location",
            "X-Total-Count"
        ));
        
        // Permitir credenciales (cookies, authorization headers)
        configuration.setAllowCredentials(true);
        
        // Tiempo de cache para preflight requests (1 hora)
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        
        return source;
    }
}