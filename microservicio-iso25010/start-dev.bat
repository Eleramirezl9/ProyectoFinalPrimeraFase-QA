@echo off
echo =================================================
echo 🚀 INICIANDO MICROSERVICIO ISO/IEC 25010
echo =================================================

REM Configurar variables de entorno desde .env
set DB_URL=jdbc:h2:mem:devdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL;DATABASE_TO_LOWER=TRUE;INIT=CREATE DOMAIN IF NOT EXISTS TEXT AS VARCHAR^(255^)
set DB_DRIVER=org.h2.Driver
set DB_USERNAME=dev_user
set DB_PASSWORD=dev_secure_password_2025
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

echo ✅ Variables de entorno configuradas
echo 📍 Base de datos: %DB_URL%
echo 👤 Usuario: %DB_USERNAME%
echo 🔑 Password: %DB_PASSWORD%
echo =================================================

REM Ejecutar aplicación
echo 🚀 Iniciando aplicación...
mvnw.cmd spring-boot:run

pause