# DAT250, Experiment Assignment 7

I containerized the Spring Boot backend by following the steps provided and following the same approach as in the lecture example. The build happens in a temporary “builder” image using Gradle, and the final application runs in a slim Java runtime image as a non-root user. 

I created aa Dockerfile in the backend flder.
and ussed a multi-stage build:
* Stage 1: a Gradle image builds the application with the bootJar task.
* Stage 2: a slim runtime image copies the built JAR and runs it with java -jar.

The container runs as a non-root user by creating a dedicated app user and switching to it before starting the app.
I used a wildcard when renaming the built JAR to app.jar, so the Dockerfile does not depend on the exact artifact name.
Added a minimal .dockerignore in backend/ to keep the build context small.

How to build and run (from the backend/ directory)
Build: docker build -t pollapp-backend .
Run: docker run --rm -p 8080:8080 pollapp-backend

The API is available at http://localhost:8080 while the container is running. The image is small and the app runs as a non-root user inside the container.