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
                git branch: 'main', url: 'https://github.com/aroushirfan/CEMS.git'
            }
        }

        stage('Build Backend with Maven') {
            tools { maven 'Maven3' } // Must match Jenkins Maven installation
            steps {
                // Build only backend and its dependencies
                sh 'mvn clean package -DskipTests -pl backend -am'
            }
        }

        stage('Build Docker Image') {
            steps {
                // Dockerfile should be inside backend/ folder
                sh "docker build -t ${IMAGE_NAME}:latest backend/"
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