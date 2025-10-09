pipeline {
    agent any

    environment {
        // Set paths as per your local setup
        MAVEN_HOME = "C:\\Program Files\\Apache\\maven-3.9.6"
        JAVA_HOME = "C:\\Program Files\\Java\\jdk-21"
        TOMCAT_HOME = "C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1"
        WAR_NAME = "CRMApplication.war"
    }

    stages {

        stage('Checkout Code') {
            steps {
                echo 'Cloning repository...'
                git branch: 'main', url: 'https://github.com/Hemanth1845/CRM-PROJECT.git'
            }
        }

        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    echo 'Installing frontend dependencies...'
                    bat 'npm install'
                    echo 'Building frontend...'
                    bat 'npm run build'
                }
            }
        }

        stage('Build Backend') {
            steps {
                dir('backend') {
                    echo 'Building Spring Boot project using Maven...'
                    bat "\"${MAVEN_HOME}\\bin\\mvn.cmd\" clean package -DskipTests"
                }
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                echo 'Deploying WAR file to Tomcat...'

                // Delete old WAR and folder
                bat """
                del /Q "${TOMCAT_HOME}\\webapps\\${WAR_NAME}"
                rmdir /S /Q "${TOMCAT_HOME}\\webapps\\CRMApplication"
                """

                // Copy new WAR
                bat "copy backend\\target\\${WAR_NAME} \"${TOMCAT_HOME}\\webapps\\${WAR_NAME}\""

                // Restart Tomcat service
                echo 'Restarting Tomcat...'
                bat """
                net stop Tomcat10
                net start Tomcat10
                """
            }
        }
    }

    post {
        success {
            echo '✅ Deployment successful! CRM Application is now live on Tomcat.'
        }
        failure {
            echo '❌ Deployment failed! Check Jenkins logs for details.'
        }
    }
}
