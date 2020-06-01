#!/bin/bash

# exit when any command fails
set -e

echo "Building api/auth"
cd api/auth/
./build.sh

echo "Building api/eureka"
cd ../eureka
./build.sh

echo "Building api/imageProcessorRequestsServer"
cd ../imageProcessorRequestsServer
./build.sh

echo "Building api/imageRegionProcessor"
cd ../imageRegionProcessor
./build.sh

echo "Building api/zuul"
cd ../zuul
./build.sh

echo "Building management-portal"
cd ../../management-portal
./build.sh

cd ../

if [[ $1 = "-d" ]] ; then
    docker-compose -f docker-compose/all-docker-compose.yml build --parallel
fi
