# Stage 1: Build the application
FROM gradle:8.8-jdk17 AS builder

# Set the working directory
WORKDIR /app

# Copy the Gradle build files
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src ./src

# Build the application
RUN gradle build --no-daemon

# Stage 2: Package the application
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=builder /app/build/libs/foreign-exchange-application-v1.jar /app/foreign-exchange-application.jar

# Specify the command to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/foreign-exchange-application.jar"]
