# crwd-pipelines
This repository contains all the files needed for CI/CD pipelines for the crwd-app.

These files are all used by a Jenkins instance which is installed on local k8s cluster.

The only manual steps are to install the k8s cluster and to install Jenkins on it along with the necessary plugins and secrets. 
Also, a new Jenkins Cloud has to be configured so it can spawn up new pods for some of the jobs and run the builds inside those pods.
