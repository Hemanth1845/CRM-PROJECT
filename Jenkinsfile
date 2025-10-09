pipeline {
    agent any

    tools {
        // IMPORTANT: Replace these names with your actual tool names from 
        // Jenkins -> Manage Jenkins -> Tools
        nodejs 'YOUR_NODEJS_TOOL_NAME' // e.g., 'NodeJS-20', 'Default-Node'
        maven 'YOUR_MAVEN_TOOL_NAME'   // e.g., 'Maven-3.9', 'Default-Maven'
    }

    environment {
        // The ID of the Tomcat credentials stored in Jenkins
        TOMCAT_CREDS = 'tomcat-credentials' 
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code from GitHub...'
                git url: 'https://github.com/Hemanth1845/CRM-PROJECT.git', branch: 'main'
            }
        }

        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    echo 'Installing frontend dependencies...'
                    sh 'npm install'
                    
                    echo 'Building frontend for production...'
                    // This sets the API URL to a relative path for deployment
                    sh 'npm run build -- --base=./ --define:import.meta.env.VITE_APP_API_URL="\'/crmproject/api\'"'
                }
            }
        }

        stage('Prepare Backend') {
            steps {
                echo 'Cleaning old frontend build from backend...'
                dir('backend/CRMApplication/src/main/resources/static') {
                    deleteDir()
                }

                echo 'Copying new frontend build to backend...'
                sh 'cp -R frontend/dist/* backend/CRMApplication/src/main/resources/static/'
            }
        }

        stage('Build Backend WAR') {
            steps {
                dir('backend/CRMApplication') {
                    echo 'Building Spring Boot WAR file...'
                    sh 'mvn clean package'
                }
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                echo 'Deploying WAR to Tomcat...'
                deploy adapters: [
                    tomcat9(
                        credentialsId: TOMCAT_CREDS, 
                        // IMPORTANT: Replace with your Tomcat server's URL
                        url: 'http://your-tomcat-ip:8080', 
                        path: '/crmproject'
                    )
                ], 
                war: 'backend/CRMApplication/target/crmproject.war'
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished.'
        }
        success {
            echo 'Deployment successful!'
        }
        failure {
            echo 'Deployment failed. Please check the logs.'
        }
    }
}

