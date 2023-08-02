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
                        sh "echo ${params.TAG}"
                        docker.build("catalincatana/crwd-repository:${params.TAG}")
                    }
                }
            }
        }
        stage('Docker Push') {
            steps {
                container('docker') {
                    withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_CREDS', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
                        sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPassword}"
                        sh "docker push catalincatana/crwd-repository:${params.TAG}"
                    }
                }
            }
        }
        stage('Increment Version') {
            steps {
                // read VERSION file and increment the version
                script {
                    def version = readFile('VERSION').trim()
                    def versionParts = version.split('\\.')
                    def major = versionParts[0].toInteger()
                    def minor = versionParts[1].toInteger()
                    def patch = versionParts[2].toInteger()
                    patch++
                    def newVersion = "${major}.${minor}.${patch}"
                    writeFile file: 'VERSION', text: newVersion
                    echo "New version is ${newVersion}"
                    git push origin master
                }
            }
        }
    }
}
