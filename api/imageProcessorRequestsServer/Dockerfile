FROM openjdk:8-jdk-alpine

WORKDIR /ImageProcessorRequestsServer
COPY build/libs/imageProcessorRequestsServer-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8080

USER root
RUN adduser -D imageProcessorUser

RUN chown -R imageProcessorUser:imageProcessorUser /ImageProcessorRequestsServer

USER imageProcessorUser

ENTRYPOINT ["java", "-jar", "app.jar"]
