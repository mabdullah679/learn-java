FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY target/task-list-app-1.0-SNAPSHOT.jar /app/task-list-app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "task-list-app.jar"]