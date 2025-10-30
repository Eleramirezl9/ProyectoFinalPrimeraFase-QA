@echo off
REM ===================================================================
REM Script para ejecutar pruebas unitarias del microservicio ISO 25010
REM ===================================================================

echo.
echo ====================================
echo   PRUEBAS UNITARIAS - ISO 25010
echo ====================================
echo.

REM Verificar que estamos en el directorio correcto
if not exist "pom.xml" (
    echo ERROR: No se encuentra pom.xml
    echo Ejecutar este script desde la carpeta microservicio-iso25010
    pause
    exit /b 1
)

REM Ejecutar según el parámetro recibido
if "%1"=="coverage" goto COVERAGE
if "%1"=="verify" goto VERIFY
if "%1"=="report" goto REPORT
if "%1"=="sonar" goto SONAR
goto DEFAULT

:DEFAULT
echo Ejecutando pruebas unitarias...
echo.
call "%~dp0mvnw.cmd" test
goto END

:COVERAGE
echo Ejecutando pruebas con reporte de cobertura...
echo.
call "%~dp0mvnw.cmd" clean test jacoco:report
echo.
echo Reporte generado en: target\site\jacoco\index.html
if exist "target\site\jacoco\index.html" (
    start target\site\jacoco\index.html
) else (
    echo ADVERTENCIA: No se encontro el reporte HTML
)
goto END

:VERIFY
echo Ejecutando pruebas y verificando cobertura minima (80%)...
echo.
call "%~dp0mvnw.cmd" clean verify
goto END

:REPORT
echo Generando solo reporte de cobertura...
echo.
call "%~dp0mvnw.cmd" jacoco:report
echo.
echo Reporte generado en: target\site\jacoco\index.html
if exist "target\site\jacoco\index.html" (
    start target\site\jacoco\index.html
) else (
    echo ADVERTENCIA: No se encontro el reporte HTML
)
goto END

:SONAR
echo Ejecutando analisis SonarQube...
echo.
if "%SONAR_TOKEN%"=="" (
    echo ERROR: Variable de entorno SONAR_TOKEN no configurada
    echo Configurar con: set SONAR_TOKEN=tu_token_aqui
    pause
    exit /b 1
)
call "%~dp0mvnw.cmd" clean verify sonar:sonar
goto END

:END
echo.
echo ====================================
echo   EJECUCION COMPLETADA
echo ====================================
echo.

REM Mostrar ayuda si no hay parámetros
if "%1"=="" (
    echo.
    echo OPCIONES DISPONIBLES:
    echo   run-tests.bat          - Ejecutar pruebas
    echo   run-tests.bat coverage - Ejecutar con cobertura y abrir reporte
    echo   run-tests.bat verify   - Verificar cobertura minima 80%%
    echo   run-tests.bat report   - Generar solo reporte
    echo   run-tests.bat sonar    - Ejecutar analisis SonarQube
    echo.
)

pause
