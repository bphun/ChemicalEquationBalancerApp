#!/bin/bash

gradle clean && \
gradle build && \
docker build -t bphun/chemical-equation-balancer:eureka . && \
docker push bphun/chemical-equation-balancer:eureka
