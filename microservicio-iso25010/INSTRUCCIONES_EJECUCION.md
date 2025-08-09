# Instrucciones para Ejecutar el Microservicio ISO/IEC 25010

## Requisitos Previos

### 1. Instalar Java 17
**Opción A: Descargar desde Oracle**
- Ve a [Oracle JDK Downloads](https://www.oracle.com/java/technologies/downloads/#java17)
- Descarga "Windows x64 Installer" para Java 17
- Ejecuta el instalador y sigue las instrucciones

**Opción B: Usar OpenJDK (Recomendado)**
- Ve a [Adoptium](https://adoptium.net/)
- Descarga Eclipse Temurin JDK 17 para Windows
- Ejecuta el instalador

### 2. Verificar la instalación
Después de instalar Java, abre una nueva ventana de PowerShell y ejecuta:
```powershell
java -version
```

Deberías ver algo como:
```
openjdk version "17.0.x" 2023-xx-xx
OpenJDK Runtime Environment Temurin-17.0.x+x (build 17.0.x+x)
OpenJDK 64-Bit Server VM Temurin-17.0.x+x (build 17.0.x+x, mixed mode, sharing)
```

## Ejecutar el Proyecto

### Opción 1: Usando el Wrapper de Maven (Recomendado)
Una vez que tengas Java 17 instalado, ejecuta:

```powershell
cd microservicio-iso25010
.\mvnw.cmd spring-boot:run
```

### Opción 2: Si tienes Maven instalado globalmente
```powershell
cd microservicio-iso25010
mvn spring-boot:run
```

## Verificar que la aplicación está funcionando

Una vez que la aplicación esté ejecutándose, verás un mensaje como:
```
=================================================
🚀 Microservicio ISO/IEC 25010 iniciado exitosamente
📖 Swagger UI: http://localhost:8080/api/swagger-ui.html
🗄️  H2 Console: http://localhost:8080/api/h2-console
📊 API Docs: http://localhost:8080/api/api-docs
=================================================
```

## Acceder a la aplicación

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **H2 Console**: http://localhost:8080/api/h2-console
- **API Docs**: http://localhost:8080/api/api-docs

## Credenciales de la base de datos H2
- **URL**: jdbc:h2:mem:testdb
- **Usuario**: sa
- **Contraseña**: password

## Detener la aplicación
Presiona `Ctrl + C` en la terminal donde está ejecutándose la aplicación.

## Solución de problemas

### Error: "java no se reconoce como comando"
- Asegúrate de que Java 17 esté instalado correctamente
- Verifica que JAVA_HOME esté configurado en las variables de entorno
- Reinicia la terminal después de instalar Java

### Error: "mvn no se reconoce como comando"
- Usa el wrapper de Maven: `.\mvnw.cmd` en lugar de `mvn`
- O instala Maven globalmente

### Error de puerto en uso
- La aplicación usa el puerto 8080
- Si el puerto está ocupado, puedes cambiar el puerto en `src/main/resources/application.yml` 