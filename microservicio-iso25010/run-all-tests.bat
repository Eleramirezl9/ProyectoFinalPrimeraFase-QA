@echo off
echo ========================================
echo Ejecutando Tests Completos
echo ========================================
cd /d "%~dp0"
call mvnw.cmd clean test
echo.
echo ========================================
echo Tests completados
echo ========================================
pause
