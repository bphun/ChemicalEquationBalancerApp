FROM openjdk:8-jdk-alpine

WORKDIR /EurekaService
COPY build/libs/eureka-0.0.1-SNAPSHOT.jar ./app.jar
COPY src/main/resources/application-prod.yml ./application-prod.yml
COPY src/main/resources/bootstrap.yml ./bootstrap.yml

EXPOSE 8761

USER root
RUN adduser -D EurekaServiceUser

RUN chown -R EurekaServiceUser:EurekaServiceUser /EurekaService

USER EurekaServiceUser

ENV spring.profiles.active=prod

ENTRYPOINT ["java", "-Xmx500m", "-jar", "app.jar"]
