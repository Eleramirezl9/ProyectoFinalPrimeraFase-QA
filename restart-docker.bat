@echo off
REM ==========================================
REM Script para reiniciar Docker completamente
REM ==========================================

echo.
echo ================================================
echo  Reinicio Completo de Docker
echo ================================================
echo.

REM Preguntar qué ambiente reiniciar
echo Selecciona el ambiente:
echo [1] Desarrollo (dev)
echo [2] Produccion (prod)
echo [3] CI/CD
echo [4] Todos
echo.
set /p choice="Opcion (1-4): "

if "%choice%"=="1" (
    echo.
    echo Reiniciando ambiente de DESARROLLO...
    docker-compose -f docker-compose.dev.yml down
    timeout /t 2 /nobreak >nul
    docker-compose -f docker-compose.dev.yml up -d
    goto :done
)

if "%choice%"=="2" (
    echo.
    echo Reiniciando ambiente de PRODUCCION...
    docker-compose down
    timeout /t 2 /nobreak >nul
    docker-compose up -d
    goto :done
)

if "%choice%"=="3" (
    echo.
    echo Reiniciando ambiente de CI/CD...
    docker-compose -f docker-compose.ci.yml down
    timeout /t 2 /nobreak >nul
    docker-compose -f docker-compose.ci.yml up -d
    goto :done
)

if "%choice%"=="4" (
    echo.
    echo Deteniendo TODOS los ambientes...
    docker-compose -f docker-compose.dev.yml down 2>nul
    docker-compose down 2>nul
    docker-compose -f docker-compose.ci.yml down 2>nul

    echo.
    echo Limpiando contenedores huerfanos...
    docker container prune -f

    echo.
    echo Selecciona cual ambiente iniciar:
    echo [1] Desarrollo
    echo [2] Produccion
    echo [3] CI/CD
    set /p start_choice="Opcion (1-3): "

    if "%start_choice%"=="1" docker-compose -f docker-compose.dev.yml up -d
    if "%start_choice%"=="2" docker-compose up -d
    if "%start_choice%"=="3" docker-compose -f docker-compose.ci.yml up -d
    goto :done
)

echo Opcion invalida
goto :eof

:done
echo.
echo ================================================
echo  Reinicio completado!
echo ================================================
echo.
echo Verificando estado de contenedores...
docker ps
echo.
echo Para ver logs en tiempo real usa:
echo   docker-compose logs -f
echo.
pause
