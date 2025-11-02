@echo off
REM =========================================================
REM Script de Conversión Automática Markdown a PDF
REM Universidad Mariano Gálvez de Guatemala
REM Grupo 6 - Aseguramiento de la Calidad
REM Fecha: 31 de octubre de 2025
REM =========================================================

echo.
echo ========================================================
echo  CONVERSIÓN DE DOCUMENTACIÓN QA A PDF
echo  Universidad Mariano Gálvez de Guatemala
echo  Grupo 6 - Aseguramiento de la Calidad
echo ========================================================
echo.

REM Verificar que pandoc está instalado
where pandoc >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Pandoc no está instalado.
    echo.
    echo Por favor, instala Pandoc desde:
    echo https://pandoc.org/installing.html
    echo.
    echo O ejecuta: choco install pandoc
    echo.
    pause
    exit /b 1
)

echo [INFO] Pandoc encontrado:
pandoc --version | findstr "pandoc"
echo.

REM Crear carpeta para PDFs si no existe
if not exist "pdf-generados" mkdir pdf-generados

echo --------------------------------------------------------
echo Iniciando conversión de documentos...
echo --------------------------------------------------------
echo.

REM Configuración común de Pandoc
set PANDOC_OPTS=--pdf-engine=xelatex -V geometry:margin=1in --toc --toc-depth=3 -N -V linkcolor:blue -V fontsize=11pt

REM Convertir Bitácora
echo [1/5] Convirtiendo Bitácora Consolidada...
pandoc 00-BITACORA-QA-CONSOLIDADA.md -o pdf-generados\00-BITACORA-QA-CONSOLIDADA.pdf %PANDOC_OPTS%
if %ERRORLEVEL% EQU 0 (
    echo       ✓ Bitácora convertida exitosamente
) else (
    echo       ✗ Error al convertir Bitácora
)
echo.

REM Convertir Informe Pruebas Unitarias
echo [2/5] Convirtiendo Informe de Pruebas Unitarias...
pandoc 01-INFORME-PRUEBAS-UNITARIAS.md -o pdf-generados\01-INFORME-PRUEBAS-UNITARIAS.pdf %PANDOC_OPTS%
if %ERRORLEVEL% EQU 0 (
    echo       ✓ Informe de Pruebas Unitarias convertido exitosamente
) else (
    echo       ✗ Error al convertir Informe de Pruebas Unitarias
)
echo.

REM Convertir Informe Pruebas Funcionales
echo [3/5] Convirtiendo Informe de Pruebas Funcionales...
pandoc 02-INFORME-PRUEBAS-FUNCIONALES.md -o pdf-generados\02-INFORME-PRUEBAS-FUNCIONALES.pdf %PANDOC_OPTS%
if %ERRORLEVEL% EQU 0 (
    echo       ✓ Informe de Pruebas Funcionales convertido exitosamente
) else (
    echo       ✗ Error al convertir Informe de Pruebas Funcionales
)
echo.

REM Convertir Informe Rendimiento
echo [4/5] Convirtiendo Informe de Rendimiento y Microservicios...
pandoc 03-INFORME-RENDIMIENTO-MICROSERVICIOS.md -o pdf-generados\03-INFORME-RENDIMIENTO-MICROSERVICIOS.pdf %PANDOC_OPTS%
if %ERRORLEVEL% EQU 0 (
    echo       ✓ Informe de Rendimiento convertido exitosamente
) else (
    echo       ✗ Error al convertir Informe de Rendimiento
)
echo.

REM Convertir Informe Final Consolidado
echo [5/5] Convirtiendo Informe Final Consolidado...
pandoc 04-INFORME-FINAL-CONSOLIDADO-QA.md -o pdf-generados\04-INFORME-FINAL-CONSOLIDADO-QA.pdf %PANDOC_OPTS%
if %ERRORLEVEL% EQU 0 (
    echo       ✓ Informe Final Consolidado convertido exitosamente
) else (
    echo       ✗ Error al convertir Informe Final Consolidado
)
echo.

echo ========================================================
echo  RESUMEN DE CONVERSIÓN
echo ========================================================
echo.

REM Contar PDFs generados
set PDF_COUNT=0
for %%f in (pdf-generados\*.pdf) do set /a PDF_COUNT+=1

echo Total de PDFs generados: %PDF_COUNT% de 5
echo.
echo Ubicación: %CD%\pdf-generados\
echo.

REM Listar archivos generados
echo Archivos generados:
echo.
dir /b pdf-generados\*.pdf 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo   [Ninguno - verifica errores arriba]
)

echo.
echo ========================================================
echo  CONVERSIÓN COMPLETADA
echo ========================================================
echo.
echo Para ver los PDFs, abre la carpeta:
echo %CD%\pdf-generados\
echo.

pause
