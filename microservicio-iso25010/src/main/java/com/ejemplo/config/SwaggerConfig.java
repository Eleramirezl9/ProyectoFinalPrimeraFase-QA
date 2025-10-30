package com.ejemplo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de Swagger/OpenAPI para la documentación automática de la API
 * Proporciona información detallada sobre los endpoints y modelos de datos
 * 
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configuración principal de OpenAPI
     * Define información general de la API, servidores y tags
     */
    @Bean
    public OpenAPI customOpenAPI() {
        // Nombre del esquema de seguridad
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(
                    new Server()
                        .url("http://localhost:8080/api")
                        .description("Servidor de desarrollo local"),
                    new Server()
                        .url("https://api.ejemplo.com/api")
                        .description("Servidor de producción")
                ))
                .tags(List.of(
                    new Tag()
                        .name("Autenticación")
                        .description("Endpoints para autenticación y registro de usuarios"),
                    new Tag()
                        .name("Usuarios")
                        .description("Operaciones relacionadas con la gestión de usuarios del sistema"),
                    new Tag()
                        .name("Productos")
                        .description("Operaciones relacionadas con el catálogo de productos"),
                    new Tag()
                        .name("Pedidos")
                        .description("Operaciones relacionadas con la gestión de pedidos y ventas")
                ))
                // Agregar configuración de seguridad JWT
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Ingresa el token JWT obtenido del endpoint /auth/login")
                        )
                );
    }

    /**
     * Información general de la API
     * Incluye título, descripción, versión, contacto y licencia
     */
    private Info apiInfo() {
        return new Info()
                .title("Microservicio ISO/IEC 25010")
                .description(buildDescription())
                .version("1.0.0")
                .contact(buildContact())
                .license(buildLicense());
    }

    /**
     * Construye la descripción detallada de la API
     */
    private String buildDescription() {
        return """
                ## Descripción
                
                API REST desarrollada con Spring Boot para la evaluación de calidad de software según la norma ISO/IEC 25010.
                Este microservicio implementa un sistema completo de gestión de usuarios, productos y pedidos con las siguientes características:
                
                ### Características Principales
                - ✅ **Autenticación JWT** con tokens de acceso y refresh
                - ✅ **Sistema de roles y permisos** (ADMIN, MANAGER, CLIENTE)
                - ✅ **Operaciones CRUD completas** para todas las entidades
                - ✅ **Validación de datos** con Bean Validation
                - ✅ **Manejo global de excepciones** con respuestas estandarizadas
                - ✅ **Base de datos H2** en memoria para desarrollo y pruebas
                - ✅ **Documentación automática** con OpenAPI 3.0
                - ✅ **Logging estructurado** para monitoreo y debugging
                - ✅ **Arquitectura por capas** (Controller, Service, Repository)
                
                ### 🔐 Autenticación y Seguridad

                #### Autenticación JWT
                El sistema utiliza JSON Web Tokens (JWT) para autenticación segura:
                - **Access Token**: Válido por 24 horas
                - **Refresh Token**: Válido por 7 días
                - **Algoritmo**: HS384 (HMAC-SHA384)

                Para autenticarte:
                1. Haz login en `/auth/login` con username y password
                2. Copia el token recibido
                3. Click en el botón "Authorize" 🔓 arriba
                4. Ingresa: `Bearer {tu_token}`
                5. Ahora puedes usar los endpoints protegidos

                #### Sistema de Roles
                | Rol | Permisos |
                |-----|----------|
                | **CLIENTE** | Ver usuarios, productos, pedidos. Crear pedidos |
                | **MANAGER** | Todo lo de CLIENTE + crear/editar usuarios y productos |
                | **ADMIN** | Acceso total + eliminar entidades + asignar roles |

                #### Usuarios de Prueba
                ⚠️ Consultar documentación del proyecto para credenciales de prueba
                - `admin` (ADMIN)
                - `mrodriguez` (MANAGER)
                - `jgarcia` (CLIENTE)

                ### Entidades del Sistema

                #### Usuario
                Representa los usuarios del sistema con información personal, autenticación y roles.
                - Campos: ID, nombre, apellido, username, email, password, teléfono, estado activo
                - Roles: ADMIN, MANAGER, CLIENTE (asignables por ADMIN)
                - Validaciones: email y username únicos, formato de email válido, campos obligatorios
                
                #### Producto
                Representa los productos del catálogo con información comercial.
                - Campos: ID, nombre, descripción, precio, stock, categoría, marca
                - Validaciones: precio positivo, stock no negativo, nombre obligatorio
                
                #### Pedido
                Representa las transacciones entre usuarios y productos.
                - Campos: ID, usuario, producto, cantidad, precio unitario, total, estado
                - Estados: PENDIENTE, CONFIRMADO, EN_PROCESO, ENVIADO, ENTREGADO, CANCELADO
                - Lógica: cálculo automático de totales, gestión de stock
                
                ### Funcionalidades Avanzadas
                - **Autenticación JWT**: Login seguro con tokens de acceso y refresh
                - **Control de roles**: Sistema jerárquico de permisos (ADMIN > MANAGER > CLIENTE)
                - **Gestión de roles**: Endpoint para asignar/cambiar roles (solo ADMIN)
                - **Búsquedas flexibles**: Por texto libre, rangos de precios, categorías
                - **Gestión de stock**: Reducción/aumento automático en pedidos
                - **Estados de pedidos**: Flujo completo desde creación hasta entrega
                - **Estadísticas**: Contadores y métricas del sistema
                - **Activación/desactivación**: Soft delete para usuarios y productos
                
                ### Calidad de Software (ISO/IEC 25010)
                Este microservicio ha sido diseñado siguiendo los principios de calidad:
                - **Funcionalidad**: Cumple con todos los requisitos funcionales
                - **Confiabilidad**: Manejo robusto de errores y excepciones
                - **Usabilidad**: API intuitiva y bien documentada
                - **Eficiencia**: Consultas optimizadas y uso eficiente de recursos
                - **Mantenibilidad**: Código limpio, bien estructurado y documentado
                - **Portabilidad**: Compatible con diferentes entornos y bases de datos
                
                ### Tecnologías Utilizadas
                - **Java 17** - Lenguaje de programación
                - **Spring Boot 3.2.0** - Framework principal
                - **Spring Data JPA** - Persistencia de datos
                - **H2 Database** - Base de datos en memoria
                - **SpringDoc OpenAPI** - Documentación automática
                - **Bean Validation** - Validación de datos
                - **SLF4J + Logback** - Sistema de logging
                
                ### Uso de la API
                Todos los endpoints están documentados con ejemplos de request/response.
                La API sigue las convenciones REST estándar y retorna códigos de estado HTTP apropiados.
                """;
    }

    /**
     * Información de contacto del desarrollador
     */
    private Contact buildContact() {
        return new Contact()
                .name("Estudiante Universidad Mariano Gálvez")
                .email("estudiante@umg.edu.gt")
                .url("https://www.umg.edu.gt");
    }

    /**
     * Información de licencia del proyecto
     */
    private License buildLicense() {
        return new License()
                .name("Licencia Académica")
                .url("https://www.umg.edu.gt/licencia-academica");
    }
}

