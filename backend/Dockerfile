# Use JRE image for running
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy the pre-built backend JAR from Jenkins workspace
COPY target/backend.jar ./backend.jar

EXPOSE 8080
CMD ["java", "-jar", "backend.jar"]