#!groovy

pipeline {
    agent any
    stages {
        stage('Deploy') {
            steps {
                script {
                    sh '''
            echo "Deploying to ..."
            ls -la
            '''
                }
            }
        }
    }
}
