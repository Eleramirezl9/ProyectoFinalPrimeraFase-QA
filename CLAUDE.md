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

⚠️ **IMPORTANTE**: Las credenciales deben configurarse en el archivo `.env`

**Configuración requerida en `.env`:**
- `DB_URL` - URL de conexión JDBC
- `DB_USERNAME` - Usuario de la base de datos
- `DB_PASSWORD` - Contraseña de la base de datos

**Ejemplo para desarrollo:**
```
DB_URL=jdbc:h2:mem:devdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
DB_USERNAME=<tu-usuario>
DB_PASSWORD=<tu-password-seguro>
```

Ver [.env.example](microservicio-iso25010/.env.example) para más detalles.

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

### Architecture Overview

The project has a clean CI/CD architecture with three complementary systems:

```
GitHub Actions (Cloud)     →  Quality & Analysis
    ├─ Build & Test
    ├─ SonarQube Analysis
    └─ Coverage Reports

Jenkins (Local)            →  Build & Deployment
    ├─ Maven Build
    ├─ Docker Image Creation
    ├─ Local Deployment
    └─ Health Checks + Rollback

Docker Compose             →  Orchestration
    ├─ docker-compose.yml       (Production)
    ├─ docker-compose.dev.yml   (Development)
    └─ docker-compose.ci.yml    (CI/CD)
```

### Quick Start

**Automated deployment script:**
```bash
# Development environment
deploy.bat dev

# CI/CD environment (Jenkins + Staging)
deploy.bat ci

# Production environment
deploy.bat prod

# Stop all services
deploy.bat stop

# View logs
deploy.bat logs
```

### Jenkins Pipeline

The `Jenkinsfile` includes:
1. **Checkout** - Clone from GitHub
2. **Build** - Maven clean compile
3. **Test** - Unit tests with JUnit reports
4. **Package** - Create JAR artifact
5. **SonarQube** - Code quality analysis (optional, runs if SONAR_TOKEN configured)
6. **Build Docker** - Multi-stage Docker image
7. **Deploy** - Deploy with health checks and automatic rollback on failure

**Key Features:**
- ✅ Maven caching for faster builds
- ✅ Health checks after deployment
- ✅ Automatic rollback if deployment fails
- ✅ Cleanup of old Docker images (keeps last 3)
- ✅ JUnit test reports in Jenkins UI

**Note:** Pipeline executes commands from `microservicio-iso25010` directory.

### GitHub Actions

Workflow file: `.github/workflows/build.yml`

**Jobs:**
1. **build-and-test** - Build, test, generate coverage reports
2. **sonarqube** - Code quality analysis with SonarCloud
3. **package** - Create JAR artifact (only on main branch)
4. **notify** - Build status notification

**Triggers:**
- Push to `main` or `develop` branches
- Pull requests

### Docker Compose Configurations

Three separate configurations for different environments:

#### Production (`docker-compose.yml`)
```bash
docker-compose up -d
```
- Jenkins on port 8082
- Microservicio on port 8080
- Profile: `prod`
- H2 Console: Disabled
- Swagger: Disabled

#### Development (`docker-compose.dev.yml`)
```bash
docker-compose -f docker-compose.dev.yml up -d
```
- Microservicio on port 8080
- Adminer (DB client) on port 8081
- Profile: `dev`
- H2 Console: Enabled
- Swagger: Enabled
- Hot reload with mounted volumes

#### CI/CD (`docker-compose.ci.yml`)
```bash
docker-compose -f docker-compose.ci.yml up -d
```
- Jenkins on port 8082
- Microservicio Staging on port 8080
- Profile: `test`
- Docker-in-Docker for builds

### URLs

**Development:**
- API: http://localhost:8080/api
- Swagger: http://localhost:8080/api/swagger-ui.html
- H2 Console: http://localhost:8080/api/h2-console
- Adminer: http://localhost:8081

**CI/CD:**
- Jenkins: http://localhost:8082
- Staging API: http://localhost:8080/api

**Production:**
- Jenkins: http://localhost:8082
- API: http://localhost:8080/api (Swagger disabled)

### Complete Documentation

For detailed CI/CD setup, troubleshooting, and best practices, see:
📖 **[CI-CD.md](CI-CD.md)** - Complete CI/CD Guide

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
