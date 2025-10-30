@echo off
REM ==========================================
REM Script de Deployment - Microservicio ISO 25010
REM ==========================================
REM Uso: deploy.bat [dev|ci|prod]
REM
REM Ejemplos:
REM   deploy.bat dev    - Inicia entorno de desarrollo
REM   deploy.bat ci     - Inicia Jenkins + microservicio staging
REM   deploy.bat prod   - Inicia entorno de producción
REM   deploy.bat stop   - Detiene todos los servicios
REM   deploy.bat logs   - Muestra logs de los servicios

setlocal enabledelayedexpansion

REM ==========================================
REM CONFIGURACIÓN
REM ==========================================
set PROJECT_NAME=Microservicio ISO 25010
set COMPOSE_PROJECT=microservicio-iso25010

REM ==========================================
REM COLORES (deshabilitados para compatibilidad Windows)
REM ==========================================
set "GREEN="
set "YELLOW="
set "RED="
set "BLUE="
set "RESET="

REM ==========================================
REM BANNER
REM ==========================================
cls
echo %BLUE%
echo ==========================================
echo   %PROJECT_NAME%
echo   Script de Deployment
echo ==========================================
echo %RESET%

REM ==========================================
REM VERIFICAR PREREQUISITOS
REM ==========================================
echo %YELLOW%Verificando prerequisitos...%RESET%

REM Verificar Docker
docker --version >nul 2>&1
if errorlevel 1 (
    echo %RED%ERROR: Docker no esta instalado o no esta en el PATH%RESET%
    echo Por favor instala Docker Desktop: https://www.docker.com/products/docker-desktop
    pause
    exit /b 1
)
echo %GREEN%✓ Docker instalado%RESET%

REM Verificar Docker Compose
docker-compose --version >nul 2>&1
if errorlevel 1 (
    echo %RED%ERROR: Docker Compose no esta instalado%RESET%
    pause
    exit /b 1
)
echo %GREEN%✓ Docker Compose instalado%RESET%

REM ==========================================
REM PROCESAR ARGUMENTOS
REM ==========================================
set "ENV=%1"

if "%ENV%"=="" (
    echo.
    echo %YELLOW%Uso: deploy.bat [dev^|ci^|prod^|stop^|logs]%RESET%
    echo.
    echo Opciones:
    echo   dev     - Entorno de desarrollo local
    echo   ci      - Jenkins + Microservicio staging
    echo   prod    - Entorno de produccion
    echo   stop    - Detener todos los servicios
    echo   logs    - Ver logs de los servicios
    echo   restart - Reiniciar servicios
    echo.
    pause
    exit /b 1
)

REM ==========================================
REM COMANDOS
REM ==========================================

if /i "%ENV%"=="stop" (
    echo.
    echo %YELLOW%Deteniendo todos los servicios...%RESET%
    docker-compose -f docker-compose.yml down
    docker-compose -f docker-compose.dev.yml down
    docker-compose -f docker-compose.ci.yml down
    echo %GREEN%✓ Servicios detenidos%RESET%
    pause
    exit /b 0
)

if /i "%ENV%"=="logs" (
    echo.
    echo %YELLOW%Mostrando logs... (Ctrl+C para salir)%RESET%
    docker-compose -f docker-compose.yml logs -f
    exit /b 0
)

if /i "%ENV%"=="restart" (
    echo.
    echo %YELLOW%Reiniciando servicios...%RESET%
    call deploy.bat stop
    call deploy.bat prod
    exit /b 0
)

REM ==========================================
REM DEPLOYMENT POR ENTORNO
REM ==========================================

if /i "%ENV%"=="dev" (
    echo.
    echo %BLUE%==========================================
    echo   ENTORNO: DESARROLLO
    echo ==========================================%RESET%
    echo.
    echo Configuracion:
    echo   - Perfil: dev
    echo   - Puerto: 8080
    echo   - H2 Console: Habilitada
    echo   - Swagger: Habilitado
    echo   - Logs: DEBUG
    echo.

    echo %YELLOW%Deteniendo servicios previos...%RESET%
    docker-compose -f docker-compose.dev.yml down

    echo %YELLOW%Iniciando servicios de desarrollo...%RESET%
    docker-compose -f docker-compose.dev.yml up -d

    if errorlevel 1 (
        echo %RED%ERROR al iniciar servicios%RESET%
        pause
        exit /b 1
    )

    echo.
    echo %GREEN%✓ Servicios de desarrollo iniciados%RESET%
    echo.
    echo URLs disponibles:
    echo   - API: http://localhost:8080/api
    echo   - Swagger: http://localhost:8080/api/swagger-ui.html
    echo   - H2 Console: http://localhost:8080/api/h2-console
    echo   - Health: http://localhost:8080/api/actuator/health
    echo   - Adminer: http://localhost:8081
    echo.
    echo %YELLOW%Ver logs:%RESET% docker-compose -f docker-compose.dev.yml logs -f
    echo %YELLOW%Detener:%RESET% deploy.bat stop
    echo.
    pause
    exit /b 0
)

if /i "%ENV%"=="ci" (
    echo.
    echo %BLUE%==========================================
    echo   ENTORNO: CI/CD (Jenkins)
    echo ==========================================%RESET%
    echo.
    echo Configuracion:
    echo   - Jenkins: 8082
    echo   - Microservicio Staging: 8080
    echo   - Perfil: test
    echo.

    echo %YELLOW%Deteniendo servicios previos...%RESET%
    docker-compose -f docker-compose.ci.yml down

    echo %YELLOW%Iniciando servicios CI/CD...%RESET%
    echo %YELLOW%(Esto puede tardar varios minutos en el primer inicio)%RESET%
    docker-compose -f docker-compose.ci.yml up -d

    if errorlevel 1 (
        echo %RED%ERROR al iniciar servicios%RESET%
        pause
        exit /b 1
    )

    echo.
    echo %GREEN%✓ Servicios CI/CD iniciados%RESET%
    echo.
    echo URLs disponibles:
    echo   - Jenkins: http://localhost:8082
    echo   - Microservicio Staging: http://localhost:8080/api
    echo   - Swagger Staging: http://localhost:8080/api/swagger-ui.html
    echo.
    echo %YELLOW%Nota:%RESET% Jenkins puede tardar 2-3 minutos en estar listo
    echo %YELLOW%Ver logs Jenkins:%RESET% docker logs -f jenkins-server
    echo %YELLOW%Detener:%RESET% deploy.bat stop
    echo.
    pause
    exit /b 0
)

if /i "%ENV%"=="prod" (
    echo.
    echo %BLUE%==========================================
    echo   ENTORNO: PRODUCCION
    echo ==========================================%RESET%
    echo.
    echo Configuracion:
    echo   - Jenkins: 8082
    echo   - Microservicio: 8080
    echo   - Perfil: prod
    echo   - H2 Console: Deshabilitada
    echo   - Swagger: Deshabilitado
    echo   - Logs: INFO/WARN
    echo.

    echo %YELLOW%Deteniendo servicios previos...%RESET%
    docker-compose down

    echo %YELLOW%Iniciando servicios de produccion...%RESET%
    docker-compose up -d

    if errorlevel 1 (
        echo %RED%ERROR al iniciar servicios%RESET%
        pause
        exit /b 1
    )

    echo.
    echo %GREEN%✓ Servicios de produccion iniciados%RESET%
    echo.
    echo URLs disponibles:
    echo   - Jenkins: http://localhost:8082
    echo   - API: http://localhost:8080/api
    echo   - Health: http://localhost:8080/api/actuator/health
    echo.
    echo %YELLOW%Ver logs:%RESET% docker-compose logs -f
    echo %YELLOW%Detener:%RESET% deploy.bat stop
    echo.
    pause
    exit /b 0
)

REM ==========================================
REM ENTORNO NO RECONOCIDO
REM ==========================================
echo %RED%ERROR: Entorno '%ENV%' no reconocido%RESET%
echo.
echo Entornos disponibles: dev, ci, prod, stop, logs
echo.
pause
exit /b 1
