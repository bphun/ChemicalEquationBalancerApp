#!/bin/bash

docker build -t bphun/chemical-equation-balancer:dockerImageUpdater . && \
docker push bphun/chemical-equation-balancer:dockerImageUpdater
