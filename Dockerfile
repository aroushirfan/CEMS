FROM maven:3.9.12-eclipse-temurin-21 AS build
WORKDIR /app
COPY . /app
RUN mvn -B clean package -DskipTests


FROM eclipse-temurin:21-jre-jammy AS backend
WORKDIR /app
COPY --from=build /app/backend/target/backend.jar backend.jar

CMD ["java","-jar","backend.jar"]


FROM eclipse-temurin:21-jre-jammy AS frontend

WORKDIR /app

RUN apt-get update && \
    apt-get install -y wget unzip libgtk-3-0 libgbm1 libx11-6 && \
    rm -rf /var/lib/apt/lists/*

ENV OPENJFX_URL=https://download2.gluonhq.com/openjfx/21.0.11/openjfx-21.0.11-ea+2_linux-aarch64_bin-sdk.zip

RUN wget $OPENJFX_URL -O /tmp/openjfx.zip && \
    unzip /tmp/openjfx.zip -d /opt && \
    mv /opt/javafx-* /opt/openjfx && \
    rm /tmp/openjfx.zip

COPY --from=build /app/frontend/target/frontend.jar frontend.jar

CMD ["java", "--module-path","/opt/openjfx/lib", "--add-modules","javafx.controls,javafx.fxml", "-jar","frontend.jar"]