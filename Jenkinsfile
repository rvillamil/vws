/*

 About declarative Pipeline (DSL) syntax: https://jenkins.io/doc/book/pipeline/syntax/
 About DSL and maven project: https://jenkins.io/blog/2017/02/07/declarative-maven-project/

*/
pipeline {

    agent any

    tools { 
        maven 'Maven_3' 
        jdk 'JDK_8'
	sonarscanner 'SonarQube_Scanner_3'

	/*
	SonarQube Server : No se como se declara aqui 
	SonarQube_Scanner_3 : No se como se declara aqui. La declaro mas abajo con groovy
	Docker_latest
	*/
    }

    
    // Stages section: A sequence of one or more stage directives
    stages {

    	stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
		    echo "DOCKER:" $(docker --version)
                ''' 
            }
        }
	
        stage('Build') {
            steps {
	    	echo "IGNORANDO los TEST"
                sh 'mvn install -Dmaven.test.skip=true' 
            }
            post {
                success {
                    junit 'target/surefire-reports/**/*.xml' 
                }
            }
        }

	// requires SonarQube Scanner 2.8+
	stage('SonarQube analysis') {
	    steps {
    	        def scannerHome = tool 'SonarQube_Scanner_3'
    	  	withSonarQubeEnv('SonarQube Server') {
      	        sh "${scannerHome}/bin/sonar-scanner"
    	    }
  	}
   
    }
    
    /*
      The post section defines actions which will be run at the end of the Pipeline run
    */
    post { 
        always { 
            echo 'End of pipeline..'
        }
    }
}