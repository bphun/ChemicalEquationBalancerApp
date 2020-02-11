#!/bin/bash

docker build -t bphun/chemical-equation-balancer:nginx . && \
docker push bphun/chemical-equation-balancer:nginx
