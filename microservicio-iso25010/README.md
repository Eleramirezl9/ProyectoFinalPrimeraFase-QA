# Microservicio ISO/IEC 25010

## Descripción
Microservicio REST desarrollado con Spring Boot para la evaluación de calidad de software según la norma ISO/IEC 25010. Este proyecto implementa un sistema de gestión de usuarios, productos y pedidos con documentación completa y casos de prueba funcionales.

## Características Principales
- ✅ API REST con operaciones CRUD completas
- ✅ Documentación automática con Swagger/OpenAPI
- ✅ Base de datos H2 en memoria
- ✅ Manejo global de excepciones
- ✅ Validación de datos
- ✅ Casos de prueba funcionales
- ✅ Evaluación de calidad ISO/IEC 25010

## Tecnologías Utilizadas
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database**
- **SpringDoc OpenAPI 3**
- **Maven**

## Estructura del Proyecto
```
microservicio-iso25010/
├── src/
│   ├── main/
│   │   ├── java/com/ejemplo/
│   │   │   ├── config/          # Configuraciones
│   │   │   ├── controller/      # Controladores REST
│   │   │   ├── dto/            # DTOs para requests/responses
│   │   │   ├── exception/      # Manejo de errores
│   │   │   ├── model/          # Entidades JPA
│   │   │   ├── repository/     # Repositorios JPA
│   │   │   ├── service/        # Servicios de negocio
│   │   │   └── MicroservicioApplication.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── data.sql
│   │       └── messages.properties
│   └── test/                   # Pruebas unitarias
├── docs/                       # Documentación
├── pom.xml
└── README.md
```

## Requisitos Previos
- Java 17 o superior
- Maven 3.6 o superior
- IDE compatible (IntelliJ IDEA, Eclipse, VS Code)

## Instalación y Ejecución

### 1. Clonar el repositorio
```bash
git clone <url-del-repositorio>
cd microservicio-iso25010
```

### 2. Compilar el proyecto
```bash
mvn clean compile
```

### 3. Ejecutar las pruebas
```bash
mvn test
```

### 4. Ejecutar la aplicación
```bash
mvn spring-boot:run
```

### 5. Acceder a la aplicación
- **API Base URL**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **H2 Console**: http://localhost:8080/api/h2-console
- **API Docs JSON**: http://localhost:8080/api/api-docs

## Configuración de H2 Console
Para acceder a la consola H2:
- **URL**: `jdbc:h2:mem:testdb`
- **Usuario**: `sa`
- **Contraseña**: `password`

## Endpoints Principales

### Usuarios
- `GET /api/usuarios` - Listar todos los usuarios
- `GET /api/usuarios/{id}` - Obtener usuario por ID
- `POST /api/usuarios` - Crear nuevo usuario
- `PUT /api/usuarios/{id}` - Actualizar usuario
- `DELETE /api/usuarios/{id}` - Eliminar usuario

### Productos
- `GET /api/productos` - Listar todos los productos
- `GET /api/productos/{id}` - Obtener producto por ID
- `POST /api/productos` - Crear nuevo producto
- `PUT /api/productos/{id}` - Actualizar producto
- `DELETE /api/productos/{id}` - Eliminar producto

### Pedidos
- `GET /api/pedidos` - Listar todos los pedidos
- `GET /api/pedidos/{id}` - Obtener pedido por ID
- `POST /api/pedidos` - Crear nuevo pedido
- `PUT /api/pedidos/{id}` - Actualizar pedido
- `DELETE /api/pedidos/{id}` - Eliminar pedido

## Documentación Adicional
- [Casos de Prueba](docs/Casos_Prueba.pdf)
- [Documentación API](docs/API_Documentation.pdf)
- [Evaluación ISO 25010](docs/Evaluacion_ISO_25010.pdf)

## Autor
**Estudiante Universidad Mariano Gálvez**
- Curso: Aseguramiento de la Calidad de Software
- Proyecto: Evaluación ISO/IEC 25010

## Licencia
Este proyecto es desarrollado con fines académicos.

