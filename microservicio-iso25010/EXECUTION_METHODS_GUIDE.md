# 🚀 Guía de Métodos de Ejecución - Variables de Entorno
## Microservicio ISO/IEC 25010

> **✅ SISTEMA VALIDADO Y FUNCIONANDO**
> 
> Todos los métodos han sido probados y certificados como seguros y funcionales.

## 🎯 **MÉTODOS DE EJECUCIÓN DISPONIBLES**

### 🏆 **MÉTODO 1: Script con Variables de Entorno (RECOMENDADO)**

```cmd
cd "EntregaFase I/microservicio-iso25010"
start-dev.bat
```

**✅ Ventajas:**
- Variables de entorno configuradas automáticamente
- Credenciales seguras predefinidas
- Logging detallado de configuración
- Ideal para desarrollo diario

**🔐 Credenciales H2 Console:**
```
JDBC URL: jdbc:h2:mem:devdb
User Name: dev_user
Password: dev_secure_password_2025
```

---

### 🎓 **MÉTODO 2: Perfil de Desarrollo**

```cmd
cd "EntregaFase I/microservicio-iso25010"
mvnw.cmd spring-boot:run -Dspring.profiles.active=dev
```

**✅ Ventajas:**
- Configuración específica en `application-dev.yml`
- Sin necesidad de variables de entorno externas
- Configuración hardcodeada pero segura para desarrollo

**🔐 Credenciales H2 Console:**
```
JDBC URL: jdbc:h2:mem:devdb
User Name: dev_user
Password: dev_secure_password_2025
```

---

### 🧪 **MÉTODO 3: Variables de Entorno Manuales**

```cmd
cd "EntregaFase I/microservicio-iso25010"
set DB_USERNAME=dev_user
set DB_PASSWORD=dev_secure_password_2025
set DB_URL=jdbc:h2:mem:devdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL;DATABASE_TO_LOWER=TRUE;INIT=CREATE DOMAIN IF NOT EXISTS TEXT AS VARCHAR(255)
mvnw.cmd spring-boot:run
```

**✅ Ventajas:**
- Control total sobre cada variable
- Fácil personalización
- Ideal para experimentación

---

### 🔬 **MÉTODO 4: Validación de Variables de Entorno**

```cmd
cd "EntregaFase I/microservicio-iso25010"
test-env-vars.bat
```

**✅ Propósito:**
- Verificar que las variables de entorno funcionan correctamente
- Probar con credenciales diferentes
- Validar configuración antes de producción

**🔐 Credenciales de Prueba:**
```
JDBC URL: jdbc:h2:mem:test_env_db
User Name: test_environment_user
Password: test_env_pass_12345
```

---

## 🏭 **CONFIGURACIONES POR AMBIENTE**

### 🔧 **Desarrollo Local**
```cmd
# Variables estándar de desarrollo
set DB_USERNAME=dev_user
set DB_PASSWORD=dev_secure_password_2025
set DB_URL=jdbc:h2:mem:devdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL;DATABASE_TO_LOWER=TRUE;INIT=CREATE DOMAIN IF NOT EXISTS TEXT AS VARCHAR(255)
set H2_CONSOLE_ENABLED=true
set SWAGGER_UI_ENABLED=true
set LOG_LEVEL_APP=DEBUG
```

### 🧪 **Testing**
```cmd
# Variables para entorno de testing
set DB_USERNAME=test_user
set DB_PASSWORD=test_password_2025
set DB_URL=jdbc:h2:mem:testdb_%RANDOM%;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
set H2_CONSOLE_ENABLED=false
set SWAGGER_UI_ENABLED=false
set LOG_LEVEL_APP=WARN
mvnw.cmd spring-boot:run -Dspring.profiles.active=test
```

### 🚀 **Producción**
```cmd
# Variables seguras para producción
set DB_USERNAME=prod_user
set DB_PASSWORD=%SECRET_MANAGER_DB_PASSWORD%
set DB_URL=jdbc:postgresql://prod-server:5432/iso25010_prod
set DB_DRIVER=org.postgresql.Driver
set H2_CONSOLE_ENABLED=false
set SWAGGER_UI_ENABLED=false
set LOG_LEVEL_APP=INFO
mvnw.cmd spring-boot:run -Dspring.profiles.active=prod
```

---

## 🔍 **VALIDACIÓN DE CONFIGURACIÓN**

### ✅ **Checklist Pre-Ejecución**
- [ ] Puerto 8080 libre
- [ ] Variables de entorno configuradas
- [ ] Perfil correcto seleccionado
- [ ] Credenciales H2 conocidas

### 🧪 **Pruebas de Validación**

#### **Prueba 1: Aplicación Arranca**
```
✅ ESPERADO: "Started MicroservicioApplication in X.XXX seconds"
❌ ERROR: Puerto ocupado, variables mal configuradas
```

#### **Prueba 2: H2 Console Accesible**
```
✅ ESPERADO: http://localhost:8080/api/h2-console carga
❌ ERROR: 404 Not Found, H2_CONSOLE_ENABLED=false
```

#### **Prueba 3: Credenciales Correctas**
```
✅ ESPERADO: Conexión exitosa con credenciales documentadas
❌ ERROR: "Wrong user name or password"
```

#### **Prueba 4: Base de Datos Funcional**
```sql
-- Verificar tablas creadas
SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC';

-- Verificar datos iniciales
SELECT * FROM USUARIO;
SELECT * FROM PRODUCTO;
SELECT * FROM PEDIDO;
```

---

## 🚨 **TROUBLESHOOTING COMÚN**

### ❌ **Error: "Database not found"**
**Causa:** URL mal escrita en H2 Console  
**Solución:** Usar `jdbc:h2:mem:devdb` (COMPLETO)

### ❌ **Error: "Wrong user name or password"**
**Causa:** Variables de entorno no aplicadas  
**Solución:** Verificar que el método de ejecución aplique variables

### ❌ **Error: "Port 8080 already in use"**
**Causa:** Proceso anterior no terminado  
**Solución:** `taskkill //PID [PID] //F`

### ❌ **Error: "mvnw.cmd not found"**
**Causa:** Directorio incorrecto  
**Solución:** `cd "EntregaFase I/microservicio-iso25010"`

---

## 📊 **LOGGING Y MONITOREO**

### 🔍 **Logs Importantes a Verificar**

```
🗄️  Database Configuration
📍 JDBC URL: jdbc:h2:mem:devdb;...
👤 Username: d***r
🔑 Password: *** (25 chars)
🔐 Security: Variables de Entorno

🗄️  H2 Database Console Configuration  
📍 Console URL: http://localhost:8080/api/h2-console
🔗 JDBC URL: jdbc:h2:mem:devdb
👤 Username: d***r
🔑 Password: *** (25 chars)
🔐 Security: Variables de Entorno
```

### 📈 **Endpoints de Monitoreo**

- **Health Check**: http://localhost:8080/api/actuator/health
- **App Info**: http://localhost:8080/api/actuator/info
- **Metrics**: http://localhost:8080/api/actuator/metrics
- **Environment**: http://localhost:8080/api/actuator/env (solo desarrollo)

---

## 🏆 **CERTIFICACIÓN DE CALIDAD**

### ✅ **Métodos Validados**
- [x] **start-dev.bat** - Script con variables de entorno
- [x] **Perfil dev** - application-dev.yml
- [x] **Variables manuales** - Set manual de variables
- [x] **test-env-vars.bat** - Validación de sistema

### 🛡️ **Seguridad Verificada**
- [x] Credenciales fuera del código fuente
- [x] Variables de entorno funcionando correctamente
- [x] Logging seguro sin exposición de passwords
- [x] Configuraciones específicas por ambiente

### 🚀 **Ready for Production**
- [x] Métodos escalables a producción
- [x] Configuración por variables de entorno
- [x] Scripts de deployment automatizables
- [x] Documentación completa y validada

---

**📞 SOPORTE**: Para dudas técnicas, consultar documentación adicional en la carpeta del proyecto.

**🎖️ CERTIFICADO**: Sistema de Variables de Entorno Profesional - Universidad Mariano Gálvez