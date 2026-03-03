//pipeline {
//    agent any
//
//    environment {
//        PATH = "/usr/local/bin:$PATH"
//        DOCKERHUB_REPO = "${params.DOCKERHUB_REPO}"
//        DOCKERHUB_CREDENTIALS_ID = "${params.DOCKERHUB_CREDENTIALS_ID}"
//        DOCKER_IMAGE_TAG = "${params.DOCKER_IMAGE_TAG}"
//        DB_USERNAME="${params.DB_USERNAME}"
//        DB_PASSWORD="${params.DB_PASSWORD}"
//        PORT="${params.PORT}"
//        DB_URL="${params.DB_URL}"
//        JWT_SECRET="${params.JWT_SECRET}"
//    }
//
//    tools {
//        maven 'Maven'
//        dockerTool 'local-docker'
//    }
//
//    stages {
//
//        stage('Check Docker') {
//            steps {
//                sh 'docker --version'
//            }
//        }
//
//        stage('Checkout') {
//            steps {
//                git branch: 'main',
//                url: "${params.GITHUB_REPO}"
//            }
//        }
//
//        stage('Build') {
//            steps {
//                sh 'mvn clean install'
//            }
//        }
//
//        stage('Test') {
//            steps {
//                sh 'mvn test'
//            }
//        }
//
//        stage('Code Coverage') {
//            steps {
//                sh 'mvn jacoco:report'
//            }
//        }
//
//        stage('Publish Test Results') {
//            steps {
//                junit '**/**/target/surefire-reports/*.xml'
//            }
//        }
//
//        stage('Publish Coverage Report') {
//            steps {
//                jacoco()
//            }
//        }
//
////        stage('Build Docker Image') {
////            steps {
////                sh 'docker build -t ${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG} .'
////            }
////        }
////
////
//        stage('Build Docker Image') {
//              steps {
//                 script {
//                     docker.build("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}")
//                 }
//              }
//        }
//
////         stage('Push Docker Image to Docker Hub') {
////             steps {
////                 // This helper handles the credentials securely without the Docker plugin
////                 withCredentials([usernamePassword(credentialsId: DOCKERHUB_CREDENTIALS_ID,
////                                                   passwordVariable: 'DOCKER_PASS',
////                                                   usernameVariable: 'DOCKER_USER')]) {
////                     sh """
////                         echo "${DOCKER_PASS}" | docker login -u "${DOCKER_USER}" --password-stdin
////                         docker push ${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}
////                         docker logout
////                     """
////                 }
////             }
////         }
//
//           stage('Push Docker Image to Docker Hub') {
//                steps {
//                    withCredentials([
//                        usernamePassword(
//                            credentialsId: "${DOCKERHUB_CREDENTIALS_ID}",
//                            usernameVariable: 'DOCKER_USER',
//                            passwordVariable: 'DOCKER_PASS'
//                        )
//                    ]) {
//                        sh '''
//                            echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
//                            docker push ${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}
//                            docker logout
//                        '''
//                    }
//                }
//            }
//    }
//}
//
//pipeline {
//    agent any
//
//    // 1️⃣ Parameters block
//    parameters {
//        string(name: 'GITHUB_REPO', defaultValue: 'https://github.com/aroushirfan/CEMS.git', description: 'Git repository URL')
//        string(name: 'DOCKERHUB_REPO', defaultValue: '', description: 'Docker Hub repository')
//        string(name: 'DOCKERHUB_CREDENTIALS_ID', defaultValue: '', description: 'Docker Hub credentials ID')
//        string(name: 'DOCKER_IMAGE_TAG', defaultValue: 'latest', description: 'Docker image tag')
//        string(name: 'DB_USERNAME', defaultValue: '', description: 'Database username')
//        string(name: 'DB_PASSWORD', defaultValue: '', description: 'Database password')
//        string(name: 'PORT', defaultValue: '8080', description: 'Application port')
//        string(name: 'DB_URL', defaultValue: '', description: 'Database URL')
//        string(name: 'JWT_SECRET', defaultValue: '', description: 'JWT secret')
//    }
//
//    // 2️⃣ Environment block
//    environment {
//        DOCKERHUB_REPO = "${params.DOCKERHUB_REPO}"
//        DOCKERHUB_CREDENTIALS_ID = "${params.DOCKERHUB_CREDENTIALS_ID}"
//        DOCKER_IMAGE_TAG = "${params.DOCKER_IMAGE_TAG}"
//        DB_USERNAME = "${params.DB_USERNAME}"
//        DB_PASSWORD = "${params.DB_PASSWORD}"
//        PORT = "${params.PORT}"
//        DB_URL = "${params.DB_URL}"
//        JWT_SECRET = "${params.JWT_SECRET}"
//    }
//
//    // 3️⃣ Tools block
//    tools {
//        maven 'Maven3'
//    }
//
//    stages {
//
//        stage('Check Docker') {
//            steps {
//                sh 'docker --version'
//            }
//        }
//
//        stage('Checkout') {
//            steps {
//                // Use parameterized repo
//                git branch: 'main', url: "${params.GITHUB_REPO}"
//            }
//        }
//
//        stage('Build') {
//            steps {
//                sh 'mvn clean install'
//            }
//        }
//
//        stage('Test') {
//            steps {
//                sh 'mvn test'
//            }
//        }
//
//        stage('Code Coverage') {
//            steps {
//                sh 'mvn jacoco:report'
//            }
//        }
//
//        stage('Publish Test Results') {
//            steps {
//                junit '**/**/target/surefire-reports/*.xml'
//            }
//        }
//
//        stage('Publish Coverage Report') {
//            steps {
//                jacoco()
//            }
//        }
//
//        stage('Build Docker Image') {
//            steps {
//                script {
//                    docker.build("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}")
//                }
//            }
//        }
//
//        stage('Push Docker Image to Docker Hub') {
//            steps {
//                withCredentials([
//                        usernamePassword(
//                                credentialsId: "${DOCKERHUB_CREDENTIALS_ID}",
//                                usernameVariable: 'DOCKER_USER',
//                                passwordVariable: 'DOCKER_PASS'
//                        )
//                ]) {
//                    sh '''
//                        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
//                        docker push ${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}
//                        docker logout
//                    '''
//                }
//            }
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
        maven 'Maven3'   // Make sures this matches your Jenkins Maven installation
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