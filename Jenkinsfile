podTemplate(
  label: "maven-pod",
  cloud: "openshift",
  inheritFrom: "maven",
  containers: [
    containerTemplate(
      name: "jnlp",
      image: "docker-registry.default.svc:5000/ci/jenkins-agent-graalvm:latest",
      resourceRequestMemory: "4Gi",
      resourceLimitMemory: "4Gi",
      resourceRequestCpu: "100m",
      resourceLimitCpu: "2"
    )
  ],
  volumes: [
    secretVolume(
      mountPath: "/home/jenkins/mvn-settings",
      secretName: "maven"
    )
  ]
) {
	node('maven-pod') {
	  // Define Maven Command to point to the correct
	  // settings for our Nexus installation
	  def mvnCmd = "mvn -Dsettings.security=/home/jenkins/mvn-settings/settings-security.xml -s /home/jenkins/mvn-settings/settings.xml"
	  def pom
	
	  // Checkout Source Code.
	  stage('Checkout Source') {
	    checkout scm
	  }
	  
	  stage('Get project version') {
	    pom = readMavenPom file: 'pom.xml'
	  }
		
	
	  // Using Maven build the jar files
	  // Do not run tests in this step
	  stage('Build jar') {
	    echo "Building version ${pom.version}"
	    sh "${mvnCmd} clean package install -DskipTests"
	  }
	
	  // Using Maven run the unit tests
	  stage('Unit Tests') {
	    echo "Running Unit Tests"
	    sh "${mvnCmd} test"
	  }
	  
	  // // change directory and run integration tests
	  // stage('Integration Tests') {
	  //	dir('integration-tests') {
	  //		sh "${mvnCmd} verify -Pnative-image"
	  //	}
	  // }
	
	  // Using Maven to call SonarQube for Code Analysis
	  stage('Code Analysis') {
	    echo "Running Code Analysis"
	    sh "${mvnCmd} sonar:sonar -Dsonar.host.url=https://sonarqube-ci.apps.idsysapps.com -Dsonar.projectName=quarkus-orika"
	  }
	
	  // Publish the built war file to Nexus
	  stage('Publish to Nexus') {
	    echo "Publish to Nexus"
	    sh "${mvnCmd} deploy -DskipTests=true -DuniqueVersion=false"
	  }
	}
}
