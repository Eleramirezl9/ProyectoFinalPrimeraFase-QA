# 🚀 Guía CI/CD - Microservicio ISO 25010

## 📋 Tabla de Contenidos

1. [Arquitectura CI/CD](#arquitectura-cicd)
2. [GitHub Actions](#github-actions)
3. [SonarCloud](#sonarcloud)
4. [Comandos Útiles](#comandos-útiles)
5. [Troubleshooting](#troubleshooting)

---

## 🏗️ Arquitectura CI/CD

```
┌─────────────────────────────────────────────────────────────┐
│  PUSH a GitHub (main/develop)                               │
└────────────┬────────────────────────────────────────────────┘
             │
             ▼
┌────────────────────────────────────────────────────────────┐
│  GitHub Actions Workflow                                   │
│  ✓ Build con Maven                                         │
│  ✓ Tests unitarios (JUnit)                                 │
│  ✓ Cobertura de código (JaCoCo)                            │
│  ✓ Análisis SonarCloud                                     │
│  ✓ Quality Gate                                            │
│  ✓ Publicación de reportes                                 │
└────────────────────────────────────────────────────────────┘
```

### Stack Tecnológico CI/CD

| Herramienta | Propósito |
|-------------|-----------|
| **GitHub Actions** | Pipeline CI/CD automatizado |
| **SonarCloud** | Análisis de calidad y seguridad |
| **JaCoCo** | Cobertura de código |
| **Maven** | Build y gestión de dependencias |
| **JUnit** | Tests unitarios |

---

## ⚙️ GitHub Actions

### Configuración del Workflow

El workflow está definido en `.github/workflows/build.yml`

### Jobs del Pipeline

#### 1. **Build & Test**
```yaml
- Checkout del código
- Setup JDK 17
- Cache de Maven
- Build con Maven
- Ejecución de tests
- Generación de reportes JaCoCo
- Upload a Codecov
```

#### 2. **SonarQube Analysis**
```yaml
- Análisis de calidad de código
- Detección de bugs y vulnerabilidades
- Verificación de cobertura
- Quality Gate check
```

#### 3. **Package** (solo en main)
```yaml
- Empaquetado JAR
- Upload de artefactos
```

### Triggers

El workflow se ejecuta en:
- Push a `main` o `develop`
- Pull requests

### Secrets Requeridos

Configurar en GitHub → Settings → Secrets:

```bash
SONAR_TOKEN=<tu-token-de-sonarcloud>
```

---

## 🔍 SonarCloud

### Configuración

1. **Crear cuenta en SonarCloud**: https://sonarcloud.io
2. **Vincular repositorio de GitHub**
3. **Obtener token de autenticación**
4. **Agregar secrets en GitHub**

### Métricas Evaluadas

- **Bugs**: Errores en el código
- **Vulnerabilities**: Problemas de seguridad
- **Code Smells**: Deuda técnica
- **Coverage**: Cobertura de tests (≥80%)
- **Duplications**: Código duplicado (<3%)
- **Security Hotspots**: Puntos sensibles de seguridad

### Quality Gate

Condiciones para pasar:

| Métrica | Requisito |
|---------|-----------|
| Coverage on New Code | ≥ 80% |
| Duplicated Lines | ≤ 3% |
| Bugs | 0 |
| Vulnerabilities | 0 |

---

## 🛠️ Comandos Útiles

### Build Local

```bash
cd microservicio-iso25010

# Compilar
./mvnw.cmd clean compile

# Tests
./mvnw.cmd test

# Package
./mvnw.cmd package

# Con análisis SonarCloud local (requiere token)
./mvnw.cmd verify sonar:sonar \
  -Dsonar.projectKey=tu-project-key \
  -Dsonar.organization=tu-org \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.token=<tu-token>
```

### Ver Reportes JaCoCo

Después de ejecutar tests:

```bash
# El reporte HTML estará en:
microservicio-iso25010/target/site/jacoco/index.html
```

---

## 🔧 Troubleshooting

### GitHub Actions falla con "Coverage checks have not been met"

**Problema**: JaCoCo requiere cobertura mínima

**Solución**: Ya configurado con `haltOnFailure=false` en pom.xml

### SonarCloud Quality Gate falla

**Causas comunes**:
1. **Security Hotspots sin revisar**
   - Ir a SonarCloud → Security Hotspots
   - Revisar cada uno y marcar como "Safe" o "Fixed"

2. **Cobertura baja**
   - Agregar más tests unitarios
   - Verificar con: `mvnw.cmd jacoco:report`

3. **Bugs o Vulnerabilidades**
   - Revisar Issues en SonarCloud
   - Corregir según recomendaciones

### Error "SONAR_TOKEN not found"

**Solución**:
1. Ir a SonarCloud → My Account → Security
2. Generar nuevo token
3. Agregar a GitHub Secrets como `SONAR_TOKEN`

### Build tarda mucho

**Optimizaciones aplicadas**:
- ✅ Cache de Maven configurado
- ✅ Cache de SonarCloud configurado
- ✅ Exclusiones en JaCoCo (config, dto, model)

---

## 📊 Métricas Actuales del Proyecto

| Métrica | Valor |
|---------|-------|
| Coverage | 84.85% ✅ |
| Duplications | 1.45% ✅ |
| Security Hotspots | 0 ✅ |
| Bugs | 0 ✅ |
| Vulnerabilities | 0 ✅ |
| Quality Gate | Passed ✅ |

---

## 🎯 Mejores Prácticas

1. **Commit pequeños y frecuentes**
2. **Escribir tests para nuevo código**
3. **Revisar Quality Gate antes de merge**
4. **Mantener cobertura >80%**
5. **Resolver Security Hotspots rápidamente**
6. **Usar branches para features**
7. **Code review antes de merge a main**

---

## 📚 Referencias

- [GitHub Actions Docs](https://docs.github.com/en/actions)
- [SonarCloud Docs](https://docs.sonarcloud.io/)
- [JaCoCo Maven Plugin](https://www.jacoco.org/jacoco/trunk/doc/maven.html)
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)

---

**Proyecto**: Microservicio ISO/IEC 25010
**Universidad**: Mariano Gálvez de Guatemala
**Curso**: Calidad y Pruebas de Software
