pipeline {
    agent any

    // 1️⃣ Parameters block
    // Removed so that default values can be set manually on Jenkins, insteaad of setting parameters every time
    // parameters {
    //     string(name: 'GITHUB_REPO', defaultValue: 'https://github.com/aroushirfan/CEMS.git', description: 'Git repository URL')
    //     string(name: 'DOCKERHUB_REPO', defaultValue: '', description: 'Docker Hub repository')
    //     string(name: 'DOCKERHUB_CREDENTIALS_ID', defaultValue: '', description: 'Docker Hub credentials ID')
    //     string(name: 'DOCKER_IMAGE_TAG', defaultValue: 'latest', description: 'Docker image tag')
    //     string(name: 'DB_USERNAME', defaultValue: '', description: 'Database username')
    //     string(name: 'DB_PASSWORD', defaultValue: '', description: 'Database password')
    //     string(name: 'PORT', defaultValue: '8080', description: 'Application port')
    //     string(name: 'DB_URL', defaultValue: '', description: 'Database URL')
    //     string(name: 'JWT_SECRET', defaultValue: '', description: 'JWT secret')
    //     string(name: 'MAVEN', defaultValue: 'Maven3', description: 'Maven')
    // }

    // 2️⃣ Environment block
    environment {
        DOCKERHUB_REPO = "${params.DOCKERHUB_REPO}"
        DOCKERHUB_CREDENTIALS_ID = "${params.DOCKERHUB_CREDENTIALS_ID}"
        DOCKER_IMAGE_TAG = "${params.DOCKER_IMAGE_TAG}"
        DB_USERNAME = "${params.DB_USERNAME}"
        DB_PASSWORD = "${params.DB_PASSWORD}"
        PORT = "${params.PORT}"
        DB_URL = "${params.DB_URL}"
        JWT_SECRET = "${params.JWT_SECRET}"
    }

    // 3️⃣ Tools block
    tools {
        maven "${params.MAVEN}"
    }

    stages {

        stage('Check Docker') {
            steps {
                sh 'docker --version'
            }
        }

        stage('Checkout') {
            steps {
                // Use parameterized repo
                git branch: 'main', url: "${params.GITHUB_REPO}"
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQubeServer') {
                    sh 'mvn clean verify sonar:sonar'
                }
            }
        }


        stage('Test') {
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

       stage('Build Frontend Docker Image') {
           steps {
               sh 'docker build --target frontend -t ${DOCKERHUB_REPO}_frontend:${DOCKER_IMAGE_TAG} .'
           }
       }
       stage('Build Backend Docker Image') {
            steps {
              sh 'docker build --target backend -t ${DOCKERHUB_REPO}_backend:${DOCKER_IMAGE_TAG} .'
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
                        docker push ${DOCKERHUB_REPO}_frontend:${DOCKER_IMAGE_TAG}
                        docker push ${DOCKERHUB_REPO}_backend:${DOCKER_IMAGE_TAG}
                        docker logout
                    '''
                }
            }
        }
    }
}
