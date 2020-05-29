FROM openjdk:8-jdk-alpine

WORKDIR /ZuulServer
COPY build/libs/zuul-0.0.1-SNAPSHOT.jar ./app.jar
COPY src/main/resources/application-prod.yml ./application-prod.yml

EXPOSE 7080

USER root
RUN adduser -D zuulUser

RUN chown -R zuulUser:zuulUser /ZuulServer

USER zuulUser

ENV spring.profiles.active=prod

ENTRYPOINT ["java", "-jar", "app.jar"]
