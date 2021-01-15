pipeline {
    agent { docker { image 'maven:3.6.3' } }

    stages {
        stage('build') {
            steps {
                 sh 'mvn -B clean package -Dmaven.test.skip=true'
            }
        }
        stage('Software Composition Analysis (SCA)') {
            steps {
                 sh 'echo "**** CycloneDX ****"'
                 sh 'mvn -B org.cyclonedx:cyclonedx-maven-plugin:makeAggregateBom'
            }
        }
        // dependencyTrackPublisher artifact: 'devsecops-java-sandbox', autoCreateProjects: false, dependencyTrackApiKey: '', dependencyTrackUrl: '', projectId: '2d6fc921-8dd7-4c3d-9279-eb747a1fb6e1', synchronous: false
        stage('Dependency Track Publisher') {
            steps {
                withCredentials([string(credentialsId: 'mydependencytrack-api-key', variable: 'API_KEY')]) {
                    dependencyTrackPublisher dependencyTrackUrl: 'http://mydependencytrack-devsecops-rvp-local:8081', artifact: 'target/bom.xml', projectName: "vws", projectVersion: "0.0.2-SNAPSHOT", synchronous: true, dependencyTrackApiKey: API_KEY
                }
            }
        }
    }
}