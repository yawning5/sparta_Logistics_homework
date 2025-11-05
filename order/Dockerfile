FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY build/libs/*.jar order.jar

ENTRYPOINT ["java", "-jar", "/app/order.jar"]