pipeline {
  agent any
  stages {
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

    stage('Build And Push') {
      steps {
        sh 'sudo docker build -t abdullahsayed/hemmah_backend:latest . && sudo docker push abdullahsayed/hemmah_backend:latest'
      }
    }

    stage('') {
      steps {
        sh 'mvn clean && mvn install'
      }
    }

  }
  environment {
    USER = 'jenkins'
  }
}