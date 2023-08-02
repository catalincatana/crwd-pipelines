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
                container('docker') {
                    script {
                        docker.build('catalincatana/crwd-app:latest')
                    }
                }
            }
        }
        stage('Docker Push') {
            steps {
                container('docker') {
                    withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_CREDS', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
                        sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPassword}"
                        sh 'docker push catalincatana/crwd-repository:tagname'
                    }
                }
            }
        }
    }
}
