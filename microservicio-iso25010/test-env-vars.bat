@echo off
echo =================================================
echo 🧪 PRUEBA DE VARIABLES DE ENTORNO
echo =================================================

REM Probar con credenciales diferentes para confirmar que funcionan
set DB_USERNAME=test_environment_user
set DB_PASSWORD=test_env_pass_12345
set DB_URL=jdbc:h2:mem:test_env_db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL;DATABASE_TO_LOWER=TRUE;INIT=CREATE DOMAIN IF NOT EXISTS TEXT AS VARCHAR^(255^)

echo ✅ Variables de entorno de PRUEBA configuradas:
echo 👤 Usuario: %DB_USERNAME%
echo 🔑 Password: %DB_PASSWORD%
echo 📍 Base de datos: test_env_db
echo =================================================

echo 🚀 Iniciando aplicación con variables de entorno...
mvnw.cmd spring-boot:run -Dspring.profiles.active=dev

pause