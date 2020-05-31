# Zuul Proxy

The Zuul Proxy acts as the API gateway. Although an API gateway results in a single point of failure, it also results in a single point of ingress. As a result, all activity to the backend microservices are easily logged and authorization of API users is guaranteed.

### Example applications.yml
Path: src/main/resources/
Replace the text enclosed in ${*} with the appropriate information

```
zuul:
    routes: 
        requests:
            path: /requests/**
            url: http://localhost:8080
            strip-prefix: false
        regions:
            path: /regions/**
            url: http://localhost:8080
            strip-prefix: false
        imageProcessor:
            path: /imageProcessor/**
            url: http://localhost:8081
            strip-prefix: false
    sensitive-headers: Cookie,Set-Cookie  
    ignored-headers: Access-Control-Allow-Credentials, Access-Control-Allow-Origin

spring: 
    application: 
        name: zuul

eureka:
    client:
        service-url: 
            defaultZone: http://localhost:8761/eureka

server:
    port: 7080

security:
    jwt:
        uri: /auth/**
        header: "Authorization"
        prefix: "Bearer "
        expiration: 604800000
        JwtSecretKey: secretkey

ribbon:
    eureka:
        enabled: false

management:
    endpoints:
        web:
            exposure:
                include: health,info,prometheus,auditevents,caches,conditions,httptrace,metrics,scheduledtasks,heapdump
    security:
        enabled: false

endpoints:
  actuator:
    sensitive: false
```