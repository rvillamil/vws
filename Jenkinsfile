/*

 About declarative Pipeline (DSL) syntax: https://jenkins.io/doc/book/pipeline/syntax/
 About DSL and maven project: https://jenkins.io/blog/2017/02/07/declarative-maven-project/

*/
pipeline {

    agent any

    tools { 
        maven 'Maven_3' 
        jdk 'JDK_8'
	/*
	SonarQube_Scanner_3
	Docker_latest
	*/
    }

    /*
    Stages section: A sequence of one or more stage directives
    */
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
                sh 'mvn -Dmaven.test.failure.ignore=true install' 
            }
            post {
                success {
                    junit 'target/surefire-reports/**/*.xml' 
                }
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