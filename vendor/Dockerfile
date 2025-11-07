FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY build/libs/*.jar vendor.jar

ENTRYPOINT ["java", "-jar", "-DEUREKA-HOST=eureka-server", "/app/vendor.jar"]
