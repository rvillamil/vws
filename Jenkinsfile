#!groovy

/*
TODO: Quizas la mejor opcion sea dejar para el perfildesarrollo, la generacion de imagenes con maven. Para Jenkins, crear un perfil de integracion y que se ejecuten con docker normal
 
 Useful links:
  - https://jenkins.io/blog/2017/02/07/declarative-maven-project/
  - http://dmunozfer.es/tutorial-jenkins-2-configuracion-pipeline/
  - https://jenkins.io/doc/pipeline/examples/
*/

node {
    checkout scm

    // Tools y Jenkins 'configuracion tool' section
    String jdktool = tool name: "JDK_8", type: 'hudson.model.JDK'
    def mvnHome    = tool name: 'Maven_3'
    def dockerHome = tool name: 'Docker_latest'

    /* Set JAVA_HOME, and special PATH variables: docker, java, maven */
    List javaEnv = [
        "PATH+MVN=${jdktool}/bin:${mvnHome}/bin:${dockerHome}/bin",
        "M2_HOME=${mvnHome}",
        "JAVA_HOME=${jdktool}",
	"DOCKER_HOME=${dockerHome}"
    ]

    
    withEnv(javaEnv) {

      stage ('Initialize') {
	sh '''
            echo "PATH = ${PATH}"
            echo "M2_HOME = ${M2_HOME}"
            echo "DOCKER_HOME=${DOCKER_HOME}"
            echo "DOCKER_HOST=${DOCKER_HOST}"
        '''
	// TODO: Borramos el workspace??
	//sh 'rm -rf *'
      } 

      stage('Checkout SCM') {
	// Checkout code from repository
	checkout scm
      }
  
      stage ('Build') {
        try {
	  // Compila, genera el directorio target y no lanza los test
	  sh 'mvn clean compile'
        } catch (e) {
	  currentBuild.result = 'FAILURE'
        }
      } // End Build

      
      stage ('Test') {
	echo 'Ejecutando tests unitarios y de integracion'
	try{
	  // Lanza test unitarios, empaqueta y lanza los de integracion
	  sh 'mvn verify -DOCKER_HOST=/var/run/docker.sock'
	  // Archiva los resultados de las pruebas realizadas con el plugin
	  // surefire de Maven para poder ser visualizados desde la interfaz web de Jenkins
	  step([$class: 'JUnitResultArchiver',
		testResults: '**/target/surefire-reports/TEST-*.xml'])
	}
	catch (err) {
	  // En caso de error tambien archivamos los test, para su visualizacion
	  step([$class: 'JUnitResultArchiver',
		testResults: '**/target/surefire-reports/TEST-*.xml'])
	
	  if (currentBuild.result == 'UNSTABLE')
	    currentBuild.result = 'FAILURE'
	    throw err
	}
      } // End test 
      
      stage ('Archive') {
        if (currentBuild.result == null || currentBuild.result == 'SUCCESS') {
	  echo 'Archiva el paquete el paquete generado en Jenkins'
	  // Archiva los ficheros jar generados por Maven para que esten
	  // disponibles desde la interfaz web de Jenkins.
	  step([$class: 'ArtifactArchiver',
		artifacts: '**/target/*.jar, **/target/*.war', fingerprint: true])
	}
      }

      /* TODO: Cuando y como borramos el workspace
      stage ("Cleanup"){
	deleteDir()
      }
      */
    } // End With(javaEnv)
    
} // End node


   
