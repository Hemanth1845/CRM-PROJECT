pipeline {
    agent any

    tools {
        // IMPORTANT: These names MUST match your Jenkins Global Tool Configuration
        nodejs 'NodeJS' // e.g., 'NodeJS-20', 'Default-Node'
        maven 'Maven'   // e.g., 'Maven-3.9', 'Default-Maven'
    }

    environment {
        // The ID of the Tomcat credentials stored in Jenkins
        TOMCAT_CREDS = 'tomcat-credentials' 
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code from GitHub...'
                // Updated with your repository URL
                git url: 'https://github.com/Hemanth1845/CRM-PROJECT.git', branch: 'main'
            }
        }

        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    echo 'Installing frontend dependencies...'
                    sh 'npm install'
                    
                    echo 'Building frontend for production...'
                    // The API URL is set to a relative path because both frontend and backend
                    // will be served from the same Tomcat instance.
                    sh 'npm run build -- --base=./ --define:import.meta.env.VITE_APP_API_URL="\'/crmproject/api\'"'
                }
            }
        }

        stage('Prepare Backend') {
            steps {
                echo 'Cleaning old frontend build from backend...'
                // This step ensures no old frontend files are left over
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
                // Jenkins will look for the WAR file with the name 'crmproject.war'
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

