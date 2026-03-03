pipeline {
    agent any

    environment {
        DOCKER_HOST = "unix:///var/run/docker.sock"
        IMAGE_NAME = "cems-backend"
        CONTAINER_NAME = "cems-backend"
    }

    stages {

        stage('Checkout') {
            steps {
                // Checkout your feature branch
                git branch: 'jiya/docker', url: 'https://github.com/aroushirfan/CEMS.git'
            }
        }

        stage('Build Backend with Maven') {
            tools { maven 'Maven3' } // Match your Jenkins Maven installation
            steps {
                // Build only backend and its dependencies, skip tests for speed
                sh 'mvn clean package -DskipTests -pl backend -am'
            }
        }

        stage('Build Docker Image') {
            steps {
                // Build Docker image from backend Dockerfile
                sh "docker build -t ${IMAGE_NAME}:latest -f backend/Dockerfile backend/"
            }
        }

        stage('Run Docker Container') {
            steps {
                // Remove existing container if exists and start a new one
                sh """
                docker rm -f ${CONTAINER_NAME} || true
                docker run -d -p 8080:8080 --name ${CONTAINER_NAME} ${IMAGE_NAME}:latest
                """
            }
        }

        stage('Run Backend Tests') {
            tools { maven 'Maven3' }
            steps {
                // Run all backend tests; don't fail if no IntegrationTests exist
                sh "mvn test -pl backend -Dtest=*IntegrationTest -Dsurefire.failIfNoSpecifiedTests=false"
            }
        }
    }

    post {
        always {
            // Cleanup: remove container after pipeline run
            sh "docker rm -f ${CONTAINER_NAME} || true"
        }
        success {
            echo "Pipeline completed successfully."
        }
        failure {
            echo "Pipeline failed. Check logs for details."
        }
    }
}