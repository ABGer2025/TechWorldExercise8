#### This project is for the DevOps bootcamp exercise for

#### "Build Automation with Jenkins"

### Exercises Summary

#### Exercise 1: Dockerize NodeJS App
- Added a Dockerfile for the Node app and a .dockerignore in app/.
- Image builds from Node 18 Alpine and exposes port 3000.

#### Exercise 2: Full Jenkins Pipeline
- Jenkins pipeline runs npm ci, npm test, increments version (npm version minor --no-git-tag-version), builds and pushes Docker image, and commits version bump to Git.
- Docker image tagged with the application version and pushed to Docker Hub.

#### Exercise 3: Manual Deployment
- Pulled the new image on the droplet and ran the container on port 3000.
- Opened the firewall for port 3000 and verified the app is reachable.

#### Exercise 4: Jenkins Shared Library
- Extracted pipeline logic into jenkins-shared-library/vars/nodePipeline.groovy with parameters.
- Jenkinsfile now only calls the shared library pipeline.

##### Test
The project uses jest library for tests. (see "test" script in package.json)
There is 1 test (server.test.js) in the project that checks whether the main index.html file exists in the project. 

To run the nodejs test:

    npm run test

Make sure to download jest library before running test, otherwise jest command defined in package.json won't be found.

    npm install

In order to see failing test, remove index.html or rename it and run tests.
