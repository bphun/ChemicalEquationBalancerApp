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

### Configure the API server
The core of the API backend is composed of five individual microservices which communicate using HTTP requests (REST). As a result of this architecture, each of the microservice have their own configuration files. To learn more about how to configure each microservice, view to the respective README.md files for each microservice(auth, eureka, imageProcessorRequestsServer, imageRegionProcessor, zuul).

### Run the server using Docker
You must have Gradle(>=6.4.0), Node(>=v14.3.0), and Npm(>=6.14.4) installed before proceeding with these steps.
```
cd $PROJECT_ROOT_DIRECTORY
./buildAll.sh
docker-compose up --build
```
Using docker compose will start an Nginx reverse proxy, the API microservices, management portal, Prometheus server, and Grafana dashboard,

Since this process will also start a docker container running an Nginx proxy, the API server will be accessible locally at `https://localhost/api/v1/` and the management labeling portal will be accessible at `https://localhost/`. 

The Grafana dashboard will be accessible from the management portal. If you would like to access the Prometheus 
dashboard, it is available at `https://localhost/prometheus`. Port Mappings for each service are availabe in the port Mappings section.

### Run the server directly on your computer
Note: this method will not start the Grafana dashboard, Prometheus server, or Nginx reverse proxy.
To run the API microservices locally, you will need to install Gradle(>=6.4.0) and the Java JDK on your system. Once these packages are installed, you can run:
```
cd $PROJECT_ROOT_DIRECTORY/api/$MICROSERVICE
gradle bootRun
```
Port Mappings for each service are availabe in the port mappings section.
To run the image labeling portal locally you will need to install npm. Once npm is installed, run the following:
```
cd $PROJECT_ROOT_DIRECTORY/management-portal
npm i
npm start
```
The management portal will be availabe at `http://localhost:3000/`

### Port Mappings
| Service                         | Host Port Number | Container Port Number |
|---------------------------------|------------------|-----------------------|
| nginx                           | 80/tcp, 443/tcp  | 7080/tcp, 7443/tcp    |
| zuul                            | 7080/tcp         | 7080/tcp              |
| auth                            | 9100/tcp         | 9100/tcp              |
| image-region-processor-server   | 8081/tcp         | 8081/tcp              |
| image-processor-requests-server | 8080/tcp         | 8080/tcp              |
| management-portal               | 3000/tcp         | 3000/tcp              |
| prometheus                      | 9090/tcp         | 9090/tcp              |
| eureka                          | 8761/tcp         | 8761/tcp              |
| grafana                         | 9091/tcp         | 9091/tcp              |