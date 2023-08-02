#!groovy

pipeline {
    agent any
    stages {
        stage('Deploy') {
            steps {
                script {
                    kubernetesDeploy(configs:"k8sDeployFiles/crwdApp/deployment.yaml", "k8sDeployFiles/crwdApp/service.yaml")
                }
            }
        }
    }
}
