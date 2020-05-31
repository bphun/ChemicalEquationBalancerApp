FROM openjdk:8-jdk-alpine

WORKDIR /ImageProcessorRequestsServer
COPY build/libs/imageProcessorRequestsServer-0.0.1-SNAPSHOT.jar ./app.jar
COPY src/main/resources/bootstrap.yml ./bootstrap.yml

EXPOSE 8080

USER root
RUN adduser -D imageProcessorUser

RUN chown -R imageProcessorUser:imageProcessorUser /ImageProcessorRequestsServer

USER imageProcessorUser

ENV spring.profiles.active=prod

ENTRYPOINT ["java", "-Xmx200m", "-jar", "app.jar"]
