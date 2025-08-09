@echo off
echo Configurando Java...
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%

echo Verificando Java...
java -version

echo Descargando Maven...
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/apache/maven/apache-maven/3.9.5/apache-maven-3.9.5-bin.zip' -OutFile 'maven.zip'"

echo Extrayendo Maven...
powershell -Command "Expand-Archive -Path 'maven.zip' -DestinationPath '.' -Force"

echo Ejecutando el proyecto...
.\apache-maven-3.9.5\bin\mvn.cmd spring-boot:run

pause 