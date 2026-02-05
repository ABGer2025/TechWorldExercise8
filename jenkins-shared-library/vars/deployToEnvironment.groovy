#!/usr/bin/env groovy

def call(String environment, String imageName, String imageTag) {
    echo "Deploying ${imageName}:${imageTag} to ${environment}"
    // Add deployment logic here
    sh "echo 'Deploying to ${environment}...'"
}
