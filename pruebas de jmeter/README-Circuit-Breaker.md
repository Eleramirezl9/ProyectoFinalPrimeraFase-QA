# Guía Rápida: Pruebas de Circuit Breaker con JMeter

## 📁 Archivo de Prueba

**Archivo**: `Circuit-Breaker-Test.jmx`

## ✅ Características

- **Autenticación automática**: Obtiene el token Bearer al inicio
- **6 escenarios completos**: Desde estado inicial hasta reset
- **Validaciones JSON**: Verifica estados y métricas
- **Listeners incluidos**: Ver resultados en árbol, tabla y resumen

## 🚀 Cómo Ejecutar

### Prerrequisitos

1. **Microservicio corriendo**: http://localhost:8080
2. **JMeter instalado**: [Descargar aquí](https://jmeter.apache.org/download_jmeter.cgi)
3. **Usuario admin existe**: Credenciales en `data.sql`

### Opción 1: Interfaz Gráfica (Recomendado)

```bash
# 1. Abrir JMeter GUI
jmeter

# 2. File → Open → Seleccionar Circuit-Breaker-Test.jmx

# 3. Click en el botón verde ▶️ "Start" (o Ctrl+R)

# 4. Ver resultados en:
#    - "Ver Resultados en Árbol" (detalle request/response)
#    - "Reporte Resumen" (estadísticas)
#    - "Ver Resultados en Tabla" (tabla compacta)
```

### Opción 2: Línea de Comandos

```bash
jmeter -n -t "Circuit-Breaker-Test.jmx" -l resultados-cb.jtl -e -o reporte-html/
```

**Ver reporte HTML**: Abrir `reporte-html/index.html` en navegador

## 📊 Qué Observar en los Resultados

### ✅ Prueba Exitosa (Verde)

```
SETUP - Obtener Token              ✅ 200 OK
1️⃣ Verificar Estado Inicial         ✅ 200 OK (estado: CLOSED)
2️⃣ Generar Fallos                   ✅ 404 Not Found x12
3️⃣ Verificar Circuit Breaker OPEN  ✅ 200 OK (estado: OPEN/HALF_OPEN)
4️⃣ Ver Métricas Detalladas          ✅ 200 OK
5️⃣ Resetear Circuit Breaker        ✅ 200 OK
6️⃣ Verificar Estado CLOSED         ✅ 200 OK (estado: CLOSED)
```

### ❌ Errores Comunes

#### Error: "SETUP - Obtener Token" falla con 401

**Causa**: Credenciales incorrectas
**Solución**:
```bash
# Verificar que el microservicio tiene datos iniciales
cd microservicio-iso25010
./mvnw.cmd spring-boot:run
```

Debe mostrar en logs:
```
INFO - Ejecutando data.sql...
INFO - Usuario admin creado
```

#### Error: Todos los requests fallan con 403

**Causa**: Token no se extrajo correctamente
**Solución**: Ver en "Ver Resultados en Árbol":
1. Click en "POST /auth/login"
2. Response Data debe contener: `"token": "eyJ..."`
3. Si no aparece, revisar endpoint de login

#### Error: Circuit Breaker no cambia a OPEN

**Causa**: No se generaron suficientes fallos
**Solución**:
- Verificar que los 12 requests de "Generar Fallos" completaron
- Circuit Breaker requiere 50% de fallos (mínimo 5 llamadas)
- Configuración: `slidingWindowSize: 10`, `failureRateThreshold: 50%`

## 🎯 Qué Valida Esta Prueba

| Validación | Escenario | Aserción |
|------------|-----------|----------|
| ✅ Autenticación funciona | SETUP | HTTP 200 + token en respuesta |
| ✅ Circuit Breaker existe | Escenario 1 | `productoService` presente |
| ✅ Detecta fallos | Escenario 2 | 12 llamadas con HTTP 404 |
| ✅ Se abre automáticamente | Escenario 3 | Estado = OPEN o HALF_OPEN |
| ✅ Registra métricas | Escenario 3 | `numberOfFailedCalls > 0` |
| ✅ Reset funciona | Escenario 5 | HTTP 200 |
| ✅ Vuelve a estado normal | Escenario 6 | Estado = CLOSED, buffered = 0 |

## 📈 Métricas Esperadas

### Antes de los Fallos (Escenario 1)
```json
{
  "estado": "CLOSED",
  "metricas": {
    "numberOfSuccessfulCalls": 0,
    "numberOfFailedCalls": 0,
    "failureRate": "-1.00%",
    "numberOfBufferedCalls": 0
  }
}
```

### Después de los Fallos (Escenario 3)
```json
{
  "estado": "OPEN",
  "metricas": {
    "numberOfSuccessfulCalls": 0,
    "numberOfFailedCalls": 10,
    "failureRate": "100.00%",
    "numberOfBufferedCalls": 10,
    "numberOfNotPermittedCalls": 2
  }
}
```

### Después del Reset (Escenario 6)
```json
{
  "estado": "CLOSED",
  "metricas": {
    "numberOfSuccessfulCalls": 0,
    "numberOfFailedCalls": 0,
    "failureRate": "-1.00%",
    "numberOfBufferedCalls": 0
  }
}
```

## 🔧 Configuración del Archivo

### Variables Globales
```xml
<elementProp name="HOST" elementType="Argument">
  <stringProp name="Argument.value">localhost</stringProp>
</elementProp>
<elementProp name="PORT" elementType="Argument">
  <stringProp name="Argument.value">8080</stringProp>
</elementProp>
<elementProp name="BASE_PATH" elementType="Argument">
  <stringProp name="Argument.value">/api</stringProp>
</elementProp>
```

### Credenciales de Login
Para cambiar usuario/contraseña, editar en SETUP:
```json
{
  "username": "admin",
  "password": "password123"
}
```

### Personalizar Número de Fallos
Editar "Escenario 2":
```xml
<stringProp name="LoopController.loops">12</stringProp>
<!-- Cambiar a 15, 20, etc. para más fallos -->
```

## 📝 Para el Informe

### Capturas de Pantalla Sugeridas

1. **JMeter GUI con el plan cargado** (vista de árbol de escenarios)
2. **"Ver Resultados en Árbol"** mostrando todos los tests en verde
3. **"Reporte Resumen"** con estadísticas (100% éxito)
4. **Response Data de "Escenario 3"** mostrando estado OPEN
5. **Response Data de "Escenario 6"** mostrando estado CLOSED

### Tabla de Resultados

| Escenario | Request | Respuesta Esperada | Resultado Obtenido | Estado |
|-----------|---------|-------------------|-------------------|--------|
| SETUP | POST /auth/login | 200, token | 200, eyJ... | ✅ |
| 1 | GET /resilience/circuit-breakers | 200, CLOSED | 200, CLOSED | ✅ |
| 2 | GET /productos/99999 x12 | 404 x12 | 404 x12 | ✅ |
| 3 | GET /resilience/.../productoService | 200, OPEN | 200, OPEN | ✅ |
| 4 | GET /resilience/circuit-breakers | 200, métricas | 200, 10 fallos | ✅ |
| 5 | POST .../reset | 200 | 200 | ✅ |
| 6 | GET /resilience/.../productoService | 200, CLOSED | 200, CLOSED | ✅ |

### Análisis de Resiliencia

**Demostración de Circuit Breaker funcionando**:
1. ✅ Detecta 12 fallos consecutivos (404)
2. ✅ Calcula tasa de fallo: 100% (12/12)
3. ✅ Supera umbral del 50% → **Se abre automáticamente**
4. ✅ Estado cambia de CLOSED → OPEN
5. ✅ Métricas actualizadas en tiempo real
6. ✅ Reset manual funciona correctamente
7. ✅ Estado vuelve a CLOSED después del reset

**Beneficio para la aplicación**:
- Protege el sistema de llamadas repetidas a servicios fallidos
- Proporciona respuestas rápidas (fallback) sin esperar timeout
- Permite recuperación automática después del tiempo configurado (10s)
- Métricas disponibles para monitoreo y alertas

## 🔗 Documentación Adicional

Ver [`../microservicio-iso25010/CIRCUIT-BREAKER.md`](../microservicio-iso25010/CIRCUIT-BREAKER.md) para:
- Explicación completa de Circuit Breaker
- Configuración en `application.yml`
- Código fuente de la implementación
- Casos de uso detallados
- Pruebas unitarias

## 👥 Soporte

Si tienes problemas:
1. Verificar que el microservicio está corriendo (`http://localhost:8080/api/actuator/health`)
2. Revisar logs del microservicio
3. Ver respuestas completas en "Ver Resultados en Árbol"
4. Consultar sección Troubleshooting en `CIRCUIT-BREAKER.md`
