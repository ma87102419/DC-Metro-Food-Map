# Food Map Database

## Build Docker Container

Build the docker container while in this directory:

`docker build -t food-map-database:latest .`

This will use the Dockerfile located in the food-map-database directory

## Run Standalone Database

You can run the database as a standalone container using this command (assuming you are in the food-map-database directory):

`docker run --name food-map-database -v postgres_data:/var/lib/postgresql/data -p 5432:5432 -d food-map-database:latest`

Remove `-v postgres_data:/var/lib/postgresql/data` while testing the sql init script,
otherwise previous data will prevent the script from running.
Doesn't seem like -v works at the moment anyways (restarting the container will wipe any new/modified data and re-create using `init-foodmapdb.sql`)

## Run Entire System

Or you can run it with our other containers using docker-compose:

`docker compose up -d` (or with specifying the yaml file: `docker compose -f docker-compose.yml up -d`)

Using `-d` runs the command in the background

## Connect to DB

You can connect to the database using your code or using PG Admin by connecting to localhost:5432 and using the correct username/password

## Main admin account

The main admin account credentials are:

> Username: admin
>
> Password: password