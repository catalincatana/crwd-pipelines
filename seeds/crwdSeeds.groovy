job('demo') {
    steps {
        shell('echo Hello World!')
    }
}

pipelineJob('BuildCrwdApp') {
    displayName('Build Crwd App')

    parameters {
        stringParam('TAG', '', 'Docker tag for the app')
    }

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url 'git@github.com:catalincatana/crwd-pipelines.git'
                        credentials('GITHUB_SSH_KEY')
                    }
                    branch('main')
                }
            }
            scriptPath('pipelines/build.groovy')
        }
    }
}

pipelineJob('StartCrwdAppPipeline') {
    displayName('Starts the Crwd App Pipeline CI/CD Flow')

    triggers {
        githubPush()
    }

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url 'git@github.com:catalincatana/crwd-app.git'
                        credentials('GITHUB_SSH_KEY')
                    }
                    branch('main')
                }
            }
            scriptPath('jenkins/startPipeline.groovy')
        }
    }
}