#!/bin/bash

npm run build

if [[ $1 = "-d" ]] ; then
    docker build -t bphun/chemical-equation-balancer:management-portal .
fi