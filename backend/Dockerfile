# Stage 1: Build the backend jar
FROM maven:3.9.6-amazoncorretto-21 AS build
WORKDIR /app

# Copy only backend pom.xml first to cache dependencies
COPY backend/pom.xml backend/
RUN mvn dependency:resolve -f backend/pom.xml

# Copy backend source code
COPY backend/ backend/

# Build the backend jar
RUN mvn package -DskipTests -f backend/pom.xml

# Stage 2: Run the backend jar
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=build /app/backend/target/backend.jar backend.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "backend.jar"]