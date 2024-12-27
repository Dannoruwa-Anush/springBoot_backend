pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'dannoruwaanush/netcentric-spring-boot-app'  //dockerusername/image-name
        DOCKER_REGISTRY = 'docker.io'
    }

    stages {
        stage('Clone Repository') {
            steps {
                // Clone the repository containing the Spring Boot app and specify the main branch
                git branch: 'main', url: 'https://github.com/Dannoruwa-Anush/springBoot_backend.git'
            }
        }

		stage('Build JAR') {
            steps {
                script {
                    // Build the Spring Boot JAR using Maven
                    bat './mvnw clean package -DskipTests -U' // Add -U to force update of dependencies
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image for the React app using the Dockerfile
                    // Using 'bat' instead of 'sh' for Windows systems
                    bat "docker build -t ${DOCKER_IMAGE} ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    // Log in to Docker registry (you need credentials set up in Jenkins)
                    withCredentials([usernamePassword(credentialsId: 'dannoruwaanush-dockerhub', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        bat "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin"

                        // Push the Docker image to a Docker Hub
                        bat "docker tag $DOCKER_IMAGE $DOCKER_REGISTRY/$DOCKER_IMAGE"
                        bat "docker push $DOCKER_REGISTRY/$DOCKER_IMAGE"
                    }
                }
            }
        }
    }
    post {
        /*
        actions that should be performed after the stages are complete, 
        regardless of whether the pipeline execution was successful or failed.
        */
        always {
            // Clean up unused Docker resources such as Stopped containers, Unused networks, Dangling images (images not associated with any container), Build cache
            bat 'docker system prune -f'

            bat 'docker logout'
        }
    }
}
