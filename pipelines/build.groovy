#!groovy

podTemplate(containers: [
        containerTemplate(
                name: 'maven',
                image: 'maven:3.8.1-jdk-8',
                command: 'sleep',
                args: '30d'
        ),
        containerTemplate(
                name: 'python',
                image: 'python:latest',
                command: 'sleep',
                args: '30d')
]) {

    node(POD_LABEL) {
        stage('Get a Maven project') {
            git 'https://github.com/spring-projects/spring-petclinic.git'
            container('maven') {
                stage('Build a Maven project') {
                    sh '''
                    echo "maven build"
                    '''
                }
            }
        }

        stage('Get a Python Project') {
            git url: 'https://github.com/hashicorp/terraform.git', branch: 'main'
            container('python') {
                stage('Build a Go project') {
                    sh '''
                    echo "Go Build"
                    '''
                }
            }
        }

    }
}




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