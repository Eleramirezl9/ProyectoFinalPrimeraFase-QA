# Documentación de CORS - Microservicio ISO/IEC 25010

## ¿Qué es CORS?

**CORS (Cross-Origin Resource Sharing)** es un mecanismo de seguridad implementado por los navegadores web que controla cómo las páginas web de un dominio pueden acceder a recursos de otro dominio, protocolo o puerto.

## Configuración Implementada

### 1. Configuración Centralizada

Se creó la clase `CorsConfig.java` que centraliza toda la configuración de CORS:

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer
```

### 2. Orígenes Permitidos

**Para Desarrollo:**
- `http://localhost:3000` (React/Next.js)
- `http://localhost:4200` (Angular)
- `http://localhost:8080` (Vue.js/Desarrollo local)
- `http://127.0.0.1:3000`
- `http://127.0.0.1:4200`
- `http://127.0.0.1:8080`

**Para Producción:**
- Las URLs deben cambiarse por las URLs reales del frontend desplegado
- Ejemplo: `https://mi-frontend.com`

### 3. Métodos HTTP Permitidos

- `GET` - Lectura de datos
- `POST` - Creación de recursos
- `PUT` - Actualización completa
- `PATCH` - Actualización parcial
- `DELETE` - Eliminación de recursos
- `OPTIONS` - Preflight requests

### 4. Headers Permitidos

**Entrantes (Request Headers):**
- `Content-Type` - Tipo de contenido (application/json)
- `Authorization` - Token de autorización
- `X-Requested-With` - Para peticiones AJAX
- `Accept` - Tipos de respuesta aceptados
- `Origin` - Origen de la petición

**Salientes (Response Headers):**
- `Access-Control-Allow-Origin` - Para debugging
- `Location` - URL del recurso creado
- `X-Total-Count` - Para paginación

## Configuración en application.yml

```yaml
# Configuración de CORS (Cross-Origin Resource Sharing)
cors:
  # URLs permitidas para desarrollo local
  # EN PRODUCCIÓN: Cambiar por las URLs reales del frontend
  allowed-origins: >
    http://localhost:3000,
    http://localhost:4200,
    http://localhost:8080,
    http://127.0.0.1:3000,
    http://127.0.0.1:4200,
    http://127.0.0.1:8080
```

## Mejores Prácticas Implementadas

### ✅ Buenas Prácticas

1. **Configuración Centralizada**: Una sola clase maneja toda la configuración CORS
2. **Orígenes Específicos**: No usar `*` en producción
3. **Headers Controlados**: Solo los headers necesarios están permitidos
4. **Credenciales Habilitadas**: `allowCredentials(true)` para autenticación
5. **Cache de Preflight**: 1 hora de cache para optimizar rendimiento
6. **Documentación Completa**: Todo está documentado en español

### ❌ Prácticas Removidas

1. **@CrossOrigin(origins = "*")**: Removido de todos los controladores
2. **Configuración Duplicada**: Ya no hay configuración CORS dispersa
3. **Orígenes Wildcard**: No se usa `*` por seguridad

## Configuración por Ambiente

### Desarrollo Local
```yaml
cors:
  allowed-origins: http://localhost:3000,http://localhost:4200
```

### Producción
```yaml
cors:
  allowed-origins: https://mi-app.com,https://admin.mi-app.com
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
```yaml
cors:
  allowed-origins: >
    https://mi-frontend.com,
    https://admin.mi-empresa.com
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