pipeline {
    agent any

    tools {
        // IMPORTANT: Replace these with the names from your Jenkins Global Tool Configuration
        nodejs 'NodeJS' 
        maven 'Maven'
    }

    stages {
        // ===== CHECKOUT =====
        stage('Checkout') {
            steps {
                echo 'Checking out source code from GitHub...'
                git url: 'https://github.com/Hemanth1845/CRM-PROJECT.git', branch: 'main'
            }
        }

        // ===== FRONTEND BUILD =====
        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    echo 'Installing frontend dependencies...'
                    sh 'npm install'
                    
                    echo 'Building frontend for production...'
                    // This sets the API URL to point to the backend's context path on Tomcat
                    sh 'npm run build -- --define:import.meta.env.VITE_APP_API_URL="\'/crmproject/api\'"'
                }
            }
        }
        
        // ===== FRONTEND DEPLOY =====
        stage('Deploy Frontend to Tomcat') {
            steps {
                echo 'Deploying frontend to Tomcat ROOT...'
                // NOTE: This uses 'sh' for Linux-based Jenkins agents. 
                // If your agent is on Windows, replace 'sh' with 'bat' and update paths.
                sh '''
                    TOMCAT_WEBAPPS="/path/to/your/tomcat/webapps"
                    
                    echo "Cleaning Tomcat ROOT directory..."
                    rm -rf $TOMCAT_WEBAPPS/ROOT/*
                    
                    echo "Copying frontend build to ROOT..."
                    cp -R frontend/dist/* $TOMCAT_WEBAPPS/ROOT/
                '''
            }
        }
        
        // ===== BACKEND BUILD =====
        stage('Build Backend') {
            steps {
                dir('backend/CRMApplication') {
                    echo 'Building backend WAR file...'
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        
        // ===== BACKEND DEPLOY =====
        stage('Deploy Backend to Tomcat') {
            steps {
                echo 'Deploying backend WAR file to Tomcat...'
                sh '''
                    TOMCAT_WEBAPPS="/path/to/your/tomcat/webapps"
                    WAR_FILE="backend/CRMApplication/target/crmproject.war"
                    
                    echo "Removing old backend deployment..."
                    rm -f $TOMCAT_WEBAPPS/crmproject.war
                    rm -rf $TOMCAT_WEBAPPS/crmproject
                    
                    echo "Copying new backend WAR file..."
                    cp $WAR_FILE $TOMCAT_WEBAPPS/
                '''
            }
        }
    }

    post {
        success {
            echo "Pipeline completed successfully."
        }
        failure {
            echo "Pipeline failed. Please check the logs."
        }
    }
}

