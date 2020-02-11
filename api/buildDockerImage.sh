#!/bin/bash

gradle clean && \
gradle build && \
docker build -t bphun/chemical-equation-balancer:api . && \
docker push bphun/chemical-equation-balancer:api
