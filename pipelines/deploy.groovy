#!groovy

pipeline {
    agent {
        kubernetes {
            defaultContainer 'jnlp'
            yamlFile 'podTemplates/agentpod.yaml'
        }
    }
    stages {
//        stage('Deploy') {
//            steps {
//                // Mount the Kubernetes configuration file from credentials
//                withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
//                    container('kubectl') {
//                        // Use the mounted kubeconfig file in the kubectl commands
//                        script {
//                                sh """
//                            echo "Deploying to ..."
//                            ls -la
//                            kubectl version
//                            kubectl --kubeconfig=${KUBECONFIG} get po -n devops-tools
//                            kubectl apply -f k8sDeployFiles/crwdApp/deployment.yaml --kubeconfig=${KUBECONFIG}
//                            kubectl set image deployments/crwd-app-deployment py-app=catalincatana/crwd-repository:${params.Version} -n crwd --kubeconfig=${KUBECONFIG}
//                        """
//                        }
//                    }
//                }
//            }
//        }
        stage('Run integration tests') {
            steps {
                container('kubectl') {
                    script {
                        sh """
                            wget crwd-app-service.crwd.svc.cluster.local:5001
                            output=\$(cat index.html)  
                            expected="Glad to see you again"
        
                            if [ "$output" = "$expected" ]; then
                                echo "Output matches the expected string."
                            else
                                echo "Output does not match the expected string."
                            fi
                        """
                    }
                }
            }
        }
    }
}
