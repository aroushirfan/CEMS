pipeline {
    agent any

    environment {
        PATH = "/usr/local/bin:$PATH"

        DOCKERHUB_CREDENTIALS_ID = 'Docker_Hub'
        DOCKER_IMAGE_TAG = 'latest'
        DOCKERHUB_REPO = 'your-dockerhub-username/cems-backend' // <-- replace with your Docker Hub repo
    }

    tools {
        maven 'Maven'
        dockerTool 'local-docker'
    }

    stages {

        stage('Check Docker') {
            steps {
                sh 'docker --version'
            }
        }

        stage('Checkout') {
            steps {
                git branch: 'main',
                        url: 'https://github.com/puntawatsub/SoftwareEng_Temp_converter.git'
            }
        }

        stage('Build with Maven') {
            steps {
                // Only build backend for Docker; skip tests here
                sh 'mvn clean package -DskipTests -pl backend -am'
            }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Code Coverage') {
            steps {
                sh 'mvn jacoco:report'
            }
        }

        stage('Publish Test Results') {
            steps {
                junit '**/**/target/surefire-reports/*.xml'
            }
        }

        stage('Publish Coverage Report') {
            steps {
                jacoco()
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build Docker image from root (where Dockerfile is)
                    docker.build("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}", ".")
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                withCredentials([
                        usernamePassword(
                                credentialsId: "${DOCKERHUB_CREDENTIALS_ID}",
                                usernameVariable: 'DOCKER_USER',
                                passwordVariable: 'DOCKER_PASS'
                        )
                ]) {
                    sh '''
                        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                        docker push ${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}
                        docker logout
                    '''
                }
            }
        }
    }
}