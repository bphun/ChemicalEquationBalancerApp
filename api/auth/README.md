# Auth

This service is responsible for handling JWT(JSON Web Token) authentication of users. The produced JWT tokens are subsequently used in future requests to the API to ensure that only authorized users are allowed access the the backend services.

### Example applications.yml
Path: src/main/resources/
Replace the text enclosed in ${*} with the appropriate information

```
spring: 
    application: 
        name: auth-service
    datasource:
        url: ${DB_URL}
        username: ${DB_USER_USERNAME}
        password: ${DB_USER_PASSWORD}
    jpa:
        show-sql: true
        hibernate:
            ddl-auto: update
            
server:
    port: 9100

eureka:
    client:
        service-url: 
            defaultZone: http://localhost:8761/eureka

security:
    jwt:
        uri: /auth/login
        header: Authorization
        prefix: "Bearer "
        expiration: 604800
        JwtSecretKey: secretkey

management:
    endpoints:
        web:
            exposure:
                include: health,info,prometheus,auditevents,caches,conditions,httptrace,metrics,scheduledtasks,heapdump
```