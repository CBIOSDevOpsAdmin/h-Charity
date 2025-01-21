pipeline {
    agent any
    parameters {
        string(name: 'BRANCH_NAME', defaultValue: 'main', description: 'Branch to build and deploy')
    }
    environment {
        DOCKER_UI_IMAGE = "donations-angular-app"
        DOCKER_API_IMAGE = "donations-java-api"
        REPO_URL = "https://github.com/CBIOSDevOpsAdmin/h-Charity.git"
    }
    stages {
         stage('Checkout') {
            steps {
                script {
                    checkout([$class: 'GitSCM', 
                        branches: [[name: "*/${BRANCH_NAME}"]],
                        userRemoteConfigs: [[
                            url: 'https://github.com/CBIOSDevOpsAdmin/h-charity.git',
                            credentialsId: 'github-access-token' 
                        ]]
                    ])
                }
            }
        }
        stage('Build Angular App') {
            steps {
                script {
                    dir('h-charity-ui') {
                        sh 'npm install' // Install dependencies
                        sh 'ng build --prod' // Build the Angular app
                        sh 'docker build -t ${DOCKER_UI_IMAGE} .' // Build Docker image
                    }
                }
            }
        }
        stage('Build Spring Boot API') {
            steps {
                script {
                    dir('h-charity-api') {
                        sh './mvnw clean package -DskipTests' // Build the Spring Boot JAR
                        sh 'docker build -t ${DOCKER_API_IMAGE} .' // Build Docker image
                    }
                }
            }
        }
        stage('Docker Compose Setup') {
            steps {
                script {
                    // Start containers using docker-compose
                    sh 'docker-compose up -d --build'
                }
            }
        }
    }
    post {
        always {
            // Clean up unused Docker images
            sh 'docker system prune -f'
        }
        success {
            echo 'Deployment successful!'
        }
        failure {
            echo 'Deployment failed.'
        }
    }
}
