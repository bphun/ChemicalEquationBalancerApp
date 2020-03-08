# Chemical Equation Balancer App

## Background
In chemistry, chemical reactions are represented using chemical equations consisting of chemical compounds and elements present in a reaction. These equations can be broken up into two parts: reactants and products. In order to make use of these chemical equations, there must be an equal quantity of each element on the reactant and product side. The process of adding coefficients to each chemical compound so that the number of elements on each side of the reaction equation are equal is known as balancing. This iOS application aims to balance chemical equations using image recognition technology and basic linear algebra. 

### Problem
Initially, I used the GCP Cloud Vision API but it was innacurate at detecting subscript characters and other string patterns common in chemical equations. Further, the response data the API provided was quite noisy when given an image containing text unrelated to the chemical equation. This is not to say that the GCP vision API is bad; these issues may be caused by the GCP Vision API using a model geared towards general purpose applications, not the specific use case of this project.

### Solution
Since there are no existing hand written chemical equation datasets, I must make my own. Adding to the iOS application I had already created which uses the GCP Cloud Vision API to detect chemical equations in an image, I implemented a new feature that allows users to upload images of chemical equations to a server I created. These images are then made available to me in a portal where I can label chemical equations present in the image. Once I have collected enough training data, I will train a ML model that identifies chemical equations in an image. The objective of this approach is to allow my app to more accurately and quickly identify chemical equations in an image. Moreover, once implemented and optimized, using on device machine learning will allow for a more seamless user experience.

## Usage
### Running the iOS application
Open the ChemicalEquationBalancerApp.xcodeproj project in Xcode located in the iOS folder of the root project directory. This application can be used to balance chemical equations which you enter manually and to upload images of chemical equations to a server(detailed below).

### Running the API server and image labeling portal
### Configure the API server
Create the `application.yml` file in `$PROJECT_ROOT_DIRECTORY/api/imageProcessorRequestsServer/src/main/resources/`
Add the the following snippet to the `application.yml` file you just created. Replace strings with the `${*}` pattern with the appropriate strings. 
```
spring:
    datasource:
    url: ${MY_SQL_SERVER_URL}
        username: ${MY_SQL_SERVER_USERNAME}
        password: ${MY_SQL_SERVER_PASSWORD}
    jpa:
        show-sql: true
        hibernate:
            ddl-auto: update

amazonProperties:
    endpointUrl: ${AWS_S3_BUCKET_URL}
    accessKey: ${AWS_S3_IAM_USER_ACCESS_KEY}
    secretKey: ${AWS_S3_IAM_USER_SECRET_KEY}
    bucketName: ${AWS_S3_BUCKET_NAME}

management:
    endpoints:
        web:
            exposure:
                include: health,info,prometheus,auditevents,caches,conditions,httptrace,metrics,scheduledtasks,heapdump
```

### Using Docker to run the backend services
```
cd $PROJECT_ROOT_DIRECTORY
docker-compose up --build
```
Using docker compose will start an Nginx reverse proxy, the API server, image labeling portal, Prometheus server, and Grafana dashboard,

Since this process will also start a docker container running an Nginx proxy, the API server will be accessible locally at `http://localhost/api/v1/` and the image labeling portal will be accessible at `http://localhost/`. 

The Grafana dashboard will be accessible from the management portal

### Run the backend services directly on your computer
Note: this method will not start the Grafana dashboard, prometheus server, or Nginx reverse proxy.
To run the API server locally you will need to install Gradle and the Java JDK on your system. Once these packages are installed, you can run:
```
cd $PROJECT_ROOT_DIRECTORY/api/imageProcessorRequestsServer
gradle bootRun
```
The API server will be available at `http://localhost:8080/`
To run the image labeling portal locally you will need to install npm. Once npm is installed, run the following:
```
cd $PROJECT_ROOT_DIRECTORY/management-portal
npm i
npm start
```
The management portal will be availabe at `http://localhost:3000/`
