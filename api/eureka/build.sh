#!/bin/bash

# exit when any command fails
set -e

gradle clean && \
gradle build

if [[ $1 = "-d" ]] ; then
    docker build -t bphun/chemical-equation-balancer:eureka .
fi