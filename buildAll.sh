#!/bin/bash

# exit when any command fails
set -e

# keep track of the last executed command
trap 'last_command=$current_command; current_command=$BASH_COMMAND' DEBUG
# echo an error message before exiting
trap 'echo "\"${last_command}\" command filed with exit code $?."' EXIT

echo "Building api/auth"
cd api/auth/
./buildDockerImage.sh

echo "Building api/eureka"
cd ../eureka
./buildDockerImage.sh

echo "Building api/imageProcessorRequestsServer"
cd ../imageProcessorRequestsServer
./buildDockerImage.sh

echo "Building api/imageRegionProcessor"
cd ../imageRegionProcessor
./buildDockerImage.sh

echo "Building api/zuul"
cd ../zuul
./buildDockerImage.sh

echo "Building management-portal"
cd ../../management-portal
./buildDockerImage.sh

cd ../../
