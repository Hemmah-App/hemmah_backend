pipeline {
  agent any
  stages {
    stage('Show files') {
      steps {
        sh 'ls -la'
      }
    }

    stage('Checkout Code') {
      steps {
        git(url: 'https://github.com/Hemmah-App/hemmah_backend.git', branch: 'master')
      }
    }

    stage('Docker Login') {
      environment {
        DOCKER_USER = 'abdullahsayed'
        DOCKER_PASSWORD = 'html123CSS'
      }
      steps {
        sh 'sudo docker login -u $DOCKER_USER -p $DOCKER_PASSWORD'
      }
    }

  }
  environment {
    USER = 'jenkins'
  }
}