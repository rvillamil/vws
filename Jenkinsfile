#!groovy

/*
  Links:
  - https://jenkins.io/blog/2017/02/07/declarative-maven-project/
 -  http://dmunozfer.es/tutorial-jenkins-2-configuracion-pipeline/
*/

node {
    checkout scm

    String jdktool = tool name: "JDK_8", type: 'hudson.model.JDK'
    def mvnHome    = tool name: 'Maven_3'

    /* Set JAVA_HOME, and special PATH variables. */
    List javaEnv = [
        "PATH+MVN=${jdktool}/bin:${mvnHome}/bin",
        "M2_HOME=${mvnHome}",
        "JAVA_HOME=${jdktool}"
    ]

    withEnv(javaEnv) {
      
      stage ('Initialize') {
	sh '''
            echo "PATH = ${PATH}"
            echo "M2_HOME = ${M2_HOME}"
        '''
      } // End initialize

      stage ('Build') {
        try {
	  sh 'mvn -Dmaven.test.failure.ignore=true install -Ddocker.host=tcp://localhost:2375'
        } catch (e) {
	  currentBuild.result = 'FAILURE'
        }
      } // End Build

      
      stage ('Post') {
        if (currentBuild.result == null || currentBuild.result == 'SUCCESS') {
	  junit 'target/surefire-reports/**/*.xml'  
        }
      } // End post
      
    } // End With(javaEnv)
    
} // End node


   
