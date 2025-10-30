@echo off
echo =================================================
echo 🚀 INICIANDO MICROSERVICIO ISO/IEC 25010
echo =================================================

REM ⚠️ IMPORTANTE: Crear archivo .env con credenciales reales
REM Este script requiere que las credenciales estén configuradas en .env

REM Verificar si existe archivo .env
if not exist .env (
    echo ❌ ERROR: Archivo .env no encontrado
    echo.
    echo Crea un archivo .env basado en .env.example
    echo Comando: copy .env.example .env
    echo Luego edita .env con tus credenciales
    echo.
    pause
    exit /b 1
)

REM Cargar variables desde .env (solo para desarrollo local)
for /f "usebackq tokens=1,* delims==" %%a in (.env) do (
    set "%%a=%%b"
)

REM Configuraciones adicionales
set DB_DRIVER=org.h2.Driver
set DB_PLATFORM=org.hibernate.dialect.H2Dialect
set DB_DDL_AUTO=create-drop
set DB_SHOW_SQL=true

REM Configuración H2 Console
set H2_CONSOLE_ENABLED=true
set H2_CONSOLE_PATH=/h2-console
set H2_WEB_ALLOW_OTHERS=false

REM Configuración CORS
set CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:4200,http://localhost:5173,http://127.0.0.1:3000,http://127.0.0.1:4200,http://127.0.0.1:5173
set CORS_ALLOWED_METHODS=GET,POST,PUT,PATCH,DELETE,OPTIONS
set CORS_ALLOW_CREDENTIALS=true

REM Configuración del servidor
set SERVER_PORT=8080
set CONTEXT_PATH=/api

REM Configuración de logging
set LOG_LEVEL_APP=DEBUG
set LOG_LEVEL_WEB=DEBUG
set LOG_LEVEL_SQL=DEBUG

REM Perfil activo
set SPRING_PROFILES_ACTIVE=dev

echo ✅ Variables de entorno cargadas desde .env
echo =================================================

REM Ejecutar aplicación
echo 🚀 Iniciando aplicación...
echo.

REM Usar Maven global directamente (más confiable)
mvn spring-boot:run

pause