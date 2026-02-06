#!/user/bin/env groovy

def call(Map config = [:]) {
    def cfg = [
        appDir: 'app',
        dockerRepo: '',
        gitBranch: 'main',
        dockerCredsId: 'dockerhub-creds',
        gitCredsId: 'git-https-creds',
        gitRemoteUrl: ''
    ] + config
    if (!cfg.dockerRepo?.trim()) {
        error 'dockerRepo is required'
    }
    if (!cfg.gitRemoteUrl?.trim()) {
        error 'gitRemoteUrl is required'
    }
    def gitRemoteHost = cfg.gitRemoteUrl.replace('https://', '')

    pipeline {
        agent any

        options {
            timestamps()
        }

        environment {
            APP_DIR = cfg.appDir
            DOCKER_REPO = cfg.dockerRepo
            GIT_BRANCH = cfg.gitBranch
            GIT_REMOTE_HOST = gitRemoteHost
        }

        stages {
            stage('Install Dependencies') {
                steps {
                    dir(APP_DIR) {
                        sh 'npm ci'
                    }
                }
            }

            stage('Run Tests') {
                steps {
                    dir(APP_DIR) {
                        sh 'npm test'
                    }
                }
            }

            stage('Increment Version') {
                steps {
                    dir(APP_DIR) {
                        sh 'npm version minor --no-git-tag-version'
                        script {
                            env.APP_VERSION = sh(
                                returnStdout: true,
                                script: "node -p \"require('./package.json').version\""
                            ).trim()
                        }
                    }
                }
            }

            stage('Build Docker Image') {
                steps {
                    dir(APP_DIR) {
                        sh "docker build -t ${DOCKER_REPO}:${APP_VERSION} ."
                    }
                }
            }

            stage('Push Docker Image') {
                steps {
                    withCredentials([
                        usernamePassword(
                            credentialsId: cfg.dockerCredsId,
                            usernameVariable: 'DOCKER_USER',
                            passwordVariable: 'DOCKER_PASS'
                        )
                    ]) {
                        sh 'echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin'
                        sh "docker push ${DOCKER_REPO}:${APP_VERSION}"
                        sh 'docker logout'
                    }
                }
            }

            stage('Commit & Push Version') {
                steps {
                    dir(APP_DIR) {
                        sh "git checkout -B ${GIT_BRANCH}"
                        sh 'git add package.json package-lock.json'
                        sh "git -c user.name='jenkins' -c user.email='jenkins@local' commit -m \"chore: bump version to ${APP_VERSION}\""
                    }
                    withCredentials([
                        usernamePassword(
                            credentialsId: cfg.gitCredsId,
                            usernameVariable: 'GIT_USER',
                            passwordVariable: 'GIT_PASS'
                        )
                    ]) {
                        sh "git remote set-url origin https://$GIT_USER:$GIT_PASS@${env.GIT_REMOTE_HOST}"
                        sh "git push origin ${GIT_BRANCH}"
                    }
                }
            }
        }
    }
}
