FROM eclipse-temurin:21-jdk


WORKDIR /app

COPY build/libs/*.jar member.jar

ENTRYPOINT ["java", "-jar", "/app/member.jar"]