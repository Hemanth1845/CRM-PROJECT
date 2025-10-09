pipeline {
    agent any

    // No 'tools' section is needed if Maven and Node are in the system's PATH on the Jenkins server

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
                    bat 'npm install'
                    
                    echo 'Building frontend for production...'
                    // The API URL is relative because frontend and backend will be in the same WAR
                    bat 'npm run build -- --base ./'
                }
            }
        }
        
        // ===== PREPARE UNIFIED BUILD =====
        stage('Combine Frontend and Backend') {
            steps {
                echo 'Cleaning old frontend files from backend...'
                bat '''
                    if exist "backend\\CRMApplication\\src\\main\\resources\\static" (
                        rmdir /S /Q "backend\\CRMApplication\\src\\main\\resources\\static"
                    )
                    mkdir "backend\\CRMApplication\\src\\main\\resources\\static"
                '''

                echo 'Copying new frontend build to backend...'
                bat 'xcopy /E /I /Y "frontend\\dist\\*" "backend\\CRMApplication\\src\\main\\resources\\static\\"'
            }
        }
        
        // ===== BACKEND BUILD (WAR PACKAGING) =====
        stage('Build Backend WAR') {
            steps {
                dir('backend/CRMApplication') {
                    echo 'Building unified WAR file...'
                    bat 'mvn clean package -DskipTests'
                }
            }
        }
        
        // ===== DEPLOY UNIFIED WAR =====
        stage('Deploy to Tomcat') {
            steps {
                echo 'Deploying unified WAR to Tomcat...'
                bat '''
                    set "TOMCAT_WEBAPPS=C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1\\webapps"
                    set "WAR_FILE=backend\\CRMApplication\\target\\CRMApplication.war"
                    
                    echo "Removing old deployments..."
                    if exist "%TOMCAT_WEBAPPS%\\CRMApplication.war" (
                        del "%TOMCAT_WEBAPPS%\\CRMApplication.war"
                    )
                    if exist "%TOMCAT_WEBAPPS%\\CRMApplication" (
                        rmdir /S /Q "%TOMCAT_WEBAPPS%\\CRMApplication"
                    )
                     if exist "%TOMCAT_WEBAPPS%\\crm-project" (
                        rmdir /S /Q "%TOMCAT_WEBAPPS%\\crm-project"
                    )
                    
                    echo "Copying new unified WAR file..."
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

