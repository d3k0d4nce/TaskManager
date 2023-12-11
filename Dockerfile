FROM openjdk:17
ADD /target/spring-security-0.0.1-SNAPSHOT.jar backend.jar
ENTRYPOINT ["java","-jar", "backend.jar"]