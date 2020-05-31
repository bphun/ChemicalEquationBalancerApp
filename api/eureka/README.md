# Eureka Service Discovery

Eureka service discovery is used internally between Sprinboot microservices to allow each service to communicate with each other using their respective service names(i.e zuul, auth-service, eureka-server).

### Example applications.yml
Path: src/main/resources/
Replace the text enclosed in ${*} with the appropriate information

```
spring: 
    application:
        name: eureka-server

server:
    port: 8761

eureka:
    client:
        register-with-eureka: false
        fetch-registry: false

management:
    endpoints:
        web:
            exposure:
                include: health,info,prometheus,auditevents,caches,conditions,httptrace,metrics,scheduledtasks,heapdump
```