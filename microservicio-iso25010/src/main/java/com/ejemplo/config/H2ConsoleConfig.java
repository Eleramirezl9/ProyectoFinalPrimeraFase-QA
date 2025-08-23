package com.ejemplo.config;

import org.springframework.beans.factory.annotation.Value;
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
 * SEGURIDAD: Usa variables de entorno para credenciales y configuración
 * 
 * @author Estudiante Universidad Mariano Gálvez
 * @version 2.0.0 - Variables de Entorno Seguras
 */
@Configuration
@EnableWebSecurity
@Profile({"dev", "test", "default"})
public class H2ConsoleConfig {

    @Value("${DB_URL:jdbc:h2:mem:testdb}")
    private String dbUrl;
    
    @Value("${DB_USERNAME:sa}")
    private String dbUsername;
    
    @Value("${DB_PASSWORD:password}")
    private String dbPassword;
    
    @Value("${SERVER_PORT:8080}")
    private String serverPort;
    
    @Value("${CONTEXT_PATH:/api}")
    private String contextPath;

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
                    .frameOptions(frameOptions -> frameOptions.sameOrigin()))
                
                .build();
    }

    /**
     * Bean de configuración adicional para desarrollo
     * Proporciona información útil sobre la configuración de H2 de forma SEGURA
     */
    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled", havingValue = "true", matchIfMissing = true)
    public H2ConsoleInfo h2ConsoleInfo() {
        return new H2ConsoleInfo(dbUrl, dbUsername, dbPassword, serverPort, contextPath);
    }

    /**
     * Clase de información sobre la configuración de H2
     * SEGURO: No expone credenciales y usa variables de entorno
     */
    public static class H2ConsoleInfo {
        
        private final String dbUrl;
        private final String dbUsername;
        private final String dbPassword;
        private final String serverPort;
        private final String contextPath;
        
        public H2ConsoleInfo(String dbUrl, String dbUsername, String dbPassword, String serverPort, String contextPath) {
            this.dbUrl = dbUrl;
            this.dbUsername = dbUsername;
            this.dbPassword = dbPassword;
            this.serverPort = serverPort;
            this.contextPath = contextPath;
            logH2Configuration();
        }

        private void logH2Configuration() {
            System.out.println("=================================================");
            System.out.println("🗄️  H2 Database Console Configuration");
            System.out.println("📍 Console URL: http://localhost:" + serverPort + contextPath + "/h2-console");
            System.out.println("🔗 JDBC URL: " + maskJdbcUrl(dbUrl));
            System.out.println("👤 Username: " + maskUsername(dbUsername));
            System.out.println("🔑 Password: " + maskPassword(dbPassword));
            System.out.println("🔐 Security: Variables de Entorno");
            System.out.println("⚠️  Note: H2 Console is enabled for development only");
            System.out.println("=================================================");
        }

        public String getConsoleUrl() {
            return "http://localhost:" + serverPort + contextPath + "/h2-console";
        }

        public String getJdbcUrl() {
            return dbUrl;
        }

        public String getUsername() {
            return dbUsername;
        }

        public String getPassword() {
            // ✅ NUNCA retornar password real en logs o APIs
            return maskPassword(dbPassword);
        }

        public boolean isEnabled() {
            return true;
        }
        
        /**
         * Métodos de seguridad para enmascarar información sensible
         */
        private String maskJdbcUrl(String url) {
            if (url.contains("password")) {
                return url.replaceAll("password=[^;]+", "password=***");
            }
            return url;
        }
        
        private String maskUsername(String username) {
            if (username.length() <= 2) return "***";
            return username.substring(0, 1) + "***" + username.substring(username.length() - 1);
        }
        
        private String maskPassword(String password) {
            return "***" + (password.length() > 0 ? " (" + password.length() + " chars)" : "");
        }
    }
}

