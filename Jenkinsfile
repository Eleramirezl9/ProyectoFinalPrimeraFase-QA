// ==========================================
// Jenkins Pipeline - Microservicio ISO 25010
// ==========================================
// Pipeline optimizado con:
// - Cache de Maven
// - SonarQube opcional
// - Healthchecks después del deploy
// - Rollback automático si falla
// - Gestión correcta de contenedores

pipeline {
    agent any

    tools {
        maven 'Maven 3.9'
    }

    environment {
        // Configuración Docker
        DOCKER_IMAGE = 'microservicio-iso25010'
        DOCKER_TAG = "${BUILD_NUMBER}"
        CONTAINER_NAME = 'microservicio-app'

        // Puertos
        APP_PORT = '8080'

        // Configuración Maven
        MAVEN_OPTS = '-Dmaven.repo.local=/root/.m2/repository'
    }

    options {
        // Mantener solo los últimos 10 builds
        buildDiscarder(logRotator(numToKeepStr: '10'))

        // Timeout del pipeline
        timeout(time: 30, unit: 'MINUTES')

        // Timestamps en logs
        timestamps()
    }

    stages {
        // ==========================================
        // STAGE 1: Checkout
        // ==========================================
        stage('Checkout') {
            steps {
                echo '📥 Clonando código desde GitHub...'
                git branch: 'main',
                    url: 'https://github.com/Eleramirezl9/ProyectoFinalPrimeraFase-QA.git',
                    credentialsId: ''  // Añadir credenciales si es repositorio privado
            }
        }

        // ==========================================
        // STAGE 2: Build
        // ==========================================
        stage('Build') {
            steps {
                echo '🔨 Compilando proyecto con Maven...'
                dir('microservicio-iso25010') {
                    sh 'mvn clean compile ${MAVEN_OPTS}'
                }
            }
        }

        // ==========================================
        // STAGE 3: Test
        // ==========================================
        stage('Test') {
            steps {
                echo '🧪 Ejecutando tests unitarios...'
                dir('microservicio-iso25010') {
                    sh 'mvn test ${MAVEN_OPTS}'
                }
            }
            post {
                always {
                    // Publicar resultados de tests
                    junit allowEmptyResults: true,
                          testResults: 'microservicio-iso25010/target/surefire-reports/*.xml'

                    // Publicar reporte de cobertura (si está disponible)
                    script {
                        if (fileExists('microservicio-iso25010/target/site/jacoco/index.html')) {
                            publishHTML([
                                reportDir: 'microservicio-iso25010/target/site/jacoco',
                                reportFiles: 'index.html',
                                reportName: 'JaCoCo Coverage Report'
                            ])
                        }
                    }
                }
            }
        }

        // ==========================================
        // STAGE 4: Package
        // ==========================================
        stage('Package') {
            steps {
                echo '📦 Empaquetando aplicación JAR...'
                dir('microservicio-iso25010') {
                    sh 'mvn package -DskipTests ${MAVEN_OPTS}'
                }
            }
            post {
                success {
                    // Archivar el JAR generado
                    archiveArtifacts artifacts: 'microservicio-iso25010/target/*.jar',
                                     fingerprint: true
                }
            }
        }

        // ==========================================
        // STAGE 5: SonarQube Analysis (Opcional)
        // ==========================================
        stage('SonarQube Analysis') {
            when {
                // Habilitar solo si existe la credencial SONAR_TOKEN
                expression {
                    return env.SONAR_TOKEN != null ||
                           currentBuild.rawBuild.getCause(hudson.model.Cause$UserIdCause) != null
                }
            }
            steps {
                echo '🔍 Ejecutando análisis de calidad con SonarQube...'
                dir('microservicio-iso25010') {
                    script {
                        try {
                            withSonarQubeEnv('SonarQube') {
                                sh """
                                    mvn sonar:sonar \
                                    -Dsonar.projectKey=Eleramirezl9_ProyectoFinalPrimeraFase-QA \
                                    -Dsonar.organization=eleramirezl9 \
                                    -Dsonar.host.url=https://sonarcloud.io \
                                    ${MAVEN_OPTS}
                                """
                            }
                        } catch (Exception e) {
                            echo "⚠️  SonarQube falló, continuando pipeline: ${e.message}"
                        }
                    }
                }
            }
        }

        // ==========================================
        // STAGE 6: Build Docker Image
        // ==========================================
        stage('Build Docker Image') {
            steps {
                echo '🐳 Construyendo imagen Docker optimizada...'
                dir('microservicio-iso25010') {
                    script {
                        // Build con multi-stage para optimizar tamaño
                        sh """
                            docker build \
                            --tag ${DOCKER_IMAGE}:${DOCKER_TAG} \
                            --tag ${DOCKER_IMAGE}:latest \
                            --build-arg BUILD_DATE=\$(date -u +'%Y-%m-%dT%H:%M:%SZ') \
                            --build-arg VERSION=${BUILD_NUMBER} \
                            .
                        """

                        echo "✅ Imagen creada: ${DOCKER_IMAGE}:${DOCKER_TAG}"
                    }
                }
            }
        }

        // ==========================================
        // STAGE 7: Deploy
        // ==========================================
        stage('Deploy') {
            steps {
                echo '🚀 Desplegando aplicación en Docker...'
                script {
                    // Guardar nombre del contenedor anterior para rollback
                    def oldContainerExists = sh(
                        script: "docker ps -a -q -f name=${CONTAINER_NAME}",
                        returnStdout: true
                    ).trim()

                    if (oldContainerExists) {
                        echo "🔄 Renombrando contenedor anterior para rollback..."
                        sh """
                            docker rename ${CONTAINER_NAME} ${CONTAINER_NAME}-old 2>/dev/null || true
                            docker stop ${CONTAINER_NAME}-old 2>/dev/null || true
                        """
                    }

                    // Ejecutar nuevo contenedor
                    echo "🚢 Iniciando nuevo contenedor..."
                    sh """
                        docker run -d \
                        --name ${CONTAINER_NAME} \
                        --restart unless-stopped \
                        -p ${APP_PORT}:8080 \
                        -e SPRING_PROFILES_ACTIVE=prod \
                        -e LOG_LEVEL_APP=INFO \
                        -e LOG_LEVEL_WEB=WARN \
                        --health-cmd='curl -f http://localhost:8080/api/actuator/health || exit 1' \
                        --health-interval=10s \
                        --health-timeout=5s \
                        --health-retries=3 \
                        --health-start-period=30s \
                        ${DOCKER_IMAGE}:${DOCKER_TAG}
                    """

                    // Esperar a que el contenedor esté healthy
                    echo "⏳ Esperando a que la aplicación esté lista..."
                    def healthCheck = sh(
                        script: """
                            for i in {1..30}; do
                                if docker inspect --format='{{.State.Health.Status}}' ${CONTAINER_NAME} 2>/dev/null | grep -q 'healthy'; then
                                    echo "✅ Aplicación healthy"
                                    exit 0
                                fi
                                echo "Intento \$i/30: Esperando health check..."
                                sleep 2
                            done
                            echo "❌ Timeout esperando health check"
                            exit 1
                        """,
                        returnStatus: true
                    )

                    if (healthCheck != 0) {
                        error "❌ Health check falló después del deploy"
                    }

                    // Si todo OK, eliminar contenedor anterior
                    if (oldContainerExists) {
                        echo "🗑️  Eliminando contenedor anterior..."
                        sh "docker rm -f ${CONTAINER_NAME}-old 2>/dev/null || true"
                    }

                    // Limpiar imágenes antiguas (mantener últimas 3)
                    echo "🧹 Limpiando imágenes antiguas..."
                    sh """
                        docker images ${DOCKER_IMAGE} --format '{{.Tag}}' | \
                        grep -v 'latest' | sort -rn | tail -n +4 | \
                        xargs -r -I {} docker rmi ${DOCKER_IMAGE}:{} 2>/dev/null || true
                    """
                }
            }
            post {
                failure {
                    script {
                        echo "❌ Deploy falló, ejecutando rollback..."

                        // Detener contenedor fallido
                        sh "docker stop ${CONTAINER_NAME} 2>/dev/null || true"
                        sh "docker rm ${CONTAINER_NAME} 2>/dev/null || true"

                        // Restaurar contenedor anterior si existe
                        def oldContainerExists = sh(
                            script: "docker ps -a -q -f name=${CONTAINER_NAME}-old",
                            returnStdout: true
                        ).trim()

                        if (oldContainerExists) {
                            echo "🔙 Restaurando versión anterior..."
                            sh """
                                docker rename ${CONTAINER_NAME}-old ${CONTAINER_NAME}
                                docker start ${CONTAINER_NAME}
                            """
                            echo "✅ Rollback completado"
                        }
                    }
                }
            }
        }
    }

    // ==========================================
    // POST ACTIONS
    // ==========================================
    post {
        success {
            echo '✅ ============================================='
            echo '✅ Pipeline ejecutado exitosamente!'
            echo '✅ ============================================='
            echo "🌐 Aplicación: http://localhost:${APP_PORT}/api"
            echo "❤️  Health Check: http://localhost:${APP_PORT}/api/actuator/health"
            echo "🐳 Docker Image: ${DOCKER_IMAGE}:${DOCKER_TAG}"
            echo "📊 Build #${BUILD_NUMBER} completado"
        }
        failure {
            echo '❌ ============================================='
            echo '❌ Pipeline FALLÓ - Revisa los logs arriba'
            echo '❌ ============================================='
            echo "📋 Build #${BUILD_NUMBER} falló"
            echo "🔍 Verifica: logs de Docker, tests, compilación"
        }
        always {
            echo '🧹 Limpiando workspace...'
            // Mantener solo archivos esenciales
            cleanWs(
                deleteDirs: true,
                patterns: [
                    [pattern: '**/target/**', type: 'INCLUDE']
                ]
            )
        }
    }
}
