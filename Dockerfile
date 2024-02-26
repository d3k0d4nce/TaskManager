# Первый этап: сборка приложения с использованием образа Maven
FROM maven:latest AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# Второй этап: запуск приложения с использованием образа OpenJDK
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/spring-security-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]