# --- Stage 1: Build the application JAR ---
FROM maven:3.8-openjdk-17 AS build
WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY src ./src

# Build the project and skip tests
RUN mvn clean package -DskipTests

# --- Stage 2: Create the runtime image ---
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the built JAR dynamically (use wildcard)
COPY --from=build /app/target/*.jar ./app.jar

# Expose the application port
EXPOSE 8080

# Command to run the app
CMD ["java", "-jar", "app.jar"]
