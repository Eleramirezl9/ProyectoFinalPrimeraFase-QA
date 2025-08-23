# Documentación de CORS - Microservicio ISO/IEC 25010

## ¿Qué es CORS?

**CORS (Cross-Origin Resource Sharing)** es un mecanismo de seguridad implementado por los navegadores web que controla cómo las páginas web de un dominio pueden acceder a recursos de otro dominio, protocolo o puerto.

## 🚀 Configuración Implementada (v2.0 - Variables de Entorno)

### 1. Configuración Centralizada con Variables de Entorno

Se refactorizó la clase `CorsConfig.java` para usar configuración basada en variables de entorno:

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Value("${cors.allowed-origins}")
    private String allowedOriginsString;
    // ... más configuraciones desde variables
}
```

### 2. Orígenes Permitidos (Configurable)

**Variable de Entorno:** `CORS_ALLOWED_ORIGINS`

**Valores por Defecto (Desarrollo):**
- `http://localhost:3000` (React/Next.js)
- `http://localhost:4200` (Angular)
- `http://localhost:8080` (Vue.js/Desarrollo local)
- `http://127.0.0.1:3000`
- `http://127.0.0.1:4200`
- `http://127.0.0.1:8080`

**Para Producción:**
```bash
export CORS_ALLOWED_ORIGINS="https://mi-frontend.com,https://admin.mi-empresa.com"
```

### 3. Métodos HTTP Permitidos (Configurable)

**Variable de Entorno:** `CORS_ALLOWED_METHODS`
**Por Defecto:** `GET,POST,PUT,PATCH,DELETE,OPTIONS`

- `GET` - Lectura de datos
- `POST` - Creación de recursos
- `PUT` - Actualización completa
- `PATCH` - Actualización parcial
- `DELETE` - Eliminación de recursos
- `OPTIONS` - Preflight requests

### 4. Headers Permitidos (Configurable)

**Headers Entrantes - Variable:** `CORS_ALLOWED_HEADERS`
- `Content-Type` - Tipo de contenido (application/json)
- `Authorization` - Token de autorización
- `X-Requested-With` - Para peticiones AJAX
- `Accept` - Tipos de respuesta aceptados
- `Origin` - Origen de la petición
- `Access-Control-Request-Method` - Método solicitado en preflight
- `Access-Control-Request-Headers` - Headers solicitados en preflight

**Headers Expuestos - Variable:** `CORS_EXPOSED_HEADERS`
- `Access-Control-Allow-Origin` - Para debugging
- `Access-Control-Allow-Credentials` - Credenciales permitidas
- `Location` - URL del recurso creado
- `X-Total-Count` - Para paginación

## 🔧 Configuración en application.yml

```yaml
# Configuración personalizada de CORS
cors:
  allowed-origins: ${CORS_ALLOWED_ORIGINS:http://localhost:3000,http://localhost:4200,http://localhost:8080,http://127.0.0.1:3000,http://127.0.0.1:4200,http://127.0.0.1:8080}
  allowed-methods: ${CORS_ALLOWED_METHODS:GET,POST,PUT,PATCH,DELETE,OPTIONS}
  allowed-headers: ${CORS_ALLOWED_HEADERS:Content-Type,Authorization,X-Requested-With,Accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers}
  exposed-headers: ${CORS_EXPOSED_HEADERS:Access-Control-Allow-Origin,Access-Control-Allow-Credentials,Location,X-Total-Count}
  allow-credentials: ${CORS_ALLOW_CREDENTIALS:true}
  max-age: ${CORS_MAX_AGE:3600}
```

## 🌍 Variables de Entorno Disponibles

| Variable | Descripción | Valor por Defecto |
|----------|-------------|------------------|
| `CORS_ALLOWED_ORIGINS` | URLs permitidas (separadas por comas) | localhost URLs |
| `CORS_ALLOWED_METHODS` | Métodos HTTP permitidos | GET,POST,PUT,PATCH,DELETE,OPTIONS |
| `CORS_ALLOWED_HEADERS` | Headers de petición permitidos | Content-Type,Authorization,etc. |
| `CORS_EXPOSED_HEADERS` | Headers expuestos al cliente | Location,X-Total-Count,etc. |
| `CORS_ALLOW_CREDENTIALS` | Permitir credenciales | true |
| `CORS_MAX_AGE` | Cache de preflight (segundos) | 3600 |

## 📁 Archivo .env.example

Se creó un archivo `.env.example` con todas las configuraciones disponibles:

```bash
# Copiar y renombrar a .env para usar
cp .env.example .env

# Editar las variables según el entorno
nano .env
```

## ✅ Mejores Prácticas Implementadas

### 🏆 Nuevas Mejoras (v2.0)

1. **Variables de Entorno**: Configuración flexible sin recompilar
2. **Separación por Ambiente**: Desarrollo, testing y producción independientes
3. **Archivo .env.example**: Plantilla para nuevos desarrolladores
4. **Valores por Defecto Seguros**: Configuración segura out-of-the-box
5. **Documentación Actualizada**: Guías específicas para cada ambiente

### 📋 Buenas Prácticas Mantenidas

1. **Configuración Centralizada**: Una sola clase maneja toda la configuración CORS
2. **Orígenes Específicos**: No usar `*` en producción
3. **Headers Controlados**: Solo los headers necesarios están permitidos
4. **Credenciales Habilitadas**: `allowCredentials(true)` para autenticación
5. **Cache de Preflight**: Configurable según ambiente
6. **Documentación Completa**: Todo está documentado en español

### ❌ Prácticas Removidas

1. **@CrossOrigin(origins = "*")**: Removido de todos los controladores
2. **Configuración Duplicada**: Ya no hay configuración CORS dispersa
3. **Orígenes Wildcard**: No se usa `*` por seguridad

## 🏗️ Configuración por Ambiente

### Desarrollo Local
```bash
# No requiere configuración adicional - usa valores por defecto
# Opcional: crear .env para sobrescribir
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:4200
```

### Testing
```bash
# .env.test
CORS_ALLOWED_ORIGINS=http://test-frontend:3000,http://localhost:4200
CORS_MAX_AGE=0  # Sin cache para testing
```

### Producción
```bash
# Variables de entorno del servidor/contenedor
export CORS_ALLOWED_ORIGINS="https://mi-app.com,https://admin.mi-app.com"
export CORS_ALLOW_CREDENTIALS=true
export CORS_MAX_AGE=7200  # 2 horas de cache
```

## Testing de CORS

### Verificar desde navegador:
```javascript
// En la consola del navegador
fetch('http://localhost:8080/api/usuarios')
  .then(response => response.json())
  .then(data => console.log(data));
```

### Headers de respuesta esperados:
```
Access-Control-Allow-Origin: http://localhost:3000
Access-Control-Allow-Credentials: true
Access-Control-Allow-Methods: GET, POST, PUT, PATCH, DELETE, OPTIONS
```

## Seguridad

### Para Producción:
1. **Cambiar URLs**: Reemplazar localhost por URLs reales
2. **HTTPS Únicamente**: Solo permitir conexiones seguras
3. **Orígenes Específicos**: Nunca usar wildcards (`*`)
4. **Headers Mínimos**: Solo los headers estrictamente necesarios
5. **Monitoring**: Monitorear intentos de acceso desde orígenes no autorizados

### Ejemplo de configuración segura para producción:
```bash
# Variables de entorno del servidor
CORS_ALLOWED_ORIGINS="https://mi-frontend.com,https://admin.mi-empresa.com"
CORS_ALLOWED_METHODS="GET,POST,PUT,DELETE"
CORS_MAX_AGE="7200"

# O en application-prod.yml
cors:
  allowed-origins: "https://mi-frontend.com,https://admin.mi-empresa.com"
  allowed-methods: "GET,POST,PUT,DELETE"
  max-age: 7200
```

## Troubleshooting

### Error: "Access to fetch at 'API_URL' from origin 'FRONTEND_URL' has been blocked by CORS policy"

**Solución:**
1. Verificar que el origen esté en `allowed-origins`
2. Verificar que el método HTTP esté permitido
3. Verificar que los headers estén permitidos
4. Revisar los logs del servidor

### Error: "Preflight request doesn't pass access control check"

**Solución:**
1. Verificar que `OPTIONS` esté en métodos permitidos
2. Verificar configuración de `allowCredentials`
3. Verificar headers de preflight

## Contacto

Para dudas sobre esta configuración:
- **Autor**: Estudiante Universidad Mariano Gálvez
- **Email**: estudiante@umg.edu.gt
- **Documentación API**: http://localhost:8080/api/swagger-ui.html