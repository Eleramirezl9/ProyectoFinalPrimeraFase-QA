# 🚀 Microservicio ISO/IEC 25010

> **⭐ CONFIGURACIÓN PROFESIONAL CON VARIABLES DE ENTORNO**

Microservicio REST con Spring Boot para evaluación de calidad de software ISO/IEC 25010.

## ✨ Características Principales

- ✅ **Variables de Entorno** - Configuración segura y flexible
- ✅ **Multi-Ambiente** - Desarrollo, Testing, Producción
- ✅ **CORS Configurable** - Seguro por ambiente
- ✅ **API REST Completa** - Usuarios, Productos, Pedidos
- ✅ **Documentación Swagger** - Auto-generada
- ✅ **Base de Datos H2** - Con migración a PostgreSQL/MySQL

## 🚀 Inicio Rápido

```bash
# 1. Clonar proyecto
git clone https://github.com/Eleramirezl9/ProyectoFinalPrimeraFase-QA.git
cd \"EntregaFase I/microservicio-iso25010\"

# 2. Configurar entorno
cp .env.example .env

# 3. Ejecutar
./mvnw spring-boot:run
```

## 🌐 URLs Importantes

- **API**: http://localhost:8080/api
- **Swagger**: http://localhost:8080/api/swagger-ui.html
- **H2 Console**: http://localhost:8080/api/h2-console
- **Health**: http://localhost:8080/api/actuator/health

## 📊 Configuración Segura

```bash
# ⚠️ NOTA DE SEGURIDAD: Variables configuradas por ambiente
# Ver scripts específicos para valores reales

# CORS - URLs permitidas
CORS_ALLOWED_ORIGINS=[CONFIGURAR_SEGUN_AMBIENTE]

# Base de Datos - Credenciales seguras
DB_USERNAME=[CONFIGURAR_LOCALMENTE]
DB_PASSWORD=[CONFIGURAR_LOCALMENTE]

# Desarrollo
H2_CONSOLE_ENABLED=true    # Solo desarrollo
SWAGGER_UI_ENABLED=true    # Solo desarrollo
LOG_LEVEL_APP=DEBUG        # Desarrollo: DEBUG, Prod: INFO
```

### 🎯 Métodos de Ejecución

```cmd
# Método 1: Script con variables (RECOMENDADO)
start-dev.bat

# Método 2: Perfil desarrollo
mvnw.cmd spring-boot:run -Dspring.profiles.active=dev

# Método 3: Validación del sistema
test-env-vars.bat
```

## 🔍 Endpoints Principales

### Usuarios
- `GET/POST /api/usuarios`
- `GET/PUT/DELETE /api/usuarios/{id}`

### Productos  
- `GET/POST /api/productos`
- `GET/PUT/DELETE /api/productos/{id}`

### Pedidos
- `GET/POST /api/pedidos`
- `GET/PUT/DELETE /api/pedidos/{id}`

## 🛡️ Seguridad por Ambiente

### Desarrollo
- H2 Console: ✅ Habilitado
- Swagger: ✅ Habilitado
- Logs: DEBUG

### Producción
- H2 Console: ❌ Deshabilitado
- Swagger: ❌ Deshabilitado  
- Logs: INFO/WARN

## 📚 Documentación Detallada

- **🔴 [Guía de Seguridad](microservicio-iso25010/ENVIRONMENT_SECURITY_GUIDE.md)** - CRÍTICO
- **🚀 [Métodos de Ejecución](microservicio-iso25010/EXECUTION_METHODS_GUIDE.md)** - Scripts validados
- **🔍 [Validación de Configuración](microservicio-iso25010/CONFIGURATION_VALIDATION_GUIDE.md)** - Tests
- [CORS](microservicio-iso25010/CORS_DOCUMENTATION.md) - Configuración CORS
- [Base de Datos](microservicio-iso25010/DATABASE_DOCUMENTATION.md) - DB Setup
- [API Completa](documentacion-PDF/API_Documentation%20completo.pdf) - Casos de prueba

## ⚠️ Importante

### ✅ Hacer Siempre:
1. Copiar `.env.example` a `.env`
2. Leer guía de seguridad antes de producción
3. Verificar que `.env` está en `.gitignore`

### ❌ Nunca Hacer:
1. Commitear archivos `.env`
2. Usar H2 Console en producción
3. Hardcodear credenciales

## 🧪 Testing

```bash
# Tests normales
./mvnw test

# Tests con perfil específico
SPRING_PROFILES_ACTIVE=test ./mvnw test
```

## 🏭 Producción

```bash
# Variables seguras del servidor
export CORS_ALLOWED_ORIGINS=\"https://mi-app.com\"
export DB_PASSWORD=\"${SECRET_MANAGER_PASSWORD}\"
export H2_CONSOLE_ENABLED=false
export SWAGGER_UI_ENABLED=false

# Ejecutar con perfil de producción
SPRING_PROFILES_ACTIVE=prod ./mvnw spring-boot:run
```

## 👥 Equipo

**Universidad Mariano Gálvez**  
Curso: Aseguramiento de la Calidad de Software  
Proyecto: ISO/IEC 25010 con Variables de Entorno

---

**🎖️ Certificado Profesional**: Variables de Entorno Seguras | Multi-Ambiente | Documentación Completa