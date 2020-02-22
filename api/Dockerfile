FROM openjdk:8-jdk-alpine

WORKDIR /ChemicalEquationBalancerServer
COPY build/libs/ChemicalEquationBalancerAppServer-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8080

USER root
RUN adduser -D appUser

USER appUser

ENTRYPOINT ["java", "-jar", "app.jar"]
