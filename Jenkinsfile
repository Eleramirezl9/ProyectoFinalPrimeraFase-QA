pipeline {
    agent any

    tools {
        maven 'Maven 3.9'
    }

    environment {
        DOCKER_IMAGE = 'microservicio-iso25010'
        DOCKER_TAG = "${BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                echo '📥 Clonando código desde GitHub...'
                git branch: 'main',
                    url: 'https://github.com/Eleramirezl9/ProyectoFinalPrimeraFase-QA.git'
            }
        }

        stage('Build') {
            steps {
                echo '🔨 Compilando proyecto con Maven...'
                dir('microservicio-iso25010') {
                    sh 'chmod +x mvnw'
                    sh './mvnw clean compile'
                }
            }
        }

        stage('Test') {
            steps {
                echo '🧪 Ejecutando tests...'
                dir('microservicio-iso25010') {
                    sh './mvnw test'
                }
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                echo '📦 Empaquetando aplicación...'
                dir('microservicio-iso25010') {
                    sh './mvnw package -DskipTests'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo '🔍 Análisis de calidad de código con SonarQube...'
                dir('microservicio-iso25010') {
                    withSonarQubeEnv('SonarQube') {
                        sh """
                            ./mvnw sonar:sonar \
                            -Dsonar.projectKey=Eleramirezl9_ProyectoFinalPrimeraFase-QA \
                            -Dsonar.organization=eleramirezl9 \
                            -Dsonar.host.url=https://sonarcloud.io
                        """
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                echo '🐳 Construyendo imagen Docker...'
                dir('microservicio-iso25010') {
                    script {
                        sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                        sh "docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest"
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                echo '🚀 Desplegando aplicación...'
                script {
                    // Detener contenedor anterior si existe
                    sh '''
                        docker stop microservicio-app 2>/dev/null || echo "No previous container"
                        docker rm microservicio-app 2>/dev/null || echo "No previous container to remove"
                    '''

                    // Ejecutar nuevo contenedor
                    sh """
                        docker run -d \\
                        --name microservicio-app \\
                        -p 8081:8080 \\
                        -e SPRING_PROFILES_ACTIVE=prod \\
                        ${DOCKER_IMAGE}:latest
                    """
                }
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline ejecutado exitosamente!'
            echo '🌐 Aplicación disponible en: http://localhost:8081/api'
            echo '📖 Swagger UI: http://localhost:8081/api/swagger-ui.html'
        }
        failure {
            echo '❌ Pipeline falló. Revisa los logs.'
        }
        always {
            echo '🧹 Limpiando workspace...'
            cleanWs()
        }
    }
}
