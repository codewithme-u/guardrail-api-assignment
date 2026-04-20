FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]