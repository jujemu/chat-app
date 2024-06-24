FROM openjdk:23-ea-17-jdk-bullseye
COPY build/libs/kakaka-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=dev","-jar","app.jar"]