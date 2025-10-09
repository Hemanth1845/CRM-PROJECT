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
                    // This command injects the correct relative API path for the backend
                    // It assumes both frontend and backend are on the same Tomcat server
                    bat 'npm run build -- --define "import.meta.env.VITE_APP_API_URL=\\"/CRMApplication/api\\""'
                }
            }
        }
        
        // ===== FRONTEND DEPLOY =====
        stage('Deploy Frontend to Tomcat') {
            steps {
                echo 'Deploying frontend to Tomcat...'
                bat '''
                    set "TOMCAT_WEBAPPS=C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1\\webapps"

                    echo "Cleaning old frontend deployment..."
                    if exist "%TOMCAT_WEBAPPS%\\crm-project" (
                        rmdir /S /Q "%TOMCAT_WEBAPPS%\\crm-project"
                    )

                    echo "Creating new frontend directory..."
                    mkdir "%TOMCAT_WEBAPPS%\\crm-project"
                    
                    echo "Copying frontend build files..."
                    xcopy /E /I /Y "frontend\\dist\\*" "%TOMCAT_WEBAPPS%\\crm-project\\"
                '''
            }
        }
        
        // ===== BACKEND BUILD =====
        stage('Build Backend') {
            steps {
                dir('backend/CRMApplication') {
                    echo 'Building backend WAR file...'
                    bat 'mvn clean package -DskipTests'
                }
            }
        }
        
        // ===== BACKEND DEPLOY =====
        stage('Deploy Backend to Tomcat') {
            steps {
                echo 'Deploying backend WAR file to Tomcat...'
                bat '''
                    set "TOMCAT_WEBAPPS=C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1\\webapps"
                    set "WAR_FILE=backend\\CRMApplication\\target\\CRMApplication.war"
                    
                    echo "Removing old backend deployment..."
                    if exist "%TOMCAT_WEBAPPS%\\CRMApplication.war" (
                        del "%TOMCAT_WEBAPPS%\\CRMApplication.war"
                    )
                    if exist "%TOMCAT_WEBAPPS%\\CRMApplication" (
                        rmdir /S /Q "%TOMCAT_WEBAPPS%\\CRMApplication"
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

