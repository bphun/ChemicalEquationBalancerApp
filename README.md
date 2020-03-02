# Chemical Equation Balancer App

### Background
In chemistry, chemical reactions are represented using chemical equations consisting of chemical compounds and elements present in a reaction. These equations can be broken up into two parts, reactants and products. In order to make use of these chemical equations, there must be an equal amount of every element on the reactant and product side -- a process known as balancing. This iOS application aims to use image recognition technology to identify and balance chemical equations in an image. 

### Problem
Initially, I used the GCP Cloud Vision API but soon realized that it was innacurate at detecting subscript characters and other strings common in chemical equations.

### Solution
Using the iOS application that I had already created which uses the GCP Cloud Vision API, I implemented a new feature that allows users to upload images of chemical equations to a server I created. These images are then made available to me in a portal where I can label any chemical equations. Once I have collected enough training data, I will train a ML model that identifies chemical equations. I am hoping that this approach will provide me with much more accurate data.

## Usage
### Running the iOS application
Open the ChemicalEquationBalancerApp.xcodeproj project in Xcode located in the iOS folder of the root project directory. This application can be used to balance chemical equations which you enter manually and to upload images of chemical equations to a server(detailed below).

### Running the API server and image labeling portal
Run a containerized version of the server using Docker
```
cd $PROJECT_ROOT_DIRECTORY
docker-compose up --build
```
This will start the API server and image labeling portal.
The API server will be accessible locally at `http://localhost/api/v1/` and the image labeling portal will be accessible at `http://localhost`.

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
