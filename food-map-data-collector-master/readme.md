# Food Map Data Collector

## Build Docker Image

After building the database container, build the data collector image in `env/`:

`docker build --no-cache -t food-map-data-collector:latest .`

This will use the Dockerfile located in the food-map-data-collector/env


## Run Standalone Container and its Dependencies

You can run the data collector as a standalone container using this command in `env/`:

`docker run -it -d --mount type=bind,source=$(realpath ..),target=/home --name food-map-data-collector food-map-data-collector:latest`

As the data collector needs to connect to the database, create a network with the following commands:

```
docker network create fm-network
docker network connect fm-network food-map-database
docker network connect fm-network food-map-data-collector
docker network inspect fm-network
```

Retrieve database IP address:

`docker inspect -f "{{with index .NetworkSettings.Networks \"fm-network\"}}{{.IPAddress}}{{end}}" food-map-database > database_ip`


## Execute Data Collector

Run the following commands to send data into database:

`docker exec -it food-map-data-collector bash`

`cd /home/code/`

`python metro_stations.py`

`python restaurant_review.py`
