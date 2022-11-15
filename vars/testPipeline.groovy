import com.jenkins.*;

def call(body) {
    LinkedHashMap config = [:]
    
    def mvnBuild = new MavenBuild()    
    
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    
    pipeline {
      agent { label 'Demo' }
      stages {
          stage('Compile/Test') {
              steps {
                  script {
                      mvnBuild.cleanInstall(this)
                      echo "Hello World"
                  }
              }
          }
          
          
          
     }
     post {
         always {
             sh 'printenv'
             emailext body: '', recipientProviders: [developers()], subject: 'Build executed ', to: 'thomas@mosig-frey.de'
         }
         success {
              cleanWs()
         }
     }
  }
}
