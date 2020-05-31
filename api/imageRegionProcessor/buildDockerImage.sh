#!/bin/bash

gradle clean && \
gradle build && \
docker build -t bphun/chemical-equation-balancer:image-region-processor-server .
