# Use a JDK image for building
FROM maven:3.9.6-amazoncorretto-21 AS builder

WORKDIR /app

# Copy only pom.xml first to leverage caching
COPY pom.xml .

# Resolve dependencies
RUN mvn dependency:resolve

# Copy the rest of the source code
COPY . .

# Build the jar
RUN mvn clean package -DskipTests

# --------------------------
# Second stage: minimal runtime image
# --------------------------
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy built jar from builder stage
COPY --from=builder /app/target/backend.jar ./backend.jar

EXPOSE 8080

CMD ["java", "-jar", "backend.jar"]