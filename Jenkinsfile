pipeline {
    agent any

    tools {
        // IMPORTANT: Ensure these names match EXACTLY what you have configured
        // in Manage Jenkins -> Global Tool Configuration.
        // For example, if your NodeJS tool is named "MyNode", change 'NodeJS' to 'MyNode'.
        nodejs 'NodeJS' 
        maven 'Maven'
    }

    environment {
        // The ID for your Tomcat credentials stored in Jenkins
        TOMCAT_CREDS = credentials('tomcat-credentials') 
    }

    stages {
        stage('Checkout') {
            steps {
                // Clones your project from the specified Git repository
                git branch: 'main', url: 'https://github.com/Hemanth1845/CRM-PROJECT.git'
            }
        }

        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    sh 'npm install'
                    // Set the API URL to the correct relative path for Tomcat
                    sh 'npm run build -- --define process.env.VITE_APP_API_URL="\'/crmproject/api\'"'
                }
            }
        }

        stage('Copy Frontend to Backend') {
            steps {
                // FIXED: Corrected syntax for deleteDir
                // This step cleans the static resources directory before copying new files
                deleteDir(dir: 'backend/CRMApplication/src/main/resources/static')
                
                // Copy the built frontend files to the Spring Boot static resources folder
                sh 'mkdir -p backend/CRMApplication/src/main/resources/static'
                sh 'cp -r frontend/dist/* backend/CRMApplication/src/main/resources/static/'
            }
        }

        stage('Build Backend (WAR)') {
            steps {
                dir('backend/CRMApplication') {
                    // Package the backend application into a WAR file
                    sh 'mvn clean package'
                }
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                // Deploy the specifically named WAR file to your Tomcat server
                // IMPORTANT: Replace 'your-tomcat-ip' with the actual IP address of your Tomcat server
                deploy adapters: [tomcat9(credentialsId: 'tomcat-credentials', path: '/crmproject', url: 'http://your-tomcat-ip:2030')],
                       contextPath: 'crmproject', 
                       war: 'backend/CRMApplication/target/crmproject.war'
            }
        }
    }

    post {
        always {
            // Clean up the Jenkins workspace after the build is complete
            cleanWs()
        }
    }
}

