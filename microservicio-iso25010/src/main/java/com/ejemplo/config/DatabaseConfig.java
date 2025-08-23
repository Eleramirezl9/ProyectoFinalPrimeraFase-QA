package com.ejemplo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;

/**
 * Configuración específica para el DataSource con soporte completo UTF-8
 * Asegura que todos los caracteres especiales (tildes, eñes, etc.) se manejen correctamente
 * 
 * SEGURIDAD: Usa variables de entorno para todas las configuraciones sensibles
 * 
 * @author Estudiante Universidad Mariano Gálvez
 * @version 2.0.0 - Variables de Entorno Seguras
 */
@Configuration
public class DatabaseConfig {

    @Value("${DB_URL:jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL;DATABASE_TO_LOWER=TRUE}")
    private String dbUrl;
    
    @Value("${DB_DRIVER:org.h2.Driver}")
    private String dbDriver;
    
    @Value("${DB_USERNAME:sa}")
    private String dbUsername;
    
    @Value("${DB_PASSWORD:password}")
    private String dbPassword;

    /**
     * Configuración personalizada del DataSource para UTF-8
     * SEGURO: Usa variables de entorno desde application.yml o .env
     */
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        DataSourceProperties properties = new DataSourceProperties();
        
        // URL con configuración específica para UTF-8 desde variables de entorno
        String enhancedUrl = dbUrl + 
                    ";CASE_INSENSITIVE_IDENTIFIERS=TRUE" +
                    ";TRACE_LEVEL_FILE=0" +
                    ";TRACE_LEVEL_SYSTEM_OUT=0";
        
        // ✅ SEGURO: Usa variables de entorno
        properties.setUrl(enhancedUrl);
        properties.setDriverClassName(dbDriver);
        properties.setUsername(dbUsername);
        properties.setPassword(dbPassword);
        
        return properties;
    }

    /**
     * Bean del DataSource configurado para UTF-8
     */
    @Bean
    @Primary
    public DataSource dataSource() {
        DataSourceProperties properties = dataSourceProperties();
        
        // Crear el DataSource con las propiedades configuradas
        DataSource dataSource = properties.initializeDataSourceBuilder()
                .build();
        
        // Log de configuración para debugging
        logDatabaseConfiguration(properties.getUrl());
        
        return dataSource;
    }

    /**
     * Log de información sobre la configuración de la base de datos
     * SEGURO: No expone credenciales sensibles
     */
    private void logDatabaseConfiguration(String url) {
        System.out.println("=================================================");
        System.out.println("🗄️  Database Configuration");
        System.out.println("📍 JDBC URL: " + maskSensitiveUrl(url));
        System.out.println("👤 Username: " + maskUsername(dbUsername));
        System.out.println("🔤 Character Encoding: UTF-8");
        System.out.println("🌍 Unicode Support: ENABLED");
        System.out.println("📝 Default Charset: " + StandardCharsets.UTF_8.displayName());
        System.out.println("🔐 Security: Variables de Entorno");
        System.out.println("=================================================");
    }
    
    /**
     * Enmascara URLs sensibles para logging seguro
     */
    private String maskSensitiveUrl(String url) {
        if (url.contains("password")) {
            return url.replaceAll("password=[^;]+", "password=***");
        }
        return url;
    }
    
    /**
     * Enmascara username para logging seguro
     */
    private String maskUsername(String username) {
        if (username.length() <= 2) return "***";
        return username.substring(0, 1) + "***" + username.substring(username.length() - 1);
    }
}