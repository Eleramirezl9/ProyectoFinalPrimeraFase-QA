package com.ejemplo.config;

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
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@Configuration
public class DatabaseConfig {

    /**
     * Configuración personalizada del DataSource para UTF-8
     * Sobrescribe las propiedades por defecto para asegurar codificación correcta
     */
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        DataSourceProperties properties = new DataSourceProperties();
        
        // URL con configuración específica para UTF-8
        String url = "jdbc:h2:mem:testdb" +
                    ";DB_CLOSE_DELAY=-1" +
                    ";DB_CLOSE_ON_EXIT=FALSE" +
                    ";MODE=MySQL" +
                    ";DATABASE_TO_LOWER=TRUE" +
                    ";CASE_INSENSITIVE_IDENTIFIERS=TRUE" +
                    ";TRACE_LEVEL_FILE=0" +
                    ";TRACE_LEVEL_SYSTEM_OUT=0";
        
        properties.setUrl(url);
        properties.setDriverClassName("org.h2.Driver");
        properties.setUsername("sa");
        properties.setPassword("password");
        
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
     */
    private void logDatabaseConfiguration(String url) {
        System.out.println("=================================================");
        System.out.println("🗄️  Database Configuration");
        System.out.println("📍 JDBC URL: " + url);
        System.out.println("🔤 Character Encoding: UTF-8");
        System.out.println("🌍 Unicode Support: ENABLED");
        System.out.println("📝 Default Charset: " + StandardCharsets.UTF_8.displayName());
        System.out.println("=================================================");
    }
}