pipeline {
    agent any

    tools {
        // IMPORTANT: Replace these with your tool names from Jenkins -> Manage Jenkins -> Tools
        
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
                    // Use 'bat' for Windows
                    bat 'npm install'
                    
                    echo 'Building frontend for production...'
                    // Use 'bat' and adjust the command for Windows
                    bat 'npm run build -- --define:import.meta.env.VITE_APP_API_URL="\\"/crmproject/api\\""'
                }
            }
        }
        
        // ===== FRONTEND DEPLOY =====
        stage('Deploy Frontend to Tomcat') {
            steps {
                echo 'Deploying frontend to Tomcat ROOT...'
                // Use 'bat' for Windows commands and paths
                bat '''
                    set "TOMCAT_WEBAPPS=C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1\\webapps"
                    
                    echo "Cleaning Tomcat ROOT directory..."
                    if exist "%TOMCAT_WEBAPPS%\\ROOT" (
                        rmdir /S /Q "%TOMCAT_WEBAPPS%\\ROOT"
                        mkdir "%TOMCAT_WEBAPPS%\\ROOT"
                    )
                    
                    echo "Copying frontend build to ROOT..."
                    xcopy /E /I /Y "frontend\\dist\\*" "%TOMCAT_WEBAPPS%\\ROOT\\"
                '''
            }
        }
        
        // ===== BACKEND BUILD =====
        stage('Build Backend') {
            steps {
                dir('backend/CRMApplication') {
                    echo 'Building backend WAR file...'
                    // Use 'bat' for Windows
                    bat 'mvn clean package -DskipTests'
                }
            }
        }
        
        // ===== BACKEND DEPLOY =====
        stage('Deploy Backend to Tomcat') {
            steps {
                echo 'Deploying backend WAR file to Tomcat...'
                // Use 'bat' for Windows commands and paths
                bat '''
                    set "TOMCAT_WEBAPPS=C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1\\webapps"
                    set "WAR_FILE=backend\\CRMApplication\\target\\crmproject.war"
                    
                    echo "Removing old backend deployment..."
                    if exist "%TOMCAT_WEBAPPS%\\crmproject.war" (
                        del "%TOMCAT_WEBAPPS%\\crmproject.war"
                    )
                    if exist "%TOMCAT_WEBAPPS%\\crmproject" (
                        rmdir /S /Q "%TOMCAT_WEBAPPS%\\crmproject"
                    )
                    
                    echo "Copying new backend WAR file..."
                    copy "%WAR_FILE%" "%TOMCAT_WEBAPPS%\\"
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

