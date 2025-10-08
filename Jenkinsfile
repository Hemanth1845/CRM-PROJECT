pipeline {
    agent any

    tools {
        // The name 'NodeJS-20' must match the name you configured in Jenkins -> Tools
        nodejs 'NodeJS-20' 
        // Assumes you have a Maven tool configured in Jenkins
        maven 'Maven-3.9'
    }

    environment {
        // Credentials ID for your Tomcat server from Jenkins Credentials
        TOMCAT_CREDS = credentials('tomcat-credentials') 
    }

    stages {
        stage('Checkout') {
            steps {
                // Clones your project from your specific Git repository
                git branch: 'main', url: 'https://github.com/Hemanth1845/CRM-PROJECT.git'
            }
        }

        stage('Build Frontend') {
            steps {
                // Navigate to the frontend directory
                dir('frontend') {
                    sh 'npm install'
                    // VITE_APP_API_URL should point to where the backend will be deployed
                    sh 'npm run build -- --base=./ --define process.env.VITE_APP_API_URL="\'/api\'"'
                }
            }
        }

        stage('Copy Frontend to Backend') {
            steps {
                // Clean any old frontend files from the backend's static resources
                deleteDir content: '''backend/CRMApplication/src/main/resources/static/''', followSymlinks: false
                // Copy the newly built frontend into the backend's static folder
                sh 'cp -r frontend/dist/* backend/CRMApplication/src/main/resources/static/'
            }
        }

        stage('Build Backend (WAR)') {
            steps {
                // Navigate to the backend directory and package it as a WAR file
                dir('backend/CRMApplication') {
                    sh 'mvn clean package'
                }
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                // Deploy the generated WAR file to Tomcat
                // IMPORTANT: Replace 'your-tomcat-ip' with the actual IP address of your Tomcat server
                deploy adapters: [tomcat9(credentialsId: 'tomcat-credentials', path: '/crm', url: 'http://your-tomcat-ip:8080')],
                       contextPath: 'crm-app', 
                       war: 'backend/CRMApplication/target/*.war'
            }
        }
    }

    post {
        always {
            // Clean up the workspace after the build
            cleanWs()
        }
    }
}

