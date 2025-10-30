# Script de verificacion de requisitos para ejecutar pruebas
# Verifica Java, Maven y archivos necesarios

$ErrorActionPreference = "SilentlyContinue"

Write-Host ""
Write-Host "================================================" -ForegroundColor Cyan
Write-Host "  VERIFICACION DE REQUISITOS - ISO 25010" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

$allOk = $true

# 1. Verificar Java
Write-Host "1. Verificando Java..." -ForegroundColor Yellow
$javaVersion = & java -version 2>&1
if ($LASTEXITCODE -eq 0) {
    Write-Host "   [OK] Java instalado" -ForegroundColor Green
    Write-Host "   Version: $($javaVersion[0])" -ForegroundColor Gray

    # Verificar JAVA_HOME
    if ($env:JAVA_HOME) {
        Write-Host "   [OK] JAVA_HOME configurado: $env:JAVA_HOME" -ForegroundColor Green
    } else {
        Write-Host "   [ADVERTENCIA] JAVA_HOME no configurado" -ForegroundColor Yellow
    }
} else {
    Write-Host "   [ERROR] Java NO instalado" -ForegroundColor Red
    Write-Host "   Instalar desde: https://adoptium.net/" -ForegroundColor Gray
    $allOk = $false
}

Write-Host ""

# 2. Verificar Maven
Write-Host "2. Verificando Maven..." -ForegroundColor Yellow
$mavenVersion = & mvn --version 2>&1
if ($LASTEXITCODE -eq 0) {
    Write-Host "   [OK] Maven instalado" -ForegroundColor Green
    $versionLine = $mavenVersion | Select-Object -First 1
    Write-Host "   Version: $versionLine" -ForegroundColor Gray

    # Verificar MAVEN_HOME
    if ($env:MAVEN_HOME) {
        Write-Host "   [OK] MAVEN_HOME configurado: $env:MAVEN_HOME" -ForegroundColor Green
    } else {
        Write-Host "   [ADVERTENCIA] MAVEN_HOME no configurado (opcional)" -ForegroundColor Yellow
    }
} else {
    Write-Host "   [ERROR] Maven NO instalado" -ForegroundColor Red
    Write-Host "   Instalar desde: https://maven.apache.org/download.cgi" -ForegroundColor Gray
    Write-Host "   O seguir guia: INSTALACION-MAVEN.md" -ForegroundColor Gray
    $allOk = $false
}

Write-Host ""

# 3. Verificar Maven Wrapper
Write-Host "3. Verificando Maven Wrapper..." -ForegroundColor Yellow
if (Test-Path "mvnw.cmd") {
    Write-Host "   [OK] mvnw.cmd encontrado" -ForegroundColor Green
} else {
    Write-Host "   [ERROR] mvnw.cmd no encontrado" -ForegroundColor Red
    Write-Host "   Asegurate de estar en el directorio microservicio-iso25010" -ForegroundColor Gray
    $allOk = $false
}

Write-Host ""

# 4. Verificar pom.xml
Write-Host "4. Verificando estructura del proyecto..." -ForegroundColor Yellow
if (Test-Path "pom.xml") {
    Write-Host "   [OK] pom.xml encontrado" -ForegroundColor Green
} else {
    Write-Host "   [ERROR] pom.xml no encontrado" -ForegroundColor Red
    Write-Host "   Asegurate de estar en el directorio microservicio-iso25010" -ForegroundColor Gray
    $allOk = $false
}

if (Test-Path "src/main/java") {
    Write-Host "   [OK] src/main/java encontrado" -ForegroundColor Green
} else {
    Write-Host "   [ERROR] src/main/java no encontrado" -ForegroundColor Red
    $allOk = $false
}

if (Test-Path "src/test/java") {
    Write-Host "   [OK] src/test/java encontrado" -ForegroundColor Green
} else {
    Write-Host "   [ERROR] src/test/java no encontrado" -ForegroundColor Red
    $allOk = $false
}

Write-Host ""

# 5. Verificar scripts de ejecucion
Write-Host "5. Verificando scripts de ejecucion..." -ForegroundColor Yellow
if (Test-Path "run-tests.ps1") {
    Write-Host "   [OK] run-tests.ps1 encontrado" -ForegroundColor Green
} else {
    Write-Host "   [ADVERTENCIA] run-tests.ps1 no encontrado" -ForegroundColor Yellow
}

if (Test-Path "run-tests.bat") {
    Write-Host "   [OK] run-tests.bat encontrado" -ForegroundColor Green
} else {
    Write-Host "   [ADVERTENCIA] run-tests.bat no encontrado" -ForegroundColor Yellow
}

Write-Host ""

# 6. Verificar conexion a internet
Write-Host "6. Verificando conexion a internet..." -ForegroundColor Yellow
$ping = Test-Connection -ComputerName "maven.apache.org" -Count 1 -Quiet
if ($ping) {
    Write-Host "   [OK] Conexion a internet disponible" -ForegroundColor Green
} else {
    Write-Host "   [ADVERTENCIA] No se puede conectar a maven.apache.org" -ForegroundColor Yellow
    Write-Host "   Maven necesita internet para descargar dependencias" -ForegroundColor Gray
}

Write-Host ""
Write-Host "================================================" -ForegroundColor Cyan

# Resultado final
if ($allOk) {
    Write-Host "  RESULTADO: TODO OK - Listo para ejecutar pruebas" -ForegroundColor Green
    Write-Host "================================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Comandos disponibles:" -ForegroundColor Cyan
    Write-Host "  .\run-tests.ps1 coverage  - Ejecutar pruebas con cobertura" -ForegroundColor White
    Write-Host "  mvn clean test            - Ejecutar con Maven directo" -ForegroundColor White
    Write-Host "  .\mvnw.cmd clean test     - Ejecutar con Maven Wrapper" -ForegroundColor White
} else {
    Write-Host "  RESULTADO: FALTAN REQUISITOS" -ForegroundColor Red
    Write-Host "================================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Pasos siguientes:" -ForegroundColor Yellow
    Write-Host "  1. Revisar errores arriba marcados con [ERROR]" -ForegroundColor White
    Write-Host "  2. Instalar Java y/o Maven segun sea necesario" -ForegroundColor White
    Write-Host "  3. Ver guia: INSTALACION-MAVEN.md" -ForegroundColor White
    Write-Host "  4. Ejecutar este script de nuevo para verificar" -ForegroundColor White
}

Write-Host ""
Write-Host "Documentacion:" -ForegroundColor Cyan
Write-Host "  INSTALACION-MAVEN.md  - Guia de instalacion" -ForegroundColor Gray
Write-Host "  INICIO-RAPIDO.md      - Guia de ejecucion" -ForegroundColor Gray
Write-Host "  README-TESTS.md       - Documentacion completa" -ForegroundColor Gray
Write-Host ""
