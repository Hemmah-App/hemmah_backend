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

  }
}