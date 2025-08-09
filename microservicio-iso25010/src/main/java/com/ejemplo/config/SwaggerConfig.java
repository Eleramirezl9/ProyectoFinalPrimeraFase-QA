package com.ejemplo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
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
        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(
                    new Server()
                        .url("http://localhost:8080/api")
                        .description("Servidor de desarrollo local"),
                    new Server()
                        .url("https://api.ejemplo.com")
                        .description("Servidor de producción")
                ))
                .tags(List.of(
                    new Tag()
                        .name("Usuarios")
                        .description("Operaciones relacionadas con la gestión de usuarios del sistema"),
                    new Tag()
                        .name("Productos")
                        .description("Operaciones relacionadas con el catálogo de productos"),
                    new Tag()
                        .name("Pedidos")
                        .description("Operaciones relacionadas con la gestión de pedidos y ventas")
                ));
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
                - ✅ **Operaciones CRUD completas** para todas las entidades
                - ✅ **Validación de datos** con Bean Validation
                - ✅ **Manejo global de excepciones** con respuestas estandarizadas
                - ✅ **Base de datos H2** en memoria para desarrollo y pruebas
                - ✅ **Documentación automática** con OpenAPI 3.0
                - ✅ **Logging estructurado** para monitoreo y debugging
                - ✅ **Arquitectura por capas** (Controller, Service, Repository)
                
                ### Entidades del Sistema
                
                #### Usuario
                Representa los usuarios del sistema con información personal y de contacto.
                - Campos: ID, nombre, apellido, email, teléfono, estado activo
                - Validaciones: email único, formato de email válido, campos obligatorios
                
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

