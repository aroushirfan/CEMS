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
                        url: 'https://github.com/aroushirfan/CEMS.git'
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
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
                    // Use root directory as context if Dockerfile is in repo root
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

        stage('Run Docker Container') {
            steps {
                script {
                    // Stop container if it already exists
                    sh 'docker rm -f cems-backend || true'

                    // Run the new container
                    sh "docker run -d --name cems-backend -p 8080:8080 ${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}"
                }
            }
        }
    }

    post {
        always {
            script {
                // Clean up container after pipeline
                sh 'docker rm -f cems-backend || true'
            }
        }
    }
}