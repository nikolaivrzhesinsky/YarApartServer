FROM openjdk:17
LABEL authors="Vrzesinsky"

ADD /target/apartService-YA-0.0.1-SNAPSHOT.jar backend.jar
ENTRYPOINT ["java", "-jar", "backend.jar"]