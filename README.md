# 🚀 Microservicio ISO/IEC 25010

> **Sistema de gestión de Usuarios, Productos y Pedidos con Spring Boot**

API REST desarrollada con Spring Boot para la evaluación de calidad de software según el estándar ISO/IEC 25010.

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

### 👥 Usuarios
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/usuarios` | Obtener todos los usuarios |
| POST | `/api/usuarios` | Crear un nuevo usuario |
| GET | `/api/usuarios/{id}` | Obtener un usuario específico |
| PUT | `/api/usuarios/{id}` | Actualizar un usuario |
| DELETE | `/api/usuarios/{id}` | Eliminar un usuario |

### 📦 Productos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/productos` | Obtener todos los productos |
| POST | `/api/productos` | Crear un nuevo producto |
| GET | `/api/productos/{id}` | Obtener un producto específico |
| PUT | `/api/productos/{id}` | Actualizar un producto |
| DELETE | `/api/productos/{id}` | Eliminar un producto |

### 🛒 Pedidos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/pedidos` | Obtener todos los pedidos |
| POST | `/api/pedidos` | Crear un nuevo pedido |
| GET | `/api/pedidos/{id}` | Obtener un pedido específico |
| PUT | `/api/pedidos/{id}` | Actualizar un pedido |
| DELETE | `/api/pedidos/{id}` | Eliminar un pedido |

> 💡 **Tip**: Para ver todos los endpoints disponibles y probarlos, usa **Swagger UI**: http://localhost:8080/api/swagger-ui.html

---

## 🗄️ Base de Datos

### Acceder a la Consola H2

1. Abre: http://localhost:8080/api/h2-console
2. Configuración de conexión:
   - **JDBC URL**: `jdbc:h2:mem:devdb` (desarrollo) o `jdbc:h2:mem:testdb` (por defecto)
   - **Username**: `dev_user` (desarrollo) o `sa` (por defecto)
   - **Password**: `dev_secure_password_2025` (desarrollo) o `password` (por defecto)

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

## 🔒 Seguridad

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

## 🐛 Solución de Problemas

### El puerto 8080 ya está en uso
```bash
# Cambiar el puerto en .env
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
