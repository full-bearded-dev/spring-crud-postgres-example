FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY build/libs/crud.app-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]