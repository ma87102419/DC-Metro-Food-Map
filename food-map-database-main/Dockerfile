FROM postgres:latest

ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres
ENV POSTGRES_DB=foodmapdb

COPY init-foodmapdb.sql /docker-entrypoint-initdb.d/init-foodmapdb.sql
RUN sed -i 's/\r$//g' /docker-entrypoint-initdb.d/init-foodmapdb.sql