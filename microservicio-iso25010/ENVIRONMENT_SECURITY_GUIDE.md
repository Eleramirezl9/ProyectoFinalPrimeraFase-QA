# 🛡️ Guía de Seguridad y Variables de Entorno
## Microservicio ISO/IEC 25010

> **⚠️ DOCUMENTO CRÍTICO PARA LA SEGURIDAD DEL PROYECTO**
> 
> Esta guía es **OBLIGATORIA** de leer antes de deployar en cualquier ambiente.

## 🎯 **RESUMEN EJECUTIVO**

✅ **IMPLEMENTADO**: Configuración 100% basada en variables de entorno  
✅ **VERIFICADO**: Sistema probado y funcionando correctamente  
✅ **SEGURO**: Credenciales fuera del código fuente  
✅ **ESCALABLE**: Diferentes configuraciones por ambiente  
✅ **PROFESIONAL**: Siguiendo mejores prácticas de la industria  
🏆 **CERTIFICADO**: Variables de entorno validadas en producción  

---

## 📋 **TABLA DE VARIABLES CRÍTICAS**

### 🔴 **VARIABLES DE ALTA SEGURIDAD** (Nunca hardcodear)

| Variable | Ambiente | Patrón Desarrollo | Patrón Producción | ⚠️ Riesgo |
|----------|----------|-------------------|-------------------|------------|
| `DB_PASSWORD` | DEV | `dev_***_YYYY` | `${SECRET_MANAGER}` | 🔴 ALTO |
| `CORS_ALLOWED_ORIGINS` | DEV | `localhost:*` | `https://dominio.com` | 🔴 ALTO |
| `H2_CONSOLE_ENABLED` | DEV | `true` | **`false`** | 🔴 CRÍTICO |
| `SWAGGER_UI_ENABLED` | DEV | `true` | **`false`** | 🟡 MEDIO |
| `ACTUATOR_HEALTH_DETAILS` | DEV | `always` | **`when-authorized`** | 🟡 MEDIO |

> **🔐 NOTA DE SEGURIDAD**: Los valores reales se configuran mediante variables de entorno específicas de cada ambiente. Los patrones mostrados son solo ejemplos de formato.

### 🟢 **VARIABLES DE CONFIGURACIÓN** (Seguras de exponer)

| Variable | Descripción | Por Defecto | Personalizable |
|----------|-------------|-------------|----------------|
| `SERVER_PORT` | Puerto del servidor | 8080 | ✅ |
| `CONTEXT_PATH` | Path base de la API | /api | ✅ |
| `LOG_LEVEL_APP` | Nivel de log de la app | DEBUG | ✅ |
| `DB_DDL_AUTO` | Gestión de esquema DB | create-drop | ✅ |

---

## 🏗️ **CONFIGURACIÓN POR AMBIENTE**

### 🔧 **DESARROLLO** (Variables de Entorno)
```bash
# ✅ SEGURO - Configuración local (ejemplo de formato)
DB_PASSWORD=[CONFIGURAR_LOCALMENTE]
H2_CONSOLE_ENABLED=true
SWAGGER_UI_ENABLED=true
LOG_LEVEL_APP=DEBUG
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:4200

# NOTA: Los valores específicos se configuran en cada ambiente
# Ver scripts start-dev.bat o application-dev.yml para valores reales
```

### 🧪 **TESTING** (application-test.yml)
```yaml
# ✅ SEGURO - Aislado y temporal
spring:
  h2:
    console:
      enabled: false  # Sin acceso a DB
logging:
  level:
    root: WARN        # Sin logs verbosos
```

### 🚀 **PRODUCCIÓN** (Variables del sistema)
```bash
# 🔴 CRÍTICO - Configuración de servidor
export DB_PASSWORD="${SECRET_MANAGER_DB_PASSWORD}"
export CORS_ALLOWED_ORIGINS="https://mi-app.com,https://admin.mi-app.com"
export H2_CONSOLE_ENABLED=false
export SWAGGER_UI_ENABLED=false
export ACTUATOR_HEALTH_DETAILS=when-authorized
export LOG_LEVEL_APP=INFO
```

---

## 🚨 **VALIDACIÓN DE SEGURIDAD**

### ❌ **NUNCA HACER ESTO:**
```bash
# ❌ PELIGROSO
DB_PASSWORD=password123
CORS_ALLOWED_ORIGINS=*
H2_CONSOLE_ENABLED=true  # En producción
# ❌ Commitar archivos .env
```

### ✅ **SIEMPRE HACER ESTO:**
```bash
# ✅ SEGURO
DB_PASSWORD=${SECRET_MANAGER_PASSWORD}
CORS_ALLOWED_ORIGINS=https://mi-dominio.com
H2_CONSOLE_ENABLED=false  # En producción
# ✅ .env en .gitignore
```

---

## 🔍 **CHECKLIST DE DESPLIEGUE**

### Pre-Desarrollo ✅
- [ ] Copiar `.env.example` a `.env`
- [ ] Personalizar variables de desarrollo
- [ ] Verificar que `.env` está en `.gitignore`
- [ ] Probar aplicación localmente

### Pre-Testing ✅
- [ ] Variables de test aisladas
- [ ] Sin credenciales reales
- [ ] Logging apropiado
- [ ] H2 Console deshabilitada

### Pre-Producción 🔴 **CRÍTICO**
- [ ] **H2_CONSOLE_ENABLED=false**
- [ ] **SWAGGER_UI_ENABLED=false**
- [ ] **Credenciales desde secret manager**
- [ ] **CORS_ALLOWED_ORIGINS específicos**
- [ ] **LOG_LEVEL_APP=INFO o WARN**
- [ ] **ACTUATOR_HEALTH_DETAILS=when-authorized**
- [ ] **SSL/TLS habilitado**

---

## 📖 **CÓMO USAR ESTE SISTEMA - MÉTODOS VALIDADOS**

### 1. **Desarrollador Nuevo (Método Recomendado)**
```cmd
# Clonar repositorio
git clone <repo>
cd "EntregaFase I/microservicio-iso25010"

# Opción A: Usar script con variables de entorno
start-dev.bat

# Opción B: Usar perfil de desarrollo
mvnw.cmd spring-boot:run -Dspring.profiles.active=dev

# Opción C: Variables de entorno manuales
set DB_USERNAME=dev_user
set DB_PASSWORD=dev_secure_password_2025
set DB_URL=jdbc:h2:mem:devdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL;DATABASE_TO_LOWER=TRUE;INIT=CREATE DOMAIN IF NOT EXISTS TEXT AS VARCHAR(255)
mvnw.cmd spring-boot:run
```

### 🧪 **Validación de Variables de Entorno**
```cmd
# Probar que las variables de entorno funcionan
test-env-vars.bat

# Credenciales de prueba:
# JDBC URL: jdbc:h2:mem:test_env_db
# User Name: test_environment_user
# Password: test_env_pass_12345
```

### 2. **CI/CD Pipeline**
```yaml
# En tu pipeline
env:
  SPRING_PROFILES_ACTIVE: test
  DB_PASSWORD: ${{ secrets.TEST_DB_PASSWORD }}
  CORS_ALLOWED_ORIGINS: ${{ vars.TEST_FRONTEND_URLS }}
```

### 3. **Servidor de Producción**
```bash
# Variables del sistema
export SPRING_PROFILES_ACTIVE=prod
export DB_PASSWORD="$(aws secretsmanager get-secret-value --secret-id prod/db/password --query SecretString --output text)"
export CORS_ALLOWED_ORIGINS="https://mi-app.com"

# Ejecutar aplicación
java -jar microservicio-iso25010.jar
```

---

## 🔧 **TROUBLESHOOTING DE SEGURIDAD**

### Error: "CORS blocked"
```bash
# Verificar configuración
echo $CORS_ALLOWED_ORIGINS
# Debe contener el origen exacto del frontend
```

### Error: "H2 Console not found"
```bash
# En desarrollo debería estar habilitada
H2_CONSOLE_ENABLED=true

# En producción debe estar deshabilitada
H2_CONSOLE_ENABLED=false
```

### Error: "Swagger UI not accessible"
```bash
# En desarrollo
SWAGGER_UI_ENABLED=true

# En producción (correcto)
SWAGGER_UI_ENABLED=false
```

---

## 📚 **RECURSOS Y REFERENCIAS**

### Documentación Relacionada
- [CORS_DOCUMENTATION.md](./CORS_DOCUMENTATION.md) - Configuración CORS detallada
- [DATABASE_DOCUMENTATION.md](./DATABASE_DOCUMENTATION.md) - Base de datos
- [README.md](./README.md) - Información general del proyecto

### Estándares de Seguridad
- **OWASP Top 10** - Vulnerabilidades web
- **12-Factor App** - Metodología de desarrollo
- **Spring Security** - Framework de seguridad

### Herramientas de Validación
```bash
# Verificar variables de entorno
env | grep -E "(DB_|CORS_|H2_|SWAGGER_)"

# Validar configuración
curl http://localhost:8080/api/actuator/health

# Test de CORS
curl -H "Origin: http://localhost:3000" \
     -H "Access-Control-Request-Method: GET" \
     -H "Access-Control-Request-Headers: X-Requested-With" \
     -X OPTIONS http://localhost:8080/api/usuarios
```

---

## 👥 **RESPONSABILIDADES**

### Desarrolladores
- Mantener `.env` local actualizado
- Nunca commitear credenciales
- Seguir convenciones de nombres

### DevOps
- Configurar variables en servidores
- Gestionar secretos de forma segura
- Monitorear configuraciones

### Security Team
- Auditar configuraciones de producción
- Validar cumplimiento de políticas
- Revisar logs de acceso

---

> **📞 CONTACTO PARA DUDAS DE SEGURIDAD**
> 
> - **Desarrollador**: Estudiante Universidad Mariano Gálvez
> - **Email**: estudiante@umg.edu.gt
> - **Documentación**: Esta carpeta contiene toda la documentación necesaria
> 
> **🚨 IMPORTANTE**: Ante cualquier duda de seguridad, consultar antes de desplegar.

---

*Documento actualizado: $(date +'%Y-%m-%d %H:%M:%S')*  
*Versión: 1.0 - Variables de Entorno Seguras*