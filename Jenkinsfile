pipeline {
  agent any
  stages {

    stage('Git - Checkout Code') {
      steps {
        git(url: 'https://github.com/Hemmah-App/hemmah_backend.git', branch: 'master', credentialsId: 'jenkins-git-token')
      }
    }

    stage('Maven - Build Project') {
      steps {
        sh 'mvn clean && mvn install'
      }
    }

    stage('Docker - Login') {
      environment {
        DOCKER_USER = 'abdullahsayed'
        DOCKER_PASSWORD = 'html123CSS'
      }
      steps {
        sh 'sudo docker login -u $DOCKER_USER -p $DOCKER_PASSWORD'
      }
    }

    stage('Docker - Compose Down & Up') {
      steps {
        sh 'sudo docker-compose down && sudo docker-compose up --build'
      }
    }


  }
  environment {
    USER = 'jenkins'
  }
}