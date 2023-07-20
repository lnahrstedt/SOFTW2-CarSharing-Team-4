FROM ubuntu:mantic
RUN apt update
RUN apt install openjdk-17-jre -y
RUN apt install maven -y
COPY src src
COPY pom.xml .
RUN mvn package
ENTRYPOINT java -jar /target/*.jar --spring.config.location=file:/src/main/resources/application.properties
