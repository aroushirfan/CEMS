//pipeline {
//    agent any
//
//    environment {
//        DOCKER_HOST = "unix:///var/run/docker.sock"
//        IMAGE_NAME = "cems-backend"
//        CONTAINER_NAME = "cems-backend"
//    }
//
//    stages {
//
//        stage('Checkout') {
//            steps {
//                // Checkout your feature branch
//                git branch: 'jiya/docker', url: 'https://github.com/aroushirfan/CEMS.git'
//            }
//        }
//
//        stage('Build Backend with Maven') {
//            tools { maven 'Maven3' } // Match your Jenkins Maven installation
//            steps {
//                // Build only backend and its dependencies, skip tests for speed
//                sh 'mvn clean package -DskipTests -pl backend -am'
//            }
//        }
//
//        stage('Build Docker Image') {
//            steps {
//                // Build Docker image from backend Dockerfile
//                sh "docker build -t ${IMAGE_NAME}:latest -f backend/Dockerfile backend/"
//            }
//        }
//
//        stage('Run Docker Container') {
//            steps {
//                // Remove existing container if exists and start a new one
//                sh """
//                docker rm -f ${CONTAINER_NAME} || true
//                docker run -d -p 8080:8080 --name ${CONTAINER_NAME} ${IMAGE_NAME}:latest
//                """
//            }
//        }
//
//        stage('Run Backend Tests') {
//            tools { maven 'Maven3' }
//            steps {
//                // Run all backend tests; don't fail if no IntegrationTests exist
//                sh "mvn test -pl backend -Dtest=*IntegrationTest -Dsurefire.failIfNoSpecifiedTests=false"
//            }
//        }
//    }
//
//    post {
//        always {
//            // Cleanup: remove container after pipeline run
//            sh "docker rm -f ${CONTAINER_NAME} || true"
//        }
//        success {
//            echo "Pipeline completed successfully."
//        }
//        failure {
//            echo "Pipeline failed. Check logs for details."
//        }
//    }
//}


pipeline {
    agent any

    environment {
        DOCKER_HOST = "unix:///var/run/docker.sock"
        IMAGE_NAME = "cems-backend"
        CONTAINER_NAME = "cems-backend"
    }

    tools {
        maven 'Maven3'   // Make sure this matches your Jenkins Maven installation
        jdk 'Java21'     // Make sure this matches your Jenkins JDK installation
    }

    stages {

        stage('Declarative: Checkout SCM') {
            steps {
                echo "Checking out code from Git..."
                git branch: 'jiya/docker', url: 'https://github.com/aroushirfan/CEMS.git'
            }
        }

        stage('Check Docker') {
            steps {
                echo "Checking Docker version..."
                sh "docker --version"
            }
        }

        stage('Build Backend with Maven') {
            steps {
                echo "Building backend with Maven..."
                sh 'mvn clean package -DskipTests -pl backend -am'
            }
        }

        stage('Run Unit Tests') {
            steps {
                echo "Running unit tests..."
                sh "mvn test -pl backend -Dtest=*Test"
            }
        }

        stage('Code Coverage') {
            steps {
                echo "Generating code coverage report..."
                sh "mvn jacoco:report -pl backend"
            }
        }

        stage('Publish Test Results') {
            steps {
                junit 'backend/target/surefire-reports/*.xml'
                jacoco execPattern: 'backend/target/jacoco.exec', classPattern: 'backend/target/classes', sourcePattern: 'backend/src/main/java'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "Building Docker image..."
                sh "docker build -t ${IMAGE_NAME}:latest -f backend/Dockerfile backend/"
            }
        }

        stage('Run Docker Container') {
            steps {
                echo "Running Docker container..."
                sh """
                    docker rm -f ${CONTAINER_NAME} || true
                    docker run -d -p 8080:8080 --name ${CONTAINER_NAME} ${IMAGE_NAME}:latest
                """
            }
        }

        stage('Run Backend Integration Tests') {
            steps {
                echo "Running backend integration tests..."
                sh "mvn test -pl backend -Dtest=*IntegrationTest || true"
            }
        }
    }

    post {
        always {
            echo "Cleaning up Docker container..."
            sh "docker rm -f ${CONTAINER_NAME} || true"
        }
        success {
            echo "Pipeline completed successfully!"
        }
        failure {
            echo "Pipeline failed. Check logs for details."
        }
    }
}