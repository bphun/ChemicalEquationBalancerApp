# Image Region Processor

This service processes regions(bounding boxes) produced by users in the image labeling tool by cropping, scaling, and rotating images to produce an image that only contains a single chemical equation. These images are then added to the ML training dataset. In the future, this service will also normalize the color/lighting across all images to improve  accuracy of the ML model.

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
        name: image-region-processor
        
amazonProperties:
    endpointUrl: ${S3_URL}
    accessKey: ${S3_USER_ACCESS_KEY}
    secretKey: ${S3_USER_SECRET_KEY}
    bucketName: ${S3_BUCKET_NAME}
    sqsUrl: ${SQS_URL}
    
crossOrigin:
    frontendHostname: '*'

requestsApi:
    hostname: http://localhost:8080
    allRegionsEndpoint: /regions/all/
    selectRegionsEndpoint: /regions/

management:
    endpoints:
        web:
            exposure:
                include: health,info,prometheus,auditevents,caches,conditions,httptrace,metrics,scheduledtasks,heapdump

server:
    port: 8081
    tomcat:
        mbeanregistry:
            enabled: true

eureka:
    client:
        service-url:
          defaultZone: http://localhost:8761/eureka
```