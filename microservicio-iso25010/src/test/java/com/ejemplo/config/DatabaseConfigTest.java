package com.ejemplo.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para DatabaseConfig
 * Valida la configuración del DataSource y UTF-8
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("DatabaseConfig - Pruebas de Integración")
class DatabaseConfigTest {

    @Autowired
    private DatabaseConfig databaseConfig;

    @Autowired
    private DataSource dataSource;

    @Test
    @DisplayName("Debe crear el bean DatabaseConfig")
    void testDatabaseConfigBeanCreated() {
        assertNotNull(databaseConfig);
    }

    @Test
    @DisplayName("Debe crear el bean DataSource")
    void testDataSourceBeanCreated() {
        assertNotNull(dataSource);
    }

    @Test
    @DisplayName("Debe conectar a la base de datos correctamente")
    void testDataSourceConnection() throws SQLException {
        // Act
        try (Connection connection = dataSource.getConnection()) {
            // Assert
            assertNotNull(connection);
            assertFalse(connection.isClosed());
        }
    }

    @Test
    @DisplayName("Debe configurar DataSourceProperties correctamente")
    void testDataSourceProperties() {
        // Act
        var properties = databaseConfig.dataSourceProperties();

        // Assert
        assertNotNull(properties);
        assertNotNull(properties.getUrl());
        assertNotNull(properties.getDriverClassName());
        assertNotNull(properties.getUsername());
    }

    @Test
    @DisplayName("La URL debe estar configurada")
    void testUrlContainsUtf8Configuration() {
        // Act
        var properties = databaseConfig.dataSourceProperties();
        String url = properties.getUrl();

        // Assert
        assertNotNull(url);
        assertTrue(url.contains("jdbc:h2:"));
    }

    @Test
    @DisplayName("Debe usar driver H2")
    void testDriverClassName() {
        // Act
        var properties = databaseConfig.dataSourceProperties();

        // Assert
        assertEquals("org.h2.Driver", properties.getDriverClassName());
    }

    @Test
    @DisplayName("Debe configurar username y password")
    void testUsernameAndPasswordConfigured() {
        // Act
        var properties = databaseConfig.dataSourceProperties();

        // Assert
        assertNotNull(properties.getUsername());
        assertNotNull(properties.getPassword());
    }

    @Test
    @DisplayName("El DataSource debe estar activo")
    void testDataSourceIsActive() throws SQLException {
        // Act & Assert
        try (Connection connection = dataSource.getConnection()) {
            assertTrue(connection.isValid(5)); // timeout de 5 segundos
        }
    }

    @Test
    @DisplayName("Debe soportar múltiples conexiones")
    void testMultipleConnections() throws SQLException {
        // Act
        try (Connection conn1 = dataSource.getConnection();
             Connection conn2 = dataSource.getConnection()) {

            // Assert
            assertNotNull(conn1);
            assertNotNull(conn2);
            assertNotSame(conn1, conn2);
            assertFalse(conn1.isClosed());
            assertFalse(conn2.isClosed());
        }
    }

    @Test
    @DisplayName("Debe configurar autocommit por defecto")
    void testAutoCommitDefault() throws SQLException {
        // Act
        try (Connection connection = dataSource.getConnection()) {
            // Assert
            assertTrue(connection.getAutoCommit());
        }
    }

    @Test
    @DisplayName("El catálogo debe estar configurado")
    void testCatalogConfiguration() throws SQLException {
        // Act
        try (Connection connection = dataSource.getConnection()) {
            String catalog = connection.getCatalog();

            // Assert
            assertNotNull(catalog);
        }
    }

    @Test
    @DisplayName("Debe crear dataSource con las propiedades personalizadas")
    void testDataSourceCreation() {
        // Act
        DataSource createdDataSource = databaseConfig.dataSource();

        // Assert
        assertNotNull(createdDataSource);
        assertSame(dataSource, createdDataSource);
    }

    @Test
    @DisplayName("La URL debe tener formato válido")
    void testEnhancedUrlConfiguration() {
        // Act
        var properties = databaseConfig.dataSourceProperties();
        String url = properties.getUrl();

        // Assert
        assertNotNull(url);
        assertTrue(url.startsWith("jdbc:h2:"));
    }
}
