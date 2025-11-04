FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY build/libs/*.jar notification.jar

ENTRYPOINT ["java", "-jar", "/app/notification.jar"]
