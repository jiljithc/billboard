pipeline {
    agent any

    environment {
        DOCKER_REGISTRY      = 'docker.io'
        DOCKER_IMAGE_NAME    = 'your-dockerhub-username/billboard-tracker'
        IMAGE_TAG            = "${BUILD_NUMBER}"
        DOCKER_HUB_CREDS_ID  = 'docker-hub-credentials'
        APP_PORT             = '8080'
    }

    tools {
       // maven 'Maven-3.9'
       // jdk 'JDK-21' // Or 'JDK-17' depending on the name set in Manage Jenkins -> Tools

        
        maven 'Maven-3.10'
        jdk 'JDK-26'
    
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code from Git repository...'
                checkout scm
            }
        }

        stage('Compile & Static Checks') {
            steps {
                echo 'Compiling Spring Boot backend application...'
                sh 'mvn clean compile'
            }
        }

        stage('Unit & Selenium Automation Tests') {
            steps {
                echo 'Executing JUnit 5 and Headless Selenium Browser Tests...'
                sh 'mvn test -DargLine="-Djava.awt.headless=true"'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package Executable JAR') {
            steps {
                echo 'Packaging standalone Spring Boot JAR artifact...'
                sh 'mvn package -DskipTests'
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "Building Docker image: ${DOCKER_IMAGE_NAME}:${IMAGE_TAG}"
                sh """
                    docker build -t ${DOCKER_IMAGE_NAME}:${IMAGE_TAG} -t ${DOCKER_IMAGE_NAME}:latest .
                """
            }
        }

        stage('Push Image to Docker Hub') {
            when {
                branch 'main'
            }
            steps {
                echo 'Authenticating and pushing image to container registry...'
                withCredentials([usernamePassword(credentialsId: "${DOCKER_HUB_CREDS_ID}", usernameVariable: 'DH_USER', passwordVariable: 'DH_PASS')]) {
                    sh """
                        echo "\$DH_PASS" | docker login -u "\$DH_USER" --password-stdin
                        docker push ${DOCKER_IMAGE_NAME}:${IMAGE_TAG}
                        docker push ${DOCKER_IMAGE_NAME}:latest
                        docker logout
                    """
                }
            }
        }

        stage('Deploy with Docker Compose') {
            when {
                branch 'main'
            }
            steps {
                echo 'Deploying application stack...'
                sh """
                    docker compose down || true
                    docker compose up -d --build
                """
            }
        }

        stage('Smoke Test & Health Check') {
            when {
                branch 'main'
            }
            steps {
                echo 'Verifying application status...'
                sh '''
                    for i in {1..12}; do
                        STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/actuator/health || true)
                        if [ "$STATUS" -eq 200 ]; then
                            echo "Application is UP and HEALTHY!"
                            exit 0
                        fi
                        echo "Waiting for app to start... (Attempt $i/12)"
                        sleep 5
                    done
                    echo "Healthcheck timed out! Spring Boot did not respond."
                    exit 1
                '''
            }
        }
    }

    post {
        success {
            echo "CI/CD Pipeline finished successfully for Build #${BUILD_NUMBER}!"
        }
        failure {
            echo "Pipeline build failed! Check console output for errors."
        }
        cleanup {
            sh 'docker image prune -f || true'
        }
    }
}
