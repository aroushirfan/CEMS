FROM maven:3.9.12-eclipse-temurin-21 AS build
ENV DISPLAY=host.docker.internal:0.0
ENV OPENJFX_URL=https://download2.gluonhq.com/openjfx/21/openjfx-21_linux-x64_bin-sdk.zip
LABEL authors="cems"
WORKDIR /app
COPY pom.xml .
COPY . /app

FROM build AS backend
RUN mvn clean package -DskipTests -pl backend -am
CMD ["java", "-jar", "./backend/target/backend.jar"]

FROM build AS frontend
# Install dependencies for GUI + Maven build
RUN apt-get update && \
    apt-get install -y wget unzip libgtk-3-0 libgbm1 libx11-6 && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

RUN wget $OPENJFX_URL -O /tmp/openjfx.zip && \
        unzip /tmp/openjfx.zip -d /opt && \
        rm /tmp/openjfx.zip

RUN mvn clean package -DskipTests -pl frontend -am

CMD ["java", "--module-path", "/opt/javafx-sdk-21/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "./frontend/target/frontend.jar"]