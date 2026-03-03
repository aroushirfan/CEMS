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
                // Build only backend and its dependencies
                sh 'mvn clean package -DskipTests -pl backend -am'
            }
        }

        stage('Build Docker Image') {
            steps {
                // Build Docker image using root context so Maven can access all modules
                sh "docker build -t ${IMAGE_NAME}:latest -f backend/Dockerfile ."
            }
        }

        stage('Run Docker Container') {
            steps {
                // Remove existing container and run a new one
                sh """
                docker rm -f ${CONTAINER_NAME} || true
                docker run -d -p 8080:8080 --name ${CONTAINER_NAME} ${IMAGE_NAME}:latest
                """
            }
        }

        stage('Run Backend Tests') {
            steps {
                // Run backend integration tests only
                sh "mvn test -pl backend -Dtest=*IntegrationTest"
            }
        }
    }

    post {
        always {
            // Cleanup: remove container after pipeline
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