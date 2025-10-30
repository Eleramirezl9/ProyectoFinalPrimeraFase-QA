# 🚀 Guía CI/CD - Microservicio ISO 25010

## 📋 Tabla de Contenidos

1. [Arquitectura CI/CD](#arquitectura-cicd)
2. [Configuración Inicial](#configuración-inicial)
3. [GitHub Actions](#github-actions)
4. [Jenkins](#jenkins)
5. [Docker Compose](#docker-compose)
6. [Comandos Rápidos](#comandos-rápidos)
7. [Troubleshooting](#troubleshooting)

---

## 🏗️ Arquitectura CI/CD

```
┌─────────────────────────────────────────────────────────────┐
│  PUSH a GitHub (main branch)                                │
└────────────┬────────────────────────────────────────────────┘
             │
             ├──────────────────────────────────────────────┐
             │                                              │
             ▼                                              ▼
┌────────────────────────┐                   ┌──────────────────────┐
│  GitHub Actions        │                   │  Jenkins (local)     │
│  ✓ Build + Test        │                   │  ✓ Build + Test      │
│  ✓ SonarQube Analysis  │                   │  ✓ Package           │
│  ✓ Quality Gate        │                   │  ✓ Docker Build      │
│  ✓ Coverage Report     │                   │  ✓ Deploy Local      │
└────────────────────────┘                   │  ✓ Health Checks     │
                                              │  ✓ Auto Rollback     │
                                              └──────────────────────┘
```

### Roles de Cada Sistema

| Sistema | Propósito | Cuándo se Ejecuta |
|---------|-----------|-------------------|
| **GitHub Actions** | Análisis de calidad (SonarQube) | Automático en cada push/PR |
| **Jenkins** | Build, test, deploy local | Manual o automático |
| **Docker Compose** | Orquestación de servicios | Según entorno (dev/ci/prod) |

---

## ⚙️ Configuración Inicial

### 1. Prerequisitos

```bash
# Verificar Docker
docker --version

# Verificar Docker Compose
docker-compose --version

# Verificar Git
git --version

# Verificar Java (para desarrollo local)
java -version
```

### 2. Variables de Entorno

El proyecto usa un archivo `.env` para configuración. **Nunca commitear este archivo.**

```bash
# En microservicio-iso25010/
cp .env.example .env
```

### 3. Configurar Secretos de GitHub

Para que GitHub Actions funcione correctamente:

1. Ve a tu repositorio en GitHub
2. **Settings** → **Secrets and variables** → **Actions**
3. Agregar los siguientes secretos:

| Nombre | Descripción | Dónde Obtenerlo |
|--------|-------------|-----------------|
| `SONAR_TOKEN` | Token de SonarCloud | https://sonarcloud.io/account/security |

---

## 🤖 GitHub Actions

### Workflow Actual: `.github/workflows/build.yml`

El workflow se ejecuta automáticamente en:
- Push a `main` o `develop`
- Pull Requests

### Jobs Incluidos

#### 1. **Build & Test** 🔨
```yaml
- Checkout del código
- Setup Java 17
- Cache de Maven
- Build con Maven
- Ejecutar tests
- Publicar resultados
- Generar cobertura
```

#### 2. **SonarQube Analysis** 🔍
```yaml
- Análisis de calidad de código
- Detección de bugs y vulnerabilidades
- Code smells
- Cobertura de código
```

#### 3. **Package** 📦 (solo en main)
```yaml
- Empaquetar JAR
- Upload de artifact
- Mantener por 30 días
```

#### 4. **Notify** 📢
```yaml
- Notificación de resultados
```

### Ver Resultados

1. Ve a tu repositorio en GitHub
2. Click en **Actions**
3. Selecciona el workflow más reciente
4. Ver cada job y sus logs

### SonarQube Dashboard

- URL: https://sonarcloud.io
- Proyecto: `Eleramirezl9_ProyectoFinalPrimeraFase-QA`
- Organización: `eleramirezl9`

---

## 🛠️ Jenkins

### Iniciar Jenkins

**Opción 1: Script automatizado (Recomendado)**
```bash
deploy.bat ci
```

**Opción 2: Docker Compose**
```bash
docker-compose -f docker-compose.ci.yml up -d
```

### Acceder a Jenkins

1. Abrir: http://localhost:8082
2. Primera vez:
   ```bash
   # Obtener password inicial
   docker exec jenkins-server cat /var/jenkins_home/secrets/initialAdminPassword
   ```
3. Instalar plugins recomendados
4. Crear usuario admin

### Configurar Pipeline

#### 1. Crear Nuevo Item
- **New Item** → **Pipeline** → Nombre: "Microservicio-ISO25010"

#### 2. Configurar Pipeline
```groovy
Pipeline → Definition: Pipeline script from SCM
SCM: Git
Repository URL: https://github.com/Eleramirezl9/ProyectoFinalPrimeraFase-QA.git
Branch: */main
Script Path: Jenkinsfile
```

#### 3. Configurar Maven
- **Manage Jenkins** → **Global Tool Configuration**
- **Maven** → Add Maven
  - Name: `Maven 3.9`
  - Install automatically: ✅

#### 4. (Opcional) Configurar SonarQube
- **Manage Jenkins** → **Manage Credentials**
- Agregar token de SonarCloud como `SONAR_TOKEN`

### Pipeline Stages

El Jenkinsfile incluye:

1. **Checkout** - Clonar código desde GitHub
2. **Build** - Compilar con Maven
3. **Test** - Ejecutar tests unitarios
4. **Package** - Crear JAR
5. **SonarQube** - Análisis de calidad (opcional)
6. **Build Docker** - Crear imagen Docker
7. **Deploy** - Desplegar contenedor con health checks

### Features del Pipeline

✅ **Cache de Maven** - Builds más rápidos
✅ **Health Checks** - Verifica que el deploy funcione
✅ **Rollback Automático** - Si el deploy falla, restaura versión anterior
✅ **Limpieza de Imágenes** - Mantiene solo últimas 3 versiones
✅ **Reportes de Tests** - JUnit reports visibles en Jenkins

### Ejecutar Pipeline

1. Ir a http://localhost:8082
2. Seleccionar el job
3. Click en **Build Now**
4. Ver progreso en **Console Output**

---

## 🐳 Docker Compose

### Configuraciones Disponibles

| Archivo | Propósito | Comando |
|---------|-----------|---------|
| `docker-compose.yml` | **Producción** | `deploy.bat prod` |
| `docker-compose.dev.yml` | **Desarrollo** | `deploy.bat dev` |
| `docker-compose.ci.yml` | **CI/CD (Jenkins)** | `deploy.bat ci` |

### Entorno: Desarrollo (`dev`)

```bash
deploy.bat dev
```

**Servicios incluidos:**
- Microservicio (puerto 8080)
- Adminer (puerto 8081) - Cliente de BD

**Características:**
- ✅ H2 Console habilitada
- ✅ Swagger habilitado
- ✅ Logs en DEBUG
- ✅ Hot reload (volúmenes montados)

**URLs:**
- API: http://localhost:8080/api
- Swagger: http://localhost:8080/api/swagger-ui.html
- H2 Console: http://localhost:8080/api/h2-console
- Adminer: http://localhost:8081

### Entorno: CI/CD (`ci`)

```bash
deploy.bat ci
```

**Servicios incluidos:**
- Jenkins (puerto 8082)
- Microservicio Staging (puerto 8080)

**Características:**
- ✅ Jenkins con Docker-in-Docker
- ✅ Maven cache compartido
- ✅ Microservicio en modo test

### Entorno: Producción (`prod`)

```bash
deploy.bat prod
```

**Servicios incluidos:**
- Jenkins (puerto 8082)
- Microservicio (puerto 8080)

**Características:**
- ❌ H2 Console deshabilitada
- ❌ Swagger deshabilitado
- ✅ Logs en INFO/WARN
- ✅ CORS restrictivo
- ✅ Health checks configurados

---

## ⚡ Comandos Rápidos

### Script Deploy.bat

```bash
# Iniciar desarrollo
deploy.bat dev

# Iniciar Jenkins + Staging
deploy.bat ci

# Iniciar producción
deploy.bat prod

# Detener todos los servicios
deploy.bat stop

# Ver logs
deploy.bat logs

# Reiniciar servicios
deploy.bat restart
```

### Docker Compose Manual

```bash
# Desarrollo
docker-compose -f docker-compose.dev.yml up -d
docker-compose -f docker-compose.dev.yml down

# CI/CD
docker-compose -f docker-compose.ci.yml up -d
docker-compose -f docker-compose.ci.yml down

# Producción
docker-compose up -d
docker-compose down

# Ver logs
docker-compose logs -f
docker-compose -f docker-compose.dev.yml logs -f microservicio-dev
```

### Jenkins

```bash
# Ver logs de Jenkins
docker logs -f jenkins-server

# Reiniciar Jenkins
docker restart jenkins-server

# Ver password inicial
docker exec jenkins-server cat /var/jenkins_home/secrets/initialAdminPassword
```

### Docker útiles

```bash
# Ver contenedores activos
docker ps

# Ver imágenes
docker images

# Limpiar imágenes sin usar
docker image prune -a

# Limpiar volúmenes sin usar
docker volume prune

# Ver uso de espacio
docker system df

# Limpieza completa
docker system prune -a --volumes
```

---

## 🔍 Troubleshooting

### Problema: Puerto ya en uso

**Error:**
```
Error starting userland proxy: listen tcp 0.0.0.0:8080: bind: address already in use
```

**Solución:**
```bash
# Windows: Ver qué usa el puerto
netstat -ano | findstr :8080

# Matar el proceso (reemplaza PID)
taskkill /PID <PID> /F

# O cambiar puerto en docker-compose.yml
ports:
  - "8081:8080"  # Usa 8081 en lugar de 8080
```

### Problema: Jenkins no inicia

**Verificar:**
```bash
# Ver logs
docker logs jenkins-server

# Verificar health check
docker inspect jenkins-server --format='{{.State.Health.Status}}'

# Reiniciar
docker restart jenkins-server
```

**Solución común:**
```bash
# Dar más memoria a Jenkins
# En docker-compose.yml o docker-compose.ci.yml:
environment:
  - JAVA_OPTS=-Xmx2g -Xms1g
```

### Problema: SonarQube falla en GitHub Actions

**Verificar:**
1. Token en GitHub Secrets: `SONAR_TOKEN`
2. Token válido en SonarCloud
3. Proyecto existe en SonarCloud

**Regenerar token:**
1. https://sonarcloud.io/account/security
2. Generate new token
3. Actualizar en GitHub Secrets

### Problema: Build falla en Jenkins

**Verificaciones:**

1. **Maven configurado:**
   - Manage Jenkins → Global Tool Configuration
   - Maven debe estar instalado como "Maven 3.9"

2. **Java correcto:**
   ```bash
   docker exec jenkins-server java -version
   # Debe ser Java 17
   ```

3. **Workspace limpio:**
   ```bash
   # En Jenkins, ejecutar:
   # Build → Execute shell
   mvn clean
   ```

### Problema: Docker out of space

```bash
# Ver uso
docker system df

# Limpiar
docker system prune -a --volumes

# Limpiar solo imágenes viejas
docker image prune -a --filter "until=24h"
```

### Problema: Tests fallan

```bash
# Ejecutar tests localmente
cd microservicio-iso25010
mvnw test

# Ver logs detallados
mvnw test -X

# Skip tests temporalmente (NO RECOMENDADO)
mvnw package -DskipTests
```

---

## 📊 Monitoreo

### Health Checks

```bash
# Microservicio
curl http://localhost:8080/api/actuator/health

# Jenkins
curl http://localhost:8082/login

# Docker health status
docker ps --format "table {{.Names}}\t{{.Status}}"
```

### Logs

```bash
# Todos los servicios
docker-compose logs -f

# Solo microservicio
docker logs -f microservicio-app

# Solo Jenkins
docker logs -f jenkins-server

# Últimas 100 líneas
docker logs --tail 100 microservicio-app
```

---

## 🔐 Seguridad

### Variables Sensibles

**❌ NUNCA commitear:**
- `.env`
- Passwords
- Tokens
- Claves privadas

**✅ SIEMPRE usar:**
- Variables de entorno
- GitHub Secrets
- Jenkins Credentials

### Producción

**Checklist antes de producción:**

- [ ] Cambiar `JWT_SECRET` en `.env`
- [ ] Configurar `CORS_ALLOWED_ORIGINS` correcto
- [ ] Deshabilitar H2 Console: `H2_CONSOLE_ENABLED=false`
- [ ] Deshabilitar Swagger: `SWAGGER_UI_ENABLED=false`
- [ ] Logs en INFO/WARN, no DEBUG
- [ ] Usar HTTPS en URLs públicas
- [ ] Configurar backup de base de datos
- [ ] Implementar base de datos real (no H2)

---

## 📚 Referencias

- [Documentación Spring Boot](https://spring.io/projects/spring-boot)
- [Jenkins Pipeline Syntax](https://www.jenkins.io/doc/book/pipeline/syntax/)
- [Docker Compose Reference](https://docs.docker.com/compose/compose-file/)
- [GitHub Actions Docs](https://docs.github.com/en/actions)
- [SonarQube Docs](https://docs.sonarqube.org/)

---

## 🆘 Soporte

**Problemas con el proyecto:**
- GitHub Issues: [Crear issue](https://github.com/Eleramirezl9/ProyectoFinalPrimeraFase-QA/issues)

**Documentación adicional:**
- [README.md](README.md) - Documentación general del proyecto
- [CLAUDE.md](CLAUDE.md) - Guía para Claude Code
