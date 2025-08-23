package com.ejemplo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;
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
 * Las configuraciones se obtienen de variables de entorno o archivos de propiedades,
 * permitiendo flexibilidad entre entornos de desarrollo, pruebas y producción.
 * 
 * @author Estudiante Universidad Mariano Gálvez
 * @version 2.0.0
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origins}")
    private String allowedOriginsString;
    
    @Value("${cors.allowed-methods}")
    private String allowedMethodsString;
    
    @Value("${cors.allowed-headers}")
    private String allowedHeadersString;
    
    @Value("${cors.exposed-headers}")
    private String exposedHeadersString;
    
    @Value("${cors.allow-credentials}")
    private boolean allowCredentials;
    
    @Value("${cors.max-age}")
    private long maxAge;

    /**
     * Convierte la cadena de orígenes permitidos en un array
     * @return Array de orígenes permitidos
     */
    private String[] getAllowedOrigins() {
        return allowedOriginsString.split(",");
    }
    
    /**
     * Convierte la cadena de métodos permitidos en una lista
     * @return Lista de métodos HTTP permitidos
     */
    private List<String> getAllowedMethods() {
        return Arrays.asList(allowedMethodsString.split(","));
    }
    
    /**
     * Convierte la cadena de headers permitidos en una lista
     * @return Lista de headers permitidos
     */
    private List<String> getAllowedHeaders() {
        return Arrays.asList(allowedHeadersString.split(","));
    }
    
    /**
     * Convierte la cadena de headers expuestos en una lista
     * @return Lista de headers expuestos
     */
    private List<String> getExposedHeaders() {
        return Arrays.asList(exposedHeadersString.split(","));
    }

    /**
     * Configuración global de CORS para todos los endpoints de la aplicación.
     * 
     * Esta configuración se aplica automáticamente a todos los controladores
     * sin necesidad de usar @CrossOrigin en cada uno.
     * 
     * Los valores se obtienen desde variables de entorno o application.yml
     * 
     * @param registry Registro de configuraciones CORS
     */
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/api/**")  // Aplica a todos los endpoints bajo /api
                .allowedOriginPatterns(getAllowedOrigins())  // Orígenes desde config
                .allowedMethods(getAllowedMethods().toArray(String[]::new))  // Métodos desde config
                .allowedHeaders(getAllowedHeaders().toArray(String[]::new))  // Headers desde config
                .exposedHeaders(getExposedHeaders().toArray(String[]::new))  // Headers expuestos desde config
                .allowCredentials(allowCredentials)     // Credenciales desde config
                .maxAge(maxAge);                       // Cache desde config
    }

    /**
     * Bean adicional para configuración más granular de CORS
     * 
     * Esta configuración se usa cuando necesitamos mayor control
     * sobre políticas específicas por endpoint.
     * 
     * Todos los valores se obtienen desde variables de entorno o application.yml
     * 
     * @return Fuente de configuración CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Configuración desde variables de entorno/properties
        configuration.setAllowedOriginPatterns(Arrays.asList(getAllowedOrigins()));
        configuration.setAllowedMethods(getAllowedMethods());
        configuration.setAllowedHeaders(getAllowedHeaders());
        configuration.setExposedHeaders(getExposedHeaders());
        configuration.setAllowCredentials(allowCredentials);
        configuration.setMaxAge(maxAge);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        
        return source;
    }
}