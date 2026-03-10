
pipeline {
    agent any

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
        jdk 'Jdk@21'
        maven "${params.MAVEN}"
    }

    stages {

        stage('Check Java') {
            steps {
                bat 'java -version'
                bat 'echo %JAVA_HOME%'
                bat 'mvn -version'
            }
        }

        stage('Check Docker') {
            steps {
                bat 'docker --version'
            }
        }


        stage('Checkout') {
            steps {
                git branch: "${params.MAVEN}", url: "${params.GITHUB_REPO}"
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean install -DskipTests'
            }
        }

        // stage('Test') {
        //     steps {
        //             withEnv([
        //             'DB_URL=jdbc:mariadb://localhost:3306/cems_db',
        //             'DB_USERNAME=root',
        //             'DB_PASSWORD=root'
        //         ]) {
        //             bat 'mvn test -DDB_URL=$DB_URL -DDB_USERNAME=$DB_USERNAME -DDB_PASSWORD=$DB_PASSWORD'
        //         }
        //     }
        // }

        // stage('Code Coverage') {
        //     steps {
        //         bat 'mvn jacoco:report'
        //     }
        // }

        // stage('Publish Test Results') {
        //     steps {
        //         junit '**/**/target/surefire-reports/*.xml'
        //     }
        // }

        // stage('Publish Coverage Report') {
        //     steps {
        //         jacoco()
        //     }
        // }

        stage('Build Frontend Docker Image') {
            steps {
                bat "docker build --target frontend -t ${DOCKERHUB_REPO}_frontend:${DOCKER_IMAGE_TAG} ."
            }
        }
        stage('Build Backend Docker Image') {
            steps {
                bat "docker build --target backend -t ${DOCKERHUB_REPO}_backend:${DOCKER_IMAGE_TAG} ."
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', env.DOCKERHUB_CREDENTIALS_ID) {
                        docker.image("${DOCKERHUB_REPO}_frontend:${DOCKER_IMAGE_TAG}").push()
                        docker.image("${DOCKERHUB_REPO}_backend:${DOCKER_IMAGE_TAG}").push()
                    }
                }
            }
        }
    }
    post {
        always {
            bat "docker logout"
        }
        success {
            echo "Image published to Docker Hub successfully."
        }
        failure {
            echo 'Pipeline failed. Check the logs above for details.'
        }
    }
}