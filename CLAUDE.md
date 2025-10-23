# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Spring Boot 3.2.12 microservice implementing ISO/IEC 25010 quality evaluation for a management system with Users, Products, and Orders. Built for Universidad Mariano Gálvez academic project.

**Tech Stack:**
- Java 17
- Spring Boot 3.2.12 (Web, JPA, Security, Validation, Actuator)
- H2 Database (in-memory for dev/test)
- Maven
- SpringDoc OpenAPI 2.2.0 (Swagger)

## Development Commands

### Running the Application

**Primary method (Windows):**
```cmd
cd microservicio-iso25010
start-dev.bat
```

**Maven wrapper (cross-platform):**
```cmd
cd microservicio-iso25010
./mvnw.cmd spring-boot:run
```

**With specific profile:**
```cmd
./mvnw.cmd spring-boot:run -Dspring.profiles.active=dev
```

### Build Commands

```cmd
# Clean and compile
./mvnw.cmd clean compile

# Package (create JAR)
./mvnw.cmd package

# Run tests
./mvnw.cmd test

# Clean build artifacts
./mvnw.cmd clean
```

### Key URLs (when running locally)

- API Base: http://localhost:8080/api
- Swagger UI: http://localhost:8080/api/swagger-ui.html
- H2 Console: http://localhost:8080/api/h2-console
- Health Check: http://localhost:8080/api/actuator/health
- API Docs (JSON): http://localhost:8080/api/api-docs

### H2 Console Credentials

**Dev profile:**
- JDBC URL: `jdbc:h2:mem:devdb`
- Username: `dev_user`
- Password: `dev_secure_password_2025`

**Default/test profile:**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## Architecture

### Project Structure

The microservice follows standard Spring Boot layered architecture:

```
microservicio-iso25010/src/main/java/com/ejemplo/
├── config/          # Configuration classes
│   ├── CorsConfig.java           # CORS settings
│   ├── DatabaseConfig.java       # DB configuration
│   ├── H2ConsoleConfig.java      # H2 console setup
│   └── SwaggerConfig.java        # API documentation
├── controller/      # REST endpoints (@RestController)
│   ├── UsuarioController.java    # /api/usuarios
│   ├── ProductoController.java   # /api/productos
│   └── PedidoController.java     # /api/pedidos
├── service/         # Business logic (@Service)
├── repository/      # Data access (@Repository, extends JpaRepository)
├── model/           # JPA entities (@Entity)
│   ├── Usuario.java
│   ├── Producto.java
│   └── Pedido.java
├── dto/             # Data transfer objects
└── exception/       # Error handling
    └── GlobalExceptionHandler.java  # @RestControllerAdvice
```

### Domain Model Relationships

**Pedido (Order) is the central entity:**
- `Pedido` has `@ManyToOne` relationship with `Usuario` (user who placed the order)
- `Pedido` has `@ManyToOne` relationship with `Producto` (product being ordered)
- Automatically calculates `total = precioUnitario × cantidad` using `@PrePersist` and `@PreUpdate` hooks
- Has `EstadoPedido` enum with states: PENDIENTE, CONFIRMADO, EN_PROCESO, ENVIADO, ENTREGADO, CANCELADO
- Automatically sets `fechaEntrega` when status changes to ENTREGADO

### Error Handling Pattern

All exceptions are centralized in `GlobalExceptionHandler` which returns standardized `ErrorDTO` responses:
- Validation errors → 400 BAD_REQUEST
- Entity not found → 404 NOT_FOUND
- Illegal state/conflict → 409 CONFLICT
- Method not allowed → 405 METHOD_NOT_ALLOWED
- Uncaught exceptions → 500 INTERNAL_SERVER_ERROR

### Configuration Profiles

Three profiles configured via `application-{profile}.yml`:

1. **dev** - Development (default via start-dev.bat)
   - H2 console enabled
   - Swagger enabled
   - DEBUG logging
   - Permissive CORS
   - Shows SQL queries

2. **test** - Testing
   - Uses `testdb` database
   - Clean state for each test run

3. **prod** - Production
   - H2 console disabled
   - Swagger disabled
   - INFO/WARN logging
   - Restricted CORS

### Environment Variables

All configuration uses environment variables for security. Key variables defined in `.env` (never commit this file):

**Database:**
- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`

**Features:**
- `H2_CONSOLE_ENABLED`, `SWAGGER_UI_ENABLED`

**CORS:**
- `CORS_ALLOWED_ORIGINS` (comma-separated URLs)

**Server:**
- `SERVER_PORT` (default: 8080)
- `CONTEXT_PATH` (default: /api)

**Logging:**
- `LOG_LEVEL_APP`, `LOG_LEVEL_WEB`, `LOG_LEVEL_SQL`

## CI/CD

### Jenkins Pipeline

The project includes a `Jenkinsfile` with the following stages:
1. Checkout from GitHub
2. Build (mvn clean compile)
3. Test (mvn test) - publishes JUnit results
4. Package (mvn package -DskipTests)
5. SonarQube Analysis (currently disabled, `when: expression { return false }`)
6. Build Docker Image
7. Deploy (docker run)

**Note:** Pipeline executes commands from `microservicio-iso25010` directory, not root.

### Docker

Use `docker-compose.yml` at root to run both Jenkins and the microservice:

```bash
docker-compose up -d
```

Services:
- Jenkins: http://localhost:8082
- Microservice: http://localhost:8080/api

## Important Notes

### UTF-8 Encoding

The application has extensive UTF-8 configuration to support Spanish characters (ñ, á, é, í, ó, ú):
- System properties set in `MicroservicioApplication.main()`
- `application.yml` has `spring.http.encoding` and `spring.servlet.encoding`
- Data initialization uses UTF-8 encoding

### Security Configuration

Spring Security is included but configured for basic/permissive access to allow H2 console and Swagger in dev. Check `config/` classes for CORS and security settings.

### Data Initialization

Initial test data loaded from `src/main/resources/data.sql` on startup (controlled by `spring.sql.init.mode=always`).

### Working Directory

When running Maven commands, ensure you're in the `microservicio-iso25010` subdirectory, not the repository root. The root contains Jenkins/Docker configuration, while the actual Spring Boot project is nested inside `microservicio-iso25010/`.
