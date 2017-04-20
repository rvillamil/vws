#!groovy

/* 
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
	checkout scm
      }

      // FIXME: Mientras arreglamos los test de integracion y el soporte para Docker...
      // Compilamos y lanzamos los test aunque fallen
      stage ('BuildAndTest') {
       	  sh 'mvn install -P develop,-docker-support -Dmaven.test.failure.ignore=true'
      }

      stage ('Archive') {
	// Archivamos los artefactos en Jenkins: Si no hay nada que archivar
	// no va a fallar 
	archiveArtifacts allowEmptyArchive: true,
			 artifacts: '**/target/*.jar', 
			 fingerprint: true,
			 onlyIfSuccessful: true
	// Publica los resultados de los test de Junit en Jenkins --> Probar
	// Si no hay test, no va a fallar
	junit allowEmptyResults: true,
	      testResults: '**/target/surefire-reports/TEST-*.xml'
	    // Publica los resultados de los test de Jacoco en Jenkins --> Probar
	    //step( [ $class: 'JacocoPublisher' ] )
      }


       // Archiva los resultados de las pruebas realizadas con el plugin
      // surefire de Maven para poder ser visualizados desde la interfaz web de Jenkins
      //	  step([$class: 'JUnitResultArchiver',
      //		testResults: '**/target/surefire-reports/TEST-*.xml'])

      // Archiva los ficheros jar generados por Maven para que esten
      // disponibles desde la interfaz web de Jenkins.
      //step([$class: 'ArtifactArchiver',
      //		artifacts: '**/target/*.jar, **/target/*.war', fingerprint: true])

      /* TODO: Cuando y como borramos el workspace
      stage ("Cleanup"){
	deleteDir()
      }
      */
    } // End With(javaEnv)
    
} // End node


   
