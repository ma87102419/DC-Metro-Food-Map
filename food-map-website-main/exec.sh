#!/usr/bin/env bash

docker build -t food-map-website .
docker run -p 3000:3000 --init food-map-website