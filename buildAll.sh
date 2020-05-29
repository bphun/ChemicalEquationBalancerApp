#!/bin/bash

cd api/auth/
./buildDockerImage.sh

cd ../../api/eureka
./buildDockerImage.sh

cd ../../imageProcessorRequestsServer
./buildDockerImage.sh

cd ../../imageRegionProcessor
./buildDockerImage.sh

cd ../../zuul
./buildDockerImage.sh

cd ../../
