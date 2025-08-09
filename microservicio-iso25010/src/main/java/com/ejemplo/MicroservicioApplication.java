package com.ejemplo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Clase principal de la aplicación Spring Boot
 * Microservicio para evaluación de calidad según ISO/IEC 25010
 * 
 * @author Estudiante Universidad Mariano Gálvez
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaRepositories
public class MicroservicioApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicioApplication.class, args);
        System.out.println("=================================================");
        System.out.println("🚀 Microservicio ISO/IEC 25010 iniciado exitosamente");
        System.out.println("📖 Swagger UI: http://localhost:8080/api/swagger-ui.html");
        System.out.println("🗄️  H2 Console: http://localhost:8080/api/h2-console");
        System.out.println("📊 API Docs: http://localhost:8080/api/api-docs");
        System.out.println("=================================================");
    }
}

