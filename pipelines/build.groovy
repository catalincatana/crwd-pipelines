#!groovy

pipeline {
    agent {
        kubernetes {
            defaultContainer 'jnlp'
            yamlFile 'podTemplates/agentpod.yaml'
        }
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                        credentialsId: 'GITHUB_SSH_KEY',
                        url: 'git@github.com:catalincatana/crwd-app.git'

            }
        }
        stage('Docker Build') {
            steps {
                script {
                    docker.build('catalincatana/crwd-app:latest')
                }
            }
        }
    }
}

//podTemplate(containers: [
//        containerTemplate(
//                name: 'docker',
//                image: 'docker:latest',
//                command: 'sleep',
//                args: '30d')
//]) {
//
//    node(POD_LABEL) {
//        stage('Get a Python Project') {
//            container('docker') {
//                stage('Checkout') {
//                    git branch: 'main',
//                            credentialsId: 'GITHUB_SSH_KEY',
//                            url: 'git@github.com:catalincatana/crwd-app.git'
//
//                }
//                stage('Docker Build') {
//                    script {
//                        docker.build('catalincatana/crwd-app:latest')
//                    }
//                }
//            }
//        }
//
//    }
//}




//
//pipeline {
//    agent any
//    stages {
//        stage('Checkout') {
//            steps {
//                git branch: 'main',
//                        credentialsId: 'GITHUB_SSH_KEY',
//                        url: 'git@github.com:catalincatana/crwd-app.git'
//
//            }
//        }
//        stage('Docker Build') {
//            steps {
//                script {
//                    docker.build('catalincatana/crwd-app:latest')
//                }
//            }
//        }
////        stage('Docker Push') {
////            agent any
////            steps {
////                withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_CREDS', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
////                    sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPassword}"
////                    sh 'docker push catalincatana/crwd-app:latest'
////                }
////            }
////        }
//    }
//}