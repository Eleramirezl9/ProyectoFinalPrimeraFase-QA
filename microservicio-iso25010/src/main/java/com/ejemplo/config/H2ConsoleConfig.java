package com.ejemplo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuración para habilitar y asegurar el acceso a la consola H2
 * Permite acceso a la interfaz web de H2 para desarrollo y pruebas
 *
 * NOTA: La configuración de seguridad ahora está en SecurityConfig.java
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 3.0.0 - Configuración de Seguridad movida a SecurityConfig
 */
@Configuration
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

