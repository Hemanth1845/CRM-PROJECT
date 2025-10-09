pipeline {
    agent any

    environment {
        MAVEN_HOME = 'C:\\Users\\heman\\OneDrive\\Pictures\\Documents\\software solutions\\apache-maven-3.9.11-bin\\apache-maven-3.9.11'
        TOMCAT_PATH = 'C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1'
    }

    stages {

        // ===== FRONTEND BUILD =====
        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    bat 'npm install'
                    bat 'npm run build'
                }
            }
        }

        // ===== FRONTEND DEPLOY =====
        stage('Deploy Frontend to Tomcat') {
            steps {
                bat '''
                    echo Deploying Frontend to Tomcat...
                    if exist "%TOMCAT_PATH%\\webapps\\crm-frontend" (
                        rmdir /S /Q "%TOMCAT_PATH%\\webapps\\crm-frontend"
                    )
                    mkdir "%TOMCAT_PATH%\\webapps\\crm-frontend"
                    xcopy /E /I /Y frontend\\dist\\* "%TOMCAT_PATH%\\webapps\\crm-frontend"
                '''
            }
        }

        // ===== BACKEND BUILD =====
        stage('Build Backend') {
            steps {
                dir('backend/CRMApplication') {
                    bat '"%MAVEN_HOME%\\bin\\mvn" clean package -DskipTests'
                }
            }
        }

        // ===== BACKEND DEPLOY =====
        stage('Deploy Backend to Tomcat') {
            steps {
                bat '''
                    echo Deploying Backend to Tomcat...
                    if exist "%TOMCAT_PATH%\\webapps\\crm-backend.war" (
                        del "%TOMCAT_PATH%\\webapps\\crm-backend.war"
                    )
                    copy backend\\CRMApplication\\target\\*.war "%TOMCAT_PATH%\\webapps\\crm-backend.war"
                '''
            }
        }
    }

    post {
        success {
            echo "✅ CRM Project pipeline completed successfully."
        }
        failure {
            echo "❌ CRM Project pipeline failed."
        }
    }
}
