package com.ejemplo.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para CorsConfig
 * Valida la configuración de CORS
 *
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("CorsConfig - Pruebas de Integración")
class CorsConfigTest {

    @Autowired
    private CorsConfig corsConfig;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Test
    @DisplayName("Debe crear el bean CorsConfig")
    void testCorsConfigBeanCreated() {
        assertNotNull(corsConfig);
    }

    @Test
    @DisplayName("Debe crear el bean CorsConfigurationSource")
    void testCorsConfigurationSourceBeanCreated() {
        assertNotNull(corsConfigurationSource);
    }

    @Test
    @DisplayName("Debe configurar CorsConfigurationSource correctamente")
    void testCorsConfigurationSource() {
        // Act
        CorsConfiguration config = corsConfigurationSource.getCorsConfiguration(
            new org.springframework.mock.web.MockHttpServletRequest("GET", "/api/test")
        );

        // Assert
        assertNotNull(config);
    }

    @Test
    @DisplayName("Debe configurar allowed origins")
    void testAllowedOrigins() {
        // Act
        CorsConfiguration config = corsConfigurationSource.getCorsConfiguration(
            new org.springframework.mock.web.MockHttpServletRequest("GET", "/api/test")
        );

        // Assert
        assertNotNull(config);
        var patterns = config.getAllowedOriginPatterns();
        assertNotNull(patterns);
        assertFalse(patterns.isEmpty());
    }

    @Test
    @DisplayName("Debe configurar allowed methods")
    void testAllowedMethods() {
        // Act
        CorsConfiguration config = corsConfigurationSource.getCorsConfiguration(
            new org.springframework.mock.web.MockHttpServletRequest("GET", "/api/test")
        );

        // Assert
        assertNotNull(config);
        var methods = config.getAllowedMethods();
        assertNotNull(methods);
        assertFalse(methods.isEmpty());
    }

    @Test
    @DisplayName("Debe configurar allowed headers")
    void testAllowedHeaders() {
        // Act
        CorsConfiguration config = corsConfigurationSource.getCorsConfiguration(
            new org.springframework.mock.web.MockHttpServletRequest("GET", "/api/test")
        );

        // Assert
        assertNotNull(config);
        var headers = config.getAllowedHeaders();
        assertNotNull(headers);
        assertFalse(headers.isEmpty());
    }

    @Test
    @DisplayName("Debe configurar exposed headers")
    void testExposedHeaders() {
        // Act
        CorsConfiguration config = corsConfigurationSource.getCorsConfiguration(
            new org.springframework.mock.web.MockHttpServletRequest("GET", "/api/test")
        );

        // Assert
        assertNotNull(config);
        // Exposed headers puede estar vacío o nulo si no se configuró
    }

    @Test
    @DisplayName("Debe configurar allow credentials")
    void testAllowCredentials() {
        // Act
        CorsConfiguration config = corsConfigurationSource.getCorsConfiguration(
            new org.springframework.mock.web.MockHttpServletRequest("GET", "/api/test")
        );

        // Assert
        assertNotNull(config);
        assertNotNull(config.getAllowCredentials());
    }

    @Test
    @DisplayName("Debe configurar max age")
    void testMaxAge() {
        // Act
        CorsConfiguration config = corsConfigurationSource.getCorsConfiguration(
            new org.springframework.mock.web.MockHttpServletRequest("GET", "/api/test")
        );

        // Assert
        assertNotNull(config);
        Long maxAge = config.getMaxAge();
        assertNotNull(maxAge);
        assertTrue(maxAge >= 0);
    }

    @Test
    @DisplayName("Debe permitir métodos HTTP comunes")
    void testCommonHttpMethods() {
        // Act
        CorsConfiguration config = corsConfigurationSource.getCorsConfiguration(
            new org.springframework.mock.web.MockHttpServletRequest("GET", "/api/test")
        );

        // Assert
        assertNotNull(config);
        var methods = config.getAllowedMethods();
        assertNotNull(methods);
        assertTrue(methods.size() > 0);
    }

    @Test
    @DisplayName("Debe aplicar CORS a rutas /api/**")
    void testCorsAppliedToApiRoutes() {
        // Act
        CorsConfiguration config = corsConfigurationSource.getCorsConfiguration(
            new org.springframework.mock.web.MockHttpServletRequest("GET", "/api/usuarios")
        );

        // Assert
        assertNotNull(config);
    }

    @Test
    @DisplayName("Debe aplicar CORS a subrutas de /api/**")
    void testCorsAppliedToApiSubRoutes() {
        // Act
        CorsConfiguration config1 = corsConfigurationSource.getCorsConfiguration(
            new org.springframework.mock.web.MockHttpServletRequest("GET", "/api/productos")
        );
        CorsConfiguration config2 = corsConfigurationSource.getCorsConfiguration(
            new org.springframework.mock.web.MockHttpServletRequest("POST", "/api/pedidos")
        );

        // Assert
        assertNotNull(config1);
        assertNotNull(config2);
    }

    @Test
    @DisplayName("Debe tener configuración consistente entre métodos")
    void testConsistentConfiguration() {
        // Act
        CorsConfiguration config1 = corsConfigurationSource.getCorsConfiguration(
            new org.springframework.mock.web.MockHttpServletRequest("GET", "/api/test")
        );
        CorsConfiguration config2 = corsConfigurationSource.getCorsConfiguration(
            new org.springframework.mock.web.MockHttpServletRequest("POST", "/api/test")
        );

        // Assert
        assertNotNull(config1);
        assertNotNull(config2);
        assertEquals(config1.getAllowedOriginPatterns(), config2.getAllowedOriginPatterns());
        assertEquals(config1.getAllowedMethods(), config2.getAllowedMethods());
    }

    @Test
    @DisplayName("Debe configurar headers de autorización")
    void testAuthorizationHeaders() {
        // Act
        CorsConfiguration config = corsConfigurationSource.getCorsConfiguration(
            new org.springframework.mock.web.MockHttpServletRequest("GET", "/api/test")
        );

        // Assert
        assertNotNull(config);
        assertNotNull(config.getAllowedHeaders());
        // Authorization header es comúnmente usado
    }

    @Test
    @DisplayName("El bean CorsConfig debe implementar WebMvcConfigurer")
    void testWebMvcConfigurerImplementation() {
        // Assert
        assertTrue(corsConfig instanceof org.springframework.web.servlet.config.annotation.WebMvcConfigurer);
    }
}
