pipeline {
    agent any

    environment {
        PATH = "/usr/local/bin:$PATH"
        DOCKERHUB_REPO = "${params.DOCKERHUB_REPO}"
        DOCKERHUB_CREDENTIALS_ID = 'Docker_Hub'
        DOCKER_IMAGE_TAG = 'latest'
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
                url: "${params.GITHUB_REPO}"
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
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

//        stage('Build Docker Image') {
//            steps {
//                sh 'docker build -t ${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG} .'
//            }
//        }
//
//
        stage('Build Docker Image') {
              steps {
                 script {
                     docker.build("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}")
                 }
              }
        }

//         stage('Push Docker Image to Docker Hub') {
//             steps {
//                 // This helper handles the credentials securely without the Docker plugin
//                 withCredentials([usernamePassword(credentialsId: DOCKERHUB_CREDENTIALS_ID,
//                                                   passwordVariable: 'DOCKER_PASS',
//                                                   usernameVariable: 'DOCKER_USER')]) {
//                     sh """
//                         echo "${DOCKER_PASS}" | docker login -u "${DOCKER_USER}" --password-stdin
//                         docker push ${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}
//                         docker logout
//                     """
//                 }
//             }
//         }

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