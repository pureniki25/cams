pipeline {
  agent any
  stages {
    stage('sonar') {
      steps {
       sh 'mvn clean package sonar:sonar -Dsonar.host.url=http://172.16.200.111:9000'
        //tool(type: 'sonarqube scanner', name: 'sonarScanner1')
      }
    }
  }
  tools {
    maven 'M3'
  }
}
