# Jenkins Shared Library

This is a Jenkins shared library that provides reusable pipeline steps and utilities.

## Structure

- `vars/` - Global variables and pipeline steps
- `src/` - Groovy classes and utilities
- `resources/` - Non-Groovy resources

## Usage

Configure this library in Jenkins:
1. Go to Manage Jenkins → Configure System
2. Under "Global Pipeline Libraries", add this repository
3. Use the library in your Jenkinsfile with `@Library('your-library-name') _`
