FROM openjdk:17
LABEL authors="Vrzesinsky"

ADD /target/configServer-YA-0.0.1-SNAPSHOT.jar config.jar
ENTRYPOINT ["java","-jar","config.jar"]