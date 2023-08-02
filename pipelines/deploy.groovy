#!groovy

pipeline {
    agent any
    stages {
        stage('Deploy') {
            steps {
                script {
                    kubernetesDeploy(configs:"deployment.yaml", "service.yaml", namespace: "crwd")
                }
            }
        }
    }
}
