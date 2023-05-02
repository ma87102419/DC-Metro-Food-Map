docker build --rm -t food-map-website .
docker run -p 3000:3000 --pid=host food-map-website