#!groovy

pipeline {
    agent {
        kubernetes {
            defaultContainer 'jnlp'
            yamlFile 'podTemplates/agentpod.yaml'
        }
    }
    stages {
        stage('Deploy') {
            steps {
                container('kubectl') {
                    script {
                        sh '''
            echo "Deploying to ..."
            ls -la
            kubectl version
            kubectl get po -n devops-tools
            '''
                    }
                }
            }
        }
    }
}
