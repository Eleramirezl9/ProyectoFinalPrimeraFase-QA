package com.ejemplo.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Configuración para habilitar y asegurar el acceso a la consola H2
 * Permite acceso a la interfaz web de H2 para desarrollo y pruebas
 * 
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@Configuration
@EnableWebSecurity
@Profile({"dev", "test", "default"})
public class H2ConsoleConfig {

    /**
     * Configuración de seguridad para permitir acceso a la consola H2
     * Desactiva CSRF y permite frames para la interfaz web de H2
     */
    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled", havingValue = "true", matchIfMissing = true)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Desactivar CSRF para permitir el funcionamiento de la consola H2
                .csrf(csrf -> csrf
                    .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))
                    .disable())
                
                // Configurar autorización de requests
                .authorizeHttpRequests(auth -> auth
                    // Permitir acceso público a la consola H2
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                    // Permitir acceso público a todos los endpoints de la API
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/api/**")).permitAll()
                    // Permitir acceso público a Swagger UI
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui/**")).permitAll()
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui.html")).permitAll()
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/v3/api-docs/**")).permitAll()
                    // Permitir acceso público a recursos estáticos
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/webjars/**")).permitAll()
                    // Permitir acceso público a actuator endpoints
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/actuator/**")).permitAll()
                    // Cualquier otra request requiere autenticación (aunque no hay autenticación configurada)
                    .anyRequest().permitAll())
                
                // Permitir frames para la consola H2 (necesario para su interfaz web)
                .headers(headers -> headers
                    .frameOptions().sameOrigin())
                
                .build();
    }

    /**
     * Bean de configuración adicional para desarrollo
     * Proporciona información útil sobre la configuración de H2
     */
    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled", havingValue = "true", matchIfMissing = true)
    public H2ConsoleInfo h2ConsoleInfo() {
        return new H2ConsoleInfo();
    }

    /**
     * Clase de información sobre la configuración de H2
     * Útil para logging y debugging
     */
    public static class H2ConsoleInfo {
        
        public H2ConsoleInfo() {
            logH2Configuration();
        }

        private void logH2Configuration() {
            System.out.println("=================================================");
            System.out.println("🗄️  H2 Database Console Configuration");
            System.out.println("📍 Console URL: http://localhost:8080/api/h2-console");
            System.out.println("🔗 JDBC URL: jdbc:h2:mem:testdb");
            System.out.println("👤 Username: sa");
            System.out.println("🔑 Password: password");
            System.out.println("⚠️  Note: H2 Console is enabled for development only");
            System.out.println("=================================================");
        }

        public String getConsoleUrl() {
            return "http://localhost:8080/api/h2-console";
        }

        public String getJdbcUrl() {
            return "jdbc:h2:mem:testdb";
        }

        public String getUsername() {
            return "sa";
        }

        public String getPassword() {
            return "password";
        }

        public boolean isEnabled() {
            return true;
        }
    }
}

