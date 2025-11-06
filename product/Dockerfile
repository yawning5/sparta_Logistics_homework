FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY build/libs/*.jar product.jar

ENTRYPOINT ["java", "-jar", "/app/product.jar"]
