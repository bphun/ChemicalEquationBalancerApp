FROM openjdk:8-jdk-alpine

WORKDIR /ImageRegionProcessorServer
COPY build/libs/imageRegionProcessor-0.0.1-SNAPSHOT.jar ./app.jar
COPY src/main/resources/application-prod.yml ./application-prod.yml

EXPOSE 8081

USER root
RUN adduser -D imageRegionProcessorUser

RUN chown -R imageRegionProcessorUser:imageRegionProcessorUser /ImageRegionProcessorServer

USER imageRegionProcessorUser

ENV spring.profiles.active=prod

ENTRYPOINT ["java", "-jar", "app.jar"]
