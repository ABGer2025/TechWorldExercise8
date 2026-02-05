#!/usr/bin/env groovy

def call(String imageName, String imageTag = 'latest') {
    echo "Building Docker image: ${imageName}:${imageTag}"
    sh "docker build -t ${imageName}:${imageTag} ."
}
