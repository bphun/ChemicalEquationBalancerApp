FROM openjdk:8-jdk-alpine

WORKDIR /AuthService
COPY build/libs/auth-0.0.1-SNAPSHOT.jar ./app.jar
COPY src/main/resources/application-prod.yml ./application-prod.yml

EXPOSE 9100

USER root
RUN adduser -D AuthServiceUser

RUN chown -R AuthServiceUser:AuthServiceUser /AuthService

USER AuthServiceUser

ENV spring.profiles.active=prod

ENTRYPOINT ["java", "-jar", "app.jar"]
