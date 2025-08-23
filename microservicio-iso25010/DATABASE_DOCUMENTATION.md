# 🗄️ Documentación de Base de Datos - Microservicio ISO/IEC 25010

## 📋 Resumen de la Configuración

La configuración de base de datos utiliza **variables de entorno** para máxima flexibilidad y seguridad, permitiendo diferentes configuraciones por ambiente sin modificar código.

## 🏗️ Arquitectura por Ambientes

### 🔧 Desarrollo Local (.env)
```bash
DB_URL=jdbc:h2:mem:devdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
DB_USERNAME=dev_user
DB_PASSWORD=dev_secure_password_2024
H2_CONSOLE_ENABLED=true
DB_SHOW_SQL=true
```

### 🧪 Testing (application-test.yml)
```yaml
# Base de datos limpia para cada test
DB_URL=jdbc:h2:mem:testdb_${random.uuid}
H2_CONSOLE_ENABLED=false
DB_SHOW_SQL=false
```

### 🚀 Producción (application-prod.yml)
```bash
# Variables de entorno del servidor
DB_URL=jdbc:postgresql://prod-db:5432/iso25010_prod
DB_DRIVER=org.postgresql.Driver
DB_USERNAME=prod_user
DB_PASSWORD=${SECRET_DB_PASSWORD}
H2_CONSOLE_ENABLED=false
DB_DDL_AUTO=validate
```

## 📊 Variables de Entorno Disponibles

| Variable | Descripción | Desarrollo | Testing | Producción |
|----------|-------------|------------|---------|------------|
| `DB_URL` | URL de conexión | H2 memoria | H2 única | PostgreSQL/MySQL |
| `DB_DRIVER` | Driver JDBC | H2Driver | H2Driver | PostgreSQLDriver |
| `DB_USERNAME` | Usuario DB | dev_user | test_user | prod_user |
| `DB_PASSWORD` | Contraseña DB | dev_password | test_password | ${SECRET} |
| `DB_PLATFORM` | Dialecto Hibernate | H2Dialect | H2Dialect | PostgreSQLDialect |
| `DB_DDL_AUTO` | Gestión esquema | create-drop | create-drop | validate |
| `DB_SHOW_SQL` | Mostrar SQL | true | false | false |
| `H2_CONSOLE_ENABLED` | Consola H2 | true | false | false |

## 🛡️ Características de Seguridad

### ✅ Implementadas
- **Variables de entorno**: Credenciales fuera del código
- **Perfiles por ambiente**: Configuraciones específicas
- **H2 Console condicional**: Solo en desarrollo
- **Contraseñas seguras**: No usar valores por defecto
- **Validación en producción**: `ddl-auto: validate`

### ⚠️ Consideraciones de Seguridad
```bash
# NUNCA hacer esto en producción:
DB_PASSWORD=password123

# SÍ hacer esto:
DB_PASSWORD=${SECRET_MANAGER_DB_PASSWORD}
```

## 🚀 Cómo Usar

### 1. Desarrollo Local
```bash
# Copiar configuración
cp .env.example .env

# Personalizar variables
nano .env

# Ejecutar aplicación
./mvnw spring-boot:run
```

### 2. Testing
```bash
# Tests usan application-test.yml automáticamente
./mvnw test

# O especificar perfil
SPRING_PROFILES_ACTIVE=test ./mvnw test
```

### 3. Producción
```bash
# Variables de entorno del sistema
export SPRING_PROFILES_ACTIVE=prod
export DB_URL="jdbc:postgresql://prod-server:5432/iso25010"
export DB_USERNAME="prod_user"
export DB_PASSWORD="${DB_SECRET}"
export H2_CONSOLE_ENABLED=false

./mvnw spring-boot:run
```

## 📈 Migración a Bases de Datos Productivas

### PostgreSQL
```bash
# Variables de entorno
DB_URL=jdbc:postgresql://localhost:5432/iso25010_prod
DB_DRIVER=org.postgresql.Driver
DB_PLATFORM=org.hibernate.dialect.PostgreSQLDialect
DB_USERNAME=postgres_user
DB_PASSWORD=${POSTGRES_PASSWORD}
```

### MySQL
```bash
# Variables de entorno
DB_URL=jdbc:mysql://localhost:3306/iso25010_prod?serverTimezone=UTC
DB_DRIVER=com.mysql.cj.jdbc.Driver
DB_PLATFORM=org.hibernate.dialect.MySQLDialect
DB_USERNAME=mysql_user
DB_PASSWORD=${MYSQL_PASSWORD}
```

### Dependencias Requeridas
```xml
<!-- Para PostgreSQL -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Para MySQL -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
```

## 🔍 Monitoreo y Debugging

### H2 Console (Solo Desarrollo)
- **URL**: http://localhost:8080/api/h2-console
- **JDBC URL**: Ver variable `DB_URL` en .env
- **Usuario**: Ver variable `DB_USERNAME` en .env

### Logs de SQL
```bash
# Habilitar en desarrollo
DB_SHOW_SQL=true

# Ver logs formateados
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### Health Check
```bash
# Verificar estado de la DB
curl http://localhost:8080/api/actuator/health
```

## 📁 Estructura de Datos

### Modelos Principales
- **Usuario**: Gestión de usuarios del sistema
- **Producto**: Catálogo de productos
- **Pedido**: Sistema de órdenes

### Inicialización
- **Archivo**: `src/main/resources/data.sql`
- **Configuración**: `spring.sql.init.mode=always`
- **Testing**: Deshabilitada con `mode=never`

## 🔧 Troubleshooting

### Error: "Table not found"
```bash
# Verificar DDL
DB_DDL_AUTO=create-drop  # Para desarrollo
DB_DDL_AUTO=validate     # Para producción
```

### Error: "Connection refused"
```bash
# Verificar URL y credenciales
echo $DB_URL
echo $DB_USERNAME
```

### H2 Console no accesible
```bash
# Verificar configuración
H2_CONSOLE_ENABLED=true
H2_WEB_ALLOW_OTHERS=false  # Para seguridad local
```

## 📚 Recursos Adicionales

- [Spring Boot Database Documentation](https://spring.io/guides/gs/accessing-data-jpa/)
- [H2 Database Documentation](http://h2database.com/html/main.html)
- [Hibernate Configuration Properties](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#configurations)

---

**Autor**: Estudiante Universidad Mariano Gálvez  
**Versión**: 2.0 - Variables de Entorno  
**Fecha**: $(date +'%Y-%m-%d')