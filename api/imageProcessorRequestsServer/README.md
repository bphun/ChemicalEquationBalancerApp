# Image Processor Requests Server

This service serves two purposes. One being a proxy server for the iOS application to send requests to the GCP Cloud Vision API. This is done to have a singular point of entry to the Cloud Vision API, meaning that only one user has access to the private Cloud Vision API key. Moreover, it allows for a me to collect information about each request sent to the Cloud Vision API(i.e image data, Cloud Vision API response time, etc) so that I can create my own dataset used to train the ML model described in the README found in the root of this project. As a result of this feature, this service also exposes an interface(REST API) to interface with database which stores information about these requests.

### Example applications.yml
Path: src/main/resources/
Replace the text enclosed in ${*} with the appropriate information

```
spring:
    datasource:
        url: ${DB_URL}
        username: ${DB_USER_USERNAME}
        password: ${DB_USER_PASSWORD}
    jpa:
        show-sql: true
        hibernate:
            ddl-auto: update
    application:
        name: image-processor-request-server

amazonProperties:
    endpointUrl: ${S3_URL}
    accessKey: ${S3_USER_ACCESS_KEY}
    secretKey: ${S3_USER_SECRET_KEY}
    bucketName: ${S3_BUCKET_NAME}
    sqsUrl: ${SQS_URL}
crossOrigin:
    frontendHostname: '*'

management:
    endpoints:
        web:
            exposure:
                include: health,info,prometheus,auditevents,caches,conditions,httptrace,metrics,scheduledtasks,heapdump

server:
    tomcat:
        mbeanregistry:
            enabled: true

eureka:
    client:
        service-url:
            defaultZone: http://localhost:8761/eureka
```