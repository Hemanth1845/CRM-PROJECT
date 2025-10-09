pipeline {
    agent any

    environment {
        // IMPORTANT: Update this with the actual path to your Maven installation
        MAVEN_HOME = 'C:\\Users\\heman\\OneDrive\\Pictures\\Documents\\software solutions\\apache-maven-3.9.11-bin\\apache-maven-3.9.11'
        TOMCAT_PATH = 'C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1'
    }

    stages {

        // ===== FRONTEND BUILD =====
        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    echo 'Installing frontend dependencies...'
                    bat 'npm install'
                    echo 'Building frontend for production...'
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
                    echo 'Building Spring Boot WAR file...'
                    bat '"%MAVEN_HOME%\\bin\\mvn" clean package -DskipTests'
                }
            }
        }

        // ===== BACKEND DEPLOY =====
        stage('Deploy Backend to Tomcat') {
            steps {
                bat '''
                    echo Deploying Backend to Tomcat...
                    rem CORRECTED FILENAME: Now looks for crmproject.war
                    if exist "%TOMCAT_PATH%\\webapps\\crmproject.war" (
                        del "%TOMCAT_PATH%\\webapps\\crmproject.war"
                    )
                    rem CORRECTED FILENAME: Now copies the correct crmproject.war
                    copy backend\\CRMApplication\\target\\crmproject.war "%TOMCAT_PATH%\\webapps\\crmproject.war"
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

