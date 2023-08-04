# crwd-pipelines
This repository contains all the files needed for CI/CD pipelines for the crwd-app.

These files are all used by a Jenkins instance which is installed on local k8s cluster.

Ngrok is used to expose the local Jenkins instance to the internet. This is needed for the github hook to work.

## Jenkins Configurations

Some of the Jenkins Plugins Used:
* Kubernetes
* Docker
* Docker Pipeline
* Github plugin
* Job DSL -> for seed job

Seed job manually created:
* crwd-pipelines Git repo is configured
* Build Steps -> Look on Filesystem -> seeds/crwdSeeds.groovy
* Every seed run must pe approved on the Jenkins in-process scripts approval config page

Credentials needed:
* GITHUB_SSH_KEY -> ssh private key to access github repos
* kubeconfig -> kubeconfig file to access local k8s cluster
* DOCKERHUB_CREDS -> credentials to push image to docker hub registry
* GITHUB_TOKEN -> github token to access github api (to autotrigger jenkins jobs)

Cloud Configured:
* Kubernetes Cloud -> configured to access local k8s cluster
* Manage Jenkins -> Clouds -> Add new cloud -> Kubernetes
* Cloud Details -> Namespace=devops-tools -> Test Connection
* Jenkins URL -> http://jenkins-service.devops-tools.svc.cluster.local:8080 
* Pod Label -> jenkins=agent

## Jenkins Jobs
### Start CrwdApp Pipeline
This is the pipeline which is triggered by the github hook.
It runs on Jenkins master node.
It calls Build and Deploy pipelines

### Build Crwd App
This pipeline builds the docker image of the crwd-app and pushes it to docker hub registry.
During the build process, pytests are run to validate the app is working as expected.
If pytests fail, the build will fail and all the pipeline will stop.
The build is run on a k8s pod. 
The pod contains 3 containers:
* docker -> used to build the docker image
* kubectl -> used to deploy the app to k8s cluster
* jnlp -> used to connect to Jenkins master node
Current job is using only the docker container and the jnlp container.

### Deploy Crwd App
This pipeline deploys the crwd-app to a local k8s cluster.
It uses the docker image built by the Build Crwd App pipeline.
The deployment is done using kubectl.
The deployment is run on a k8s pod. Its the same pod definition as in the Build Crwd App pipeline.
During the deployment pipeline, a simple integration test (written in bash) is run to validate the app is working as expected in Stage environment.
If the integration test fails, the deployment will fail and all the pipeline will stop so the code will not reach Production.

### Environments
There are 2 environments:
* Stage -> the app is deployed to this environment after the build pipeline is successful
* Production -> the app is deployed to this environment after the deployment pipeline is successful
The 2 environments are represented by 2 k8s namespaces:
* crwd -> Exposed on port 30111
* crwd-prod -> Exposed on port 30222
* Both environments are simulated on the same local k8s cluster as Jenkins.
From k8s perspective, each environment has 1 deployment resource and 1 service to access the ReplicaSets.






