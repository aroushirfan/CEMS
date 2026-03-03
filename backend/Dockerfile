# Use Maven image to build
FROM maven:3.9.6-amazoncorretto-21 AS builder
WORKDIR /app

# Copy entire repo (root POM + modules)
COPY . .

# Build only backend and its dependencies
RUN mvn clean package -DskipTests -pl backend -am

# Use JRE image for running
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy the backend JAR from builder
COPY --from=builder /app/backend/target/backend.jar ./backend.jar

EXPOSE 8080
CMD ["java", "-jar", "backend.jar"]