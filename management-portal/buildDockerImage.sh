#!/bin/bash

npm run build && \
docker build -t bphun/chemical-equation-balancer:management-portal .
