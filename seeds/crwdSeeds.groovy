/*
    Seed jobs for generating the CI/CD Pipelines for CRWD-APP
*/

/*--------------------*/
/* Job that is automatically
triggered by git push to main branch*/
/*--------------------*/
pipelineJob('StartCrwdAppPipeline') {
    displayName('Starts the Crwd App Pipeline CI/CD Flow')

    properties {
        githubProjectUrl('https://github.com/catalincatana/crwd-app')
    }

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

/*--------------------*/
/* CI/CD pipelines */
/*--------------------*/
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

pipelineJob('DeployCrwdApp') {
    displayName('Deploy Crwd App to Stage and Prod')

    parameters {
        stringParam('Version', '', 'App version - latest - if not specified')
        choiceParam('Env', ['All', 'Stage', 'Prod'], 'Where do we deploy?')
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
            scriptPath('pipelines/deploy.groovy')
        }
    }
}
