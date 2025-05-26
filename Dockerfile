# Use a base JDK image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built jar into the container
COPY target/easyhire-onboarding-0.0.1-SNAPSHOT.jar app.jar

# Expose port if your app runs on it (e.g., 8080)
EXPOSE 8080

# Command to run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]