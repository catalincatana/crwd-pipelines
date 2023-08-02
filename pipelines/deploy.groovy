#!groovy

pipeline {
    agent any
    stages {
        stage('Deploy') {
            steps {
                script {
                    sh '''
            echo "Deploying to ${params.Env}"
            ls -la
            '''
                }
            }
        }
    }
}
