FROM eclipse-temurin:17-alpine

WORKDIR /opt/food-map-api
COPY target/food-map-api*.jar ./food-map-api.jar

VOLUME ["/opt/food-map-api/logs"]

EXPOSE 8080/tcp
CMD ["java", "-Dspring.profiles.active=prod", "-jar", "food-map-api.jar"]