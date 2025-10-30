# Microservicio ISO/IEC 25010 - Proyecto de Aseguramiento de la Calidad

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.12-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-Academic-blue.svg)]()

> Proyecto final del curso de Aseguramiento de la Calidad
> Universidad Mariano Gálvez de Guatemala

API REST desarrollada con Spring Boot que implementa un sistema de gestión de usuarios, productos y pedidos, evaluada bajo los estándares de calidad ISO/IEC 25010.

---

## 📋 ¿Qué es este proyecto?

Este es un **microservicio web** que proporciona una API REST completa para:
- 👥 **Gestión de Usuarios** - Crear, consultar, actualizar y eliminar usuarios
- 📦 **Catálogo de Productos** - Administrar productos con stock, precios y categorías
- 🛒 **Sistema de Pedidos** - Procesar pedidos que relacionan usuarios con productos

### Tecnologías Utilizadas

- **Java 17** - Lenguaje de programación
- **Spring Boot 3.2.12** - Framework principal
- **H2 Database** - Base de datos en memoria (para desarrollo)
- **Maven** - Gestor de dependencias
- **Swagger/OpenAPI** - Documentación automática de la API
- **Spring Security** - Seguridad básica

---

## 🚀 Cómo Empezar (Inicio Rápido)

### Paso 1: Clonar el Proyecto

```bash
git clone https://github.com/Eleramirezl9/ProyectoFinalPrimeraFase-QA.git
cd "EntregaFase I/microservicio-iso25010"
```

### Paso 2: Configurar Variables de Entorno

El proyecto usa un archivo `.env` para configuración segura:

```bash
# Copia el archivo de ejemplo
cp .env.example .env

# Edita .env con tus valores (opcional, los valores por defecto funcionan)
```

### Paso 3: Ejecutar el Proyecto

**Opción A - Usando el script (RECOMENDADO para Windows):**
```cmd
start-dev.bat
```

**Opción B - Usando Maven directamente:**
```cmd
mvnw.cmd spring-boot:run
```

**Opción C - Si tienes Maven instalado globalmente:**
```bash
mvn spring-boot:run
```

### Paso 4: Acceder a la Aplicación

Una vez iniciado, abre tu navegador y visita:

- 🌐 **API Base**: http://localhost:8080/api
- 📚 **Documentación Swagger**: http://localhost:8080/api/swagger-ui.html
- 🗄️ **Consola H2** (Base de datos): http://localhost:8080/api/h2-console
- ❤️ **Health Check**: http://localhost:8080/api/actuator/health

---

## 📡 Endpoints de la API

### 🔐 Autenticación (JWT)
| Método | Endpoint | Descripción | Acceso |
|--------|----------|-------------|--------|
| POST | `/api/auth/register` | Registrar nuevo usuario | Público |
| POST | `/api/auth/login` | Iniciar sesión (obtener token) | Público |

**Ejemplo de Login:**
```json
POST /api/auth/login
{
  "username": "admin",
  "password": "<tu-password>"
}
```

⚠️ **NOTA**: Consultar documentación interna para credenciales de prueba

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzM4NCJ9...",
  "refreshToken": "eyJhbGciOiJIUzM4NCJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "admin",
  "roles": ["ADMIN"],
  "expiresIn": 86400000
}
```

### 👥 Usuarios
| Método | Endpoint | Descripción | Roles Permitidos |
|--------|----------|-------------|------------------|
| GET | `/api/usuarios` | Obtener todos los usuarios | CLIENTE, MANAGER, ADMIN |
| POST | `/api/usuarios` | Crear un nuevo usuario | MANAGER, ADMIN |
| GET | `/api/usuarios/{id}` | Obtener un usuario específico | CLIENTE, MANAGER, ADMIN |
| PUT | `/api/usuarios/{id}` | Actualizar un usuario | MANAGER, ADMIN |
| PATCH | `/api/usuarios/{id}/roles` | **Asignar roles a usuario** | **ADMIN** |
| DELETE | `/api/usuarios/{id}` | Eliminar un usuario | ADMIN |

**Ejemplo - Asignar Roles (Solo ADMIN):**
```json
PATCH /api/usuarios/5/roles
Authorization: Bearer {token}
{
  "roles": ["MANAGER"]
}
```

### 📦 Productos
| Método | Endpoint | Descripción | Roles Permitidos |
|--------|----------|-------------|------------------|
| GET | `/api/productos` | Obtener todos los productos | CLIENTE, MANAGER, ADMIN |
| POST | `/api/productos` | Crear un nuevo producto | MANAGER, ADMIN |
| GET | `/api/productos/{id}` | Obtener un producto específico | CLIENTE, MANAGER, ADMIN |
| PUT | `/api/productos/{id}` | Actualizar un producto | MANAGER, ADMIN |
| DELETE | `/api/productos/{id}` | Eliminar un producto | ADMIN |

### 🛒 Pedidos
| Método | Endpoint | Descripción | Roles Permitidos |
|--------|----------|-------------|------------------|
| GET | `/api/pedidos` | Obtener todos los pedidos | CLIENTE, MANAGER, ADMIN |
| POST | `/api/pedidos` | Crear un nuevo pedido | CLIENTE, MANAGER, ADMIN |
| GET | `/api/pedidos/{id}` | Obtener un pedido específico | CLIENTE, MANAGER, ADMIN |
| PUT | `/api/pedidos/{id}` | Actualizar un pedido | MANAGER, ADMIN |
| DELETE | `/api/pedidos/{id}` | Eliminar un pedido | ADMIN |

> 💡 **Tip**: Para ver todos los endpoints disponibles y probarlos, usa **Swagger UI**: http://localhost:8080/api/swagger-ui.html

---

## 🗄️ Base de Datos

### Acceder a la Consola H2

1. Abre: http://localhost:8080/api/h2-console
2. Configuración de conexión:
   - Las credenciales deben estar configuradas en el archivo `.env`
   - Ver [.env.example](microservicio-iso25010/.env.example) para la configuración requerida

⚠️ **IMPORTANTE**: Nunca usar credenciales por defecto en producción

### Datos Iniciales

El proyecto incluye datos de prueba que se cargan automáticamente al iniciar:
- Usuarios de ejemplo
- Productos de muestra
- Pedidos de prueba

Estos datos están definidos en: `src/main/resources/data.sql`

---

## ⚙️ Configuración Avanzada

### Variables de Entorno Principales

El proyecto usa variables de entorno para mayor seguridad y flexibilidad:

**Base de Datos:**
- `DB_URL` - URL de conexión a la base de datos
- `DB_USERNAME` - Usuario de la base de datos
- `DB_PASSWORD` - Contraseña de la base de datos

**CORS (Seguridad Web):**
- `CORS_ALLOWED_ORIGINS` - URLs permitidas (ejemplo: `http://localhost:3000,http://localhost:4200`)

**Características:**
- `H2_CONSOLE_ENABLED` - Habilitar/deshabilitar consola H2 (`true`/`false`)
- `SWAGGER_UI_ENABLED` - Habilitar/deshabilitar Swagger (`true`/`false`)

**Logging:**
- `LOG_LEVEL_APP` - Nivel de logs (`DEBUG`, `INFO`, `WARN`, `ERROR`)

### Perfiles de Ejecución

El proyecto tiene 3 perfiles configurados:

**1. Desarrollo (`dev`)** - Para desarrollar localmente
```bash
mvnw.cmd spring-boot:run -Dspring.profiles.active=dev
```
- ✅ H2 Console habilitada
- ✅ Swagger habilitado
- ✅ Logs en DEBUG
- ✅ CORS permisivo

**2. Testing (`test`)** - Para pruebas
```bash
SPRING_PROFILES_ACTIVE=test mvnw.cmd test
```

**3. Producción (`prod`)** - Para servidor real
```bash
mvnw.cmd spring-boot:run -Dspring.profiles.active=prod
```
- ❌ H2 Console deshabilitada
- ❌ Swagger deshabilitado
- 📝 Logs en INFO/WARN
- 🔒 CORS restrictivo

---

## 🧪 Testing

### Ejecutar Tests

```bash
# Tests normales
mvnw.cmd test

# Tests con perfil específico
set SPRING_PROFILES_ACTIVE=test && mvnw.cmd test
```

---

## 🔒 Seguridad y Autenticación

### 🔐 Sistema de Autenticación JWT

El microservicio implementa autenticación basada en **JSON Web Tokens (JWT)** con los siguientes componentes:

#### **Tokens:**
- **Access Token**: Expira en 24 horas (configurable via `JWT_EXPIRATION`)
- **Refresh Token**: Expira en 7 días (configurable via `JWT_REFRESH_TOKEN_EXPIRATION`)

#### **Configuración JWT:**
Variables de entorno en `.env`:
```env
JWT_SECRET=bXljb21wbGV4c2VjcmV0a2V5...
JWT_EXPIRATION=86400000          # 24 horas en milisegundos
JWT_REFRESH_TOKEN_EXPIRATION=604800000  # 7 días
```

#### **Cómo autenticarse:**
1. **Login** para obtener token:
   ```bash
   POST /api/auth/login
   {
     "username": "admin",
     "password": "<tu-password>"
   }
   ```

   ⚠️ **NOTA**: Consultar documentación interna para credenciales de prueba

2. **Usar el token** en siguientes peticiones:
   ```bash
   GET /api/usuarios
   Authorization: Bearer eyJhbGciOiJIUzM4NCJ9...
   ```

### 👤 Sistema de Roles y Permisos

El sistema implementa **3 roles** con diferentes niveles de acceso:

| Rol | Descripción | Permisos |
|-----|-------------|----------|
| **CLIENTE** | Usuario regular | ✅ Leer usuarios, productos, pedidos<br>✅ Crear pedidos |
| **MANAGER** | Gestor | ✅ Todo lo de CLIENTE<br>✅ Crear/actualizar usuarios y productos<br>✅ Actualizar pedidos |
| **ADMIN** | Administrador | ✅ Acceso total al sistema<br>✅ Eliminar cualquier entidad<br>✅ **Asignar roles a usuarios** |

#### **Usuarios de Prueba:**

⚠️ **IMPORTANTE**: Consultar documentación interna del proyecto para obtener credenciales de prueba

| Username | Rol |
|----------|-----|
| `admin` | ADMIN |
| `superadmin` | ADMIN |
| `mrodriguez` | MANAGER |
| `jgarcia` | CLIENTE |

Las credenciales están encriptadas con BCrypt en `data.sql`

#### **Cambiar Roles (Solo ADMIN):**
```bash
PATCH /api/usuarios/{id}/roles
Authorization: Bearer {admin_token}
Content-Type: application/json

{
  "roles": ["MANAGER"]
}
```

**Roles válidos:** `ADMIN`, `MANAGER`, `CLIENTE`

### ✅ Buenas Prácticas (HACER SIEMPRE)

1. **No hardcodear credenciales** - Usar siempre variables de entorno
2. **Copiar `.env.example` a `.env`** antes de empezar
3. **Verificar `.env` en `.gitignore`** - Nunca commitear credenciales
4. **Usar HTTPS en producción** - Para `CORS_ALLOWED_ORIGINS`
5. **Deshabilitar H2 Console en producción** - `H2_CONSOLE_ENABLED=false`

### ❌ Nunca Hacer

1. ❌ Commitear archivos `.env` al repositorio
2. ❌ Usar H2 Console en producción
3. ❌ Dejar Swagger habilitado en producción
4. ❌ Hardcodear contraseñas en el código

---

## 📁 Estructura del Proyecto

```
microservicio-iso25010/
│
├── src/
│   ├── main/
│   │   ├── java/com/ejemplo/
│   │   │   ├── controller/      # Endpoints REST
│   │   │   ├── service/         # Lógica de negocio
│   │   │   ├── repository/      # Acceso a datos
│   │   │   ├── model/           # Entidades (Usuario, Producto, Pedido)
│   │   │   ├── dto/             # Objetos de transferencia
│   │   │   ├── config/          # Configuraciones (CORS, DB, Security)
│   │   │   └── exception/       # Manejo de errores
│   │   │
│   │   └── resources/
│   │       ├── application.yml          # Configuración principal
│   │       ├── application-dev.yml      # Configuración desarrollo
│   │       ├── application-test.yml     # Configuración testing
│   │       ├── application-prod.yml     # Configuración producción
│   │       └── data.sql                 # Datos iniciales
│   │
│   └── test/                    # Tests (por implementar)
│
├── .env.example                 # Plantilla de variables de entorno
├── .gitignore                   # Archivos ignorados por Git
├── pom.xml                      # Dependencias Maven
├── mvnw / mvnw.cmd             # Maven Wrapper
└── start-dev.bat               # Script de inicio rápido (Windows)
```

---

## 🛠️ Comandos Útiles

### Maven

```bash
# Compilar proyecto
mvnw.cmd clean compile

# Ejecutar aplicación
mvnw.cmd spring-boot:run

# Ejecutar tests
mvnw.cmd test

# Empaquetar (crear JAR)
mvnw.cmd package

# Limpiar compilación
mvnw.cmd clean
```

### Git

```bash
# Ver estado
git status

# Ver cambios
git diff

# Hacer commit
git add .
git commit -m "Descripción del cambio"

# Subir cambios
git push origin main
```

---

## 📚 Documentación Adicional

- **Swagger UI** - Documentación interactiva en: http://localhost:8080/api/swagger-ui.html
- **API Docs (JSON)** - http://localhost:8080/api/api-docs
- **Health Check** - http://localhost:8080/api/actuator/health

---

## 🚀 CI/CD y Deployment

### Scripts de Deployment

El proyecto incluye scripts automatizados para diferentes entornos:

```bash
# Desarrollo local con hot reload
deploy.bat dev

# Jenkins + Microservicio staging
deploy.bat ci

# Producción
deploy.bat prod

# Detener todos los servicios
deploy.bat stop
```

### Docker Compose

Tres configuraciones disponibles:

| Archivo | Entorno | Uso |
|---------|---------|-----|
| `docker-compose.yml` | Producción | Jenkins + App |
| `docker-compose.dev.yml` | Desarrollo | App + Adminer |
| `docker-compose.ci.yml` | CI/CD | Jenkins + Staging |

**Iniciar con Docker:**
```bash
# Desarrollo
docker-compose -f docker-compose.dev.yml up -d

# Producción
docker-compose up -d
```

### GitHub Actions

Pipeline automático que se ejecuta en cada push:
- ✅ Build y compilación
- ✅ Tests unitarios
- ✅ Análisis SonarQube
- ✅ Reporte de cobertura
- ✅ Empaquetado (solo en main)

Ver: [.github/workflows/build.yml](.github/workflows/build.yml)

### Jenkins

Pipeline local para build y deploy:
- 🔨 Build con Maven
- 🧪 Tests con reportes JUnit
- 📦 Empaquetado JAR
- 🐳 Build de imagen Docker
- 🚀 Deploy con health checks
- 🔙 Rollback automático si falla

Ver: [Jenkinsfile](Jenkinsfile)

**Acceder a Jenkins:**
- URL: http://localhost:8082
- Iniciar: `deploy.bat ci`

### Documentación Detallada

📖 **[Ver guía completa de CI/CD →](CI-CD.md)**

La guía incluye:
- Configuración paso a paso
- Troubleshooting
- Mejores prácticas
- Comandos útiles

---

## 🐛 Solución de Problemas

### El puerto 8080 ya está en uso
```bash
# Windows: Ver qué usa el puerto
netstat -ano | findstr :8080

# Matar el proceso
taskkill /PID <PID> /F

# O cambiar el puerto en .env
SERVER_PORT=8081
```

### Error de conexión a la base de datos
- Verifica las credenciales en `.env`
- Revisa la consola H2: http://localhost:8080/api/h2-console

### Maven no funciona
```bash
# Usar el wrapper incluido
./mvnw.cmd clean install
```

### CORS bloqueando peticiones
- Agrega tu URL frontend a `CORS_ALLOWED_ORIGINS` en `.env`:
```
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:4200
```

### Docker: Puerto ya en uso
```bash
# Detener servicios Docker
deploy.bat stop

# O manualmente
docker-compose down
docker-compose -f docker-compose.dev.yml down
docker-compose -f docker-compose.ci.yml down
```

---

## 👥 Equipo de Desarrollo

**Universidad Mariano Gálvez de Guatemala**
**Curso:** Aseguramiento de la Calidad de Software
**Proyecto:** Microservicio ISO/IEC 25010

---

## 📄 Licencia

Este proyecto es desarrollado con fines académicos para la Universidad Mariano Gálvez.

---

## 🔗 Enlaces Útiles

- [Documentación Spring Boot](https://spring.io/projects/spring-boot)
- [Swagger/OpenAPI](https://swagger.io/specification/)
- [ISO/IEC 25010](https://iso25000.com/index.php/normas-iso-25000/iso-25010)
- [Maven](https://maven.apache.org/)

---

**💡 Tip Final**: Usa Swagger UI (http://localhost:8080/api/swagger-ui.html) para probar todos los endpoints de manera interactiva sin necesidad de herramientas adicionales como Postman.
