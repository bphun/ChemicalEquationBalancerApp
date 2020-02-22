#!/bin/bash

npm run build && \
docker build -t bphun/chemical-equation-balancer:imageLabelingPortal . && \
docker push bphun/chemical-equation-balancer:imageLabelingPortal
