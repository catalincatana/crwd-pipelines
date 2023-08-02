#!groovy

pipeline {
    agent any
    stages {
        stage('Checkout') {
            agent any
            steps {
                git url: 'git@github.com:catalincatana/crwd-app.git', branch: 'main',
                        credentialsId: 'GITHUB_SSH_KEY'
            }
        }
        stage('Docker Build') {
            agent any
            steps {
                sh 'ls -la'
                sh 'cd crwd-app'
                sh 'docker build -t catalincatana/crwd-app:latest .'
            }
        }
//        stage('Docker Push') {
//            agent any
//            steps {
//                withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_CREDS', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
//                    sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPassword}"
//                    sh 'docker push catalincatana/crwd-app:latest'
//                }
//            }
//        }
    }
}