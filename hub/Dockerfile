FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY build/libs/*.jar hub.jar

ENTRYPOINT ["java", "-jar", "/app/hub.jar"]