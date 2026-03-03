FROM maven:3.9.6-amazoncorretto-21 AS build
LABEL authors="cems"

WORKDIR /app

COPY pom.xml .
COPY . /app

RUN mvn package -DskipTests -pl backend -am

CMD ["java", "-jar", "./backend/target/backend.jar"]