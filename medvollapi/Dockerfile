FROM eclipse-temurin:25-jre

WORKDIR /app

COPY target/medvollapi-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
