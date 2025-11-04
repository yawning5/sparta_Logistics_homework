FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY build/libs/*.jar eureka.jar

ENTRYPOINT ["java", "-jar", "/app/eureka.jar"]