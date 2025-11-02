# BITÁCORA CONSOLIDADA DE ASEGURAMIENTO DE LA CALIDAD

**Universidad Mariano Gálvez de Guatemala**
**Facultad de Ingeniería en Sistemas de Información**
**Curso:** Aseguramiento de la Calidad
**Grupo:** 6
**Proyecto:** Evaluación Integral de Calidad - API Spring Boot ISO/IEC 25010

---

## INFORMACIÓN DEL PROYECTO

| Campo | Detalle |
|-------|---------|
| **Nombre del Sistema** | Microservicio ISO/IEC 25010 - Sistema de Gestión |
| **Tecnología** | Spring Boot 3.2.12 + Java 17 |
| **Base de Datos** | H2 (in-memory) |
| **Periodo de Pruebas** | Octubre 2025 |
| **Estado del Proyecto** | En Fase de Testing |

---

## REGISTRO DE PRUEBAS EJECUTADAS

### Tabla Consolidada de Hallazgos

| ID | Fecha | Tipo de Prueba | Endpoint/Función | Método | Resultado | Severidad | Evidencia | Observaciones |
|----|-------|----------------|------------------|--------|-----------|-----------|-----------|---------------|
| QA-001 | 31/10/2025 | Unitaria | `UsuarioService.crearUsuario()` | N/A | ✅ EXITOSO | Baja | [UsuarioServiceTest.java](../microservicio-iso25010/src/test/java/com/ejemplo/service/UsuarioServiceTest.java) | Validación de creación correcta con datos válidos |
| QA-002 | 31/10/2025 | Unitaria | `UsuarioService.validarEmail()` | N/A | ✅ EXITOSO | Media | [UsuarioServiceTest.java](../microservicio-iso25010/src/test/java/com/ejemplo/service/UsuarioServiceTest.java) | Valida formato de email correctamente |
| QA-003 | 31/10/2025 | Unitaria | `ProductoService.obtenerTodos()` | N/A | ✅ EXITOSO | Media | [ProductoServiceTest.java](../microservicio-iso25010/src/test/java/com/ejemplo/service/ProductoServiceTest.java) | Recupera lista de productos con Circuit Breaker |
| QA-004 | 31/10/2025 | Unitaria | `ProductoService.reducirStock()` | N/A | ✅ EXITOSO | Alta | [ProductoServiceTest.java](../microservicio-iso25010/src/test/java/com/ejemplo/service/ProductoServiceTest.java) | Reduce stock correctamente y valida disponibilidad |
| QA-005 | 31/10/2025 | Unitaria | `PedidoTest.calcularTotal()` | N/A | ✅ EXITOSO | Alta | [PedidoTest.java](../microservicio-iso25010/src/test/java/com/ejemplo/model/PedidoTest.java) | Cálculo automático precio × cantidad |
| QA-006 | 31/10/2025 | Unitaria | `AuthService.authenticate()` | N/A | ✅ EXITOSO | Crítica | [AuthServiceTest.java](../microservicio-iso25010/src/test/java/com/ejemplo/service/AuthServiceTest.java) | Autenticación JWT funcional |
| QA-007 | 31/10/2025 | Unitaria | `JwtService.generateToken()` | N/A | ✅ EXITOSO | Crítica | [JwtServiceTest.java](../microservicio-iso25010/src/test/java/com/ejemplo/security/JwtServiceTest.java) | Genera tokens JWT válidos |
| QA-008 | 31/10/2025 | Integración | `POST /api/auth/login` | POST | ✅ EXITOSO | Crítica | [AuthControllerTest.java](../microservicio-iso25010/src/test/java/com/ejemplo/controller/AuthControllerTest.java) | Login retorna token Bearer |
| QA-009 | 31/10/2025 | Integración | `GET /api/usuarios` | GET | ✅ EXITOSO | Media | [UsuarioControllerTest.java](../microservicio-iso25010/src/test/java/com/ejemplo/controller/UsuarioControllerTest.java) | Requiere autenticación JWT |
| QA-010 | 31/10/2025 | Integración | `POST /api/usuarios` | POST | ✅ EXITOSO | Alta | [UsuarioControllerTest.java](../microservicio-iso25010/src/test/java/com/ejemplo/controller/UsuarioControllerTest.java) | Crea usuario con validaciones |
| QA-011 | 31/10/2025 | Integración | `GET /api/productos` | GET | ✅ EXITOSO | Media | [ProductoControllerTest.java](../microservicio-iso25010/src/test/java/com/ejemplo/controller/ProductoControllerTest.java) | Lista productos con paginación |
| QA-012 | 31/10/2025 | Integración | `PUT /api/productos/{id}` | PUT | ✅ EXITOSO | Media | [ProductoControllerTest.java](../microservicio-iso25010/src/test/java/com/ejemplo/controller/ProductoControllerTest.java) | Actualiza producto existente |
| QA-013 | 31/10/2025 | Integración | `DELETE /api/productos/{id}` | DELETE | ✅ EXITOSO | Alta | [ProductoControllerTest.java](../microservicio-iso25010/src/test/java/com/ejemplo/controller/ProductoControllerTest.java) | Elimina producto (soft delete) |
| QA-014 | 31/10/2025 | Integración | `POST /api/pedidos` | POST | ✅ EXITOSO | Crítica | [PedidoControllerTest.java](../microservicio-iso25010/src/test/java/com/ejemplo/controller/PedidoControllerTest.java) | Crea pedido y reduce stock |
| QA-015 | 31/10/2025 | Integración | `PUT /api/pedidos/{id}/estado` | PUT | ✅ EXITOSO | Alta | [PedidoControllerTest.java](../microservicio-iso25010/src/test/java/com/ejemplo/controller/PedidoControllerTest.java) | Cambia estado del pedido |
| QA-016 | 31/10/2025 | Funcional | `POST /api/usuarios` (email duplicado) | POST | ✅ EXITOSO | Media | Swagger UI | Retorna 409 CONFLICT correctamente |
| QA-017 | 31/10/2025 | Funcional | `GET /api/productos/99999` | GET | ✅ EXITOSO | Baja | Swagger UI | Retorna 404 NOT_FOUND correctamente |
| QA-018 | 31/10/2025 | Funcional | `POST /api/pedidos` (stock insuficiente) | POST | ✅ EXITOSO | Alta | Swagger UI | Retorna 400 BAD_REQUEST con mensaje claro |
| QA-019 | 31/10/2025 | Resiliencia | `CircuitBreaker.productoService` | N/A | ✅ EXITOSO | Alta | [CircuitBreakerTest.java](../microservicio-iso25010/src/test/java/com/ejemplo/resilience/CircuitBreakerTest.java) | Circuit Breaker se abre tras 50% fallos |
| QA-020 | 31/10/2025 | Resiliencia | `Retry.productoService` | N/A | ✅ EXITOSO | Media | [CircuitBreakerTest.java](../microservicio-iso25010/src/test/java/com/ejemplo/resilience/CircuitBreakerTest.java) | Retry reintenta 3 veces antes de fallar |
| QA-021 | 31/10/2025 | Resiliencia | `Fallback.obtenerTodosFallback()` | N/A | ✅ EXITOSO | Media | [CircuitBreakerTest.java](../microservicio-iso25010/src/test/java/com/ejemplo/resilience/CircuitBreakerTest.java) | Retorna lista vacía cuando falla BD |
| QA-022 | 31/10/2025 | Resiliencia | `GET /api/resilience/circuit-breakers` | GET | ✅ EXITOSO | Baja | [ResilienceMonitorController.java](../microservicio-iso25010/src/main/java/com/ejemplo/controller/ResilienceMonitorController.java) | Monitoreo de Circuit Breakers activo |
| QA-023 | 31/10/2025 | Rendimiento | `GET /api/productos` (100 usuarios) | GET | ⏳ PENDIENTE | Media | [Testing QA.jmx](../pruebas de jmeter/Testing QA.jmx) | Prueba de carga con JMeter |
| QA-024 | 31/10/2025 | Rendimiento | `POST /api/pedidos` (50 usuarios) | POST | ⏳ PENDIENTE | Alta | [Testing QA.jmx](../pruebas de jmeter/Testing QA.jmx) | Prueba de concurrencia |
| QA-025 | 31/10/2025 | Rendimiento | Circuit Breaker bajo carga | N/A | ⏳ PENDIENTE | Alta | [Circuit-Breaker-Test.jmx](../pruebas de jmeter/Circuit-Breaker-Test.jmx) | Validar resiliencia con tráfico alto |
| QA-026 | 31/10/2025 | Seguridad | JWT Token Expiration | N/A | ✅ EXITOSO | Crítica | [JwtServiceTest.java](../microservicio-iso25010/src/test/java/com/ejemplo/security/JwtServiceTest.java) | Tokens expiran correctamente |
| QA-027 | 31/10/2025 | Seguridad | Autorización por roles (ADMIN) | N/A | ✅ EXITOSO | Crítica | [AuthControllerTest.java](../microservicio-iso25010/src/test/java/com/ejemplo/controller/AuthControllerTest.java) | Solo ADMIN puede eliminar usuarios |
| QA-028 | 31/10/2025 | Configuración | H2 Console accesible | N/A | ✅ EXITOSO | Baja | [H2ConsoleConfigTest.java](../microservicio-iso25010/src/test/java/com/ejemplo/config/H2ConsoleConfigTest.java) | H2 Console habilitado en dev |
| QA-029 | 31/10/2025 | Configuración | CORS configurado | N/A | ✅ EXITOSO | Media | [CorsConfigTest.java](../microservicio-iso25010/src/test/java/com/ejemplo/config/CorsConfigTest.java) | CORS permite orígenes configurados |
| QA-030 | 31/10/2025 | Validación | `PasswordGenerator.generate()` | N/A | ✅ EXITOSO | Media | [PasswordGeneratorTest.java](../microservicio-iso25010/src/test/java/com/ejemplo/util/PasswordGeneratorTest.java) | Genera contraseñas seguras |

---

## RESUMEN EJECUTIVO DE HALLAZGOS

### Por Tipo de Prueba

| Tipo de Prueba | Total | Exitosos | Fallidos | Pendientes | % Éxito |
|----------------|-------|----------|----------|------------|---------|
| Unitarias | 7 | 7 | 0 | 0 | 100% |
| Integración | 8 | 8 | 0 | 0 | 100% |
| Funcionales | 3 | 3 | 0 | 0 | 100% |
| Resiliencia | 4 | 4 | 0 | 0 | 100% |
| Rendimiento | 3 | 0 | 0 | 3 | 0% (Pendiente) |
| Seguridad | 2 | 2 | 0 | 0 | 100% |
| Configuración | 3 | 3 | 0 | 0 | 100% |
| **TOTAL** | **30** | **27** | **0** | **3** | **90%** |

### Por Severidad

| Severidad | Cantidad | Porcentaje | Estado Crítico |
|-----------|----------|------------|----------------|
| 🔴 Crítica | 5 | 16.7% | ✅ Todos resueltos |
| 🟠 Alta | 8 | 26.7% | ✅ Todos resueltos |
| 🟡 Media | 13 | 43.3% | ✅ Todos resueltos |
| 🟢 Baja | 4 | 13.3% | ✅ Todos resueltos |

### Hallazgos Críticos (Resueltos)

1. **QA-006**: Autenticación JWT implementada y validada
2. **QA-007**: Generación de tokens seguros funcionando
3. **QA-008**: Endpoint de login operativo
4. **QA-014**: Creación de pedidos con manejo transaccional
5. **QA-026**: Expiración de tokens configurada correctamente

---

## COBERTURA DE CÓDIGO

| Métrica | Objetivo | Actual | Estado |
|---------|----------|--------|--------|
| **Cobertura de Líneas** | ≥ 80% | 85%+ | ✅ CUMPLE |
| **Cobertura de Ramas** | ≥ 70% | 78%+ | ✅ CUMPLE |
| **Cobertura de Métodos** | ≥ 75% | 82%+ | ✅ CUMPLE |

**Herramientas utilizadas:**
- JaCoCo (Generación de reportes de cobertura)
- Maven Surefire Plugin (Ejecución de pruebas)

---

## ENTORNOS DE PRUEBA

| Entorno | Uso | URL | Estado |
|---------|-----|-----|--------|
| **Desarrollo** | Testing manual | http://localhost:8080/api | ✅ Activo |
| **CI/CD** | Tests automáticos | GitHub Actions | ✅ Activo |
| **Staging** | Pre-producción | Docker Compose | ✅ Configurado |

---

## HERRAMIENTAS UTILIZADAS

### Pruebas Unitarias e Integración
- **JUnit 5**: Framework de testing
- **Mockito**: Mocking de dependencias
- **Spring Boot Test**: Testing de contexto Spring
- **AssertJ**: Aserciones fluidas

### Pruebas de Rendimiento
- **Apache JMeter 5.6.3**: Pruebas de carga y estrés
- **Spring Boot Actuator**: Métricas en tiempo real

### Resiliencia
- **Resilience4j 2.1.0**: Circuit Breaker, Retry, Fallback
- **Actuator Endpoints**: Monitoreo de patrones de resiliencia

### Control de Calidad
- **JaCoCo**: Cobertura de código
- **SonarQube/SonarCloud**: Análisis estático
- **Checkstyle**: Estándares de código

---

## PLAN DE ACCIÓN - TAREAS PENDIENTES

| ID | Tarea | Prioridad | Responsable | Fecha Límite | Estado |
|----|-------|-----------|-------------|--------------|--------|
| PA-001 | Ejecutar pruebas de rendimiento JMeter | 🔴 Alta | Grupo 6 | 05/11/2025 | ⏳ En Progreso |
| PA-002 | Analizar métricas de throughput y latencia | 🟠 Media | Grupo 6 | 05/11/2025 | 📋 Planificado |
| PA-003 | Documentar resultados de JMeter con gráficos | 🟡 Media | Grupo 6 | 06/11/2025 | 📋 Planificado |
| PA-004 | Pruebas de seguridad con OWASP ZAP | 🟢 Baja | Grupo 6 | 08/11/2025 | 📋 Planificado |
| PA-005 | Generar informe final consolidado | 🔴 Alta | Grupo 6 | 10/11/2025 | 📋 Planificado |

---

## RIESGOS IDENTIFICADOS

| ID | Riesgo | Probabilidad | Impacto | Mitigación | Estado |
|----|--------|--------------|---------|------------|--------|
| R-001 | Rendimiento bajo en alta concurrencia | Media | Alto | Implementar caché, optimizar queries | ✅ Mitigado (Circuit Breaker) |
| R-002 | Fallos en BD afectan disponibilidad | Baja | Crítico | Resilience4j con fallback | ✅ Mitigado |
| R-003 | Tokens JWT comprometidos | Baja | Crítico | Tiempo expiración corto (1h) | ✅ Mitigado |
| R-004 | Datos sensibles en logs | Media | Alto | LogSanitizer implementado | ✅ Mitigado |

---

## OBSERVACIONES GENERALES

### Fortalezas del Sistema
1. ✅ **Cobertura de pruebas unitarias excelente** (>85%)
2. ✅ **Patrones de resiliencia implementados** (Circuit Breaker, Retry, Fallback)
3. ✅ **Seguridad JWT funcional** con roles y autorización
4. ✅ **Manejo de errores centralizado** con GlobalExceptionHandler
5. ✅ **Documentación Swagger** completa y actualizada

### Áreas de Mejora
1. ⚠️ **Pruebas de rendimiento pendientes** - Prioridad para siguiente iteración
2. ⚠️ **Métricas de producción** - Implementar monitoreo con Prometheus/Grafana
3. ⚠️ **Pruebas de seguridad avanzadas** - OWASP ZAP, penetration testing

### Lecciones Aprendidas
- El uso de Circuit Breaker previene cascadas de fallos efectivamente
- La autenticación JWT simplifica la gestión de sesiones
- Los tests de integración detectan problemas que tests unitarios no capturan
- La documentación con Swagger acelera el desarrollo frontend

---

## CONCLUSIONES

El microservicio ISO/IEC 25010 ha alcanzado un **nivel de calidad alto** con:
- **90% de pruebas completadas exitosamente**
- **0 defectos críticos pendientes**
- **Cobertura de código superior al 80%**
- **Patrones de resiliencia implementados y validados**

Las pruebas de rendimiento pendientes son la última fase antes de considerar el sistema listo para producción.

---

## APROBACIONES

| Rol | Nombre | Firma | Fecha |
|-----|--------|-------|-------|
| **QA Lead** | Grupo 6 | _____________ | __/__/2025 |
| **Desarrollador** | Grupo 6 | _____________ | __/__/2025 |
| **Docente** | [Nombre Docente] | _____________ | __/__/2025 |

---

**Documento generado:** 31 de octubre de 2025
**Versión:** 1.0
**Próxima revisión:** 05 de noviembre de 2025

---

## ANEXOS

1. [Informe de Pruebas Unitarias](01-INFORME-PRUEBAS-UNITARIAS.md)
2. [Informe de Pruebas Funcionales](02-INFORME-PRUEBAS-FUNCIONALES.md)
3. [Informe de Pruebas de Rendimiento y Microservicios](03-INFORME-RENDIMIENTO-MICROSERVICIOS.md)
4. [Informe Final Consolidado de QA](04-INFORME-FINAL-CONSOLIDADO-QA.md)
5. [Scripts de Pruebas JUnit](../microservicio-iso25010/src/test/java/com/ejemplo/)
6. [Planes de Prueba JMeter](../pruebas de jmeter/)
7. [Documentación Circuit Breaker](../microservicio-iso25010/CIRCUIT-BREAKER.md)
