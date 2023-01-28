pipeline {
  agent any
  stages {


    stage('Checkout Code') {
      steps {
        git(url: 'https://github.com/Hemmah-App/hemmah_backend.git', branch: 'master')
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
        sh 'sudo docker-compose down && sudo docker-compose up -d --build'
      }
    }

//     stage('Docker - Build And Push') {
//       steps {
//         sh 'sudo docker build -t abdullahsayed/hemmah_backend:latest . && sudo docker push abdullahsayed/hemmah_backend:latest'
//       }
//     }


  }
  environment {
    USER = 'jenkins'
  }
}