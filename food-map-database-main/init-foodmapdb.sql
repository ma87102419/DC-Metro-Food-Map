-- Database: foodmapdb

-- DROP DATABASE IF EXISTS foodmapdb;

-- CREATE DATABASE foodmapdb
--     WITH
--     OWNER = postgres
--     ENCODING = 'UTF8'
--     LC_COLLATE = 'en_US.utf8'
--     LC_CTYPE = 'en_US.utf8'
--     TABLESPACE = pg_default
--     CONNECTION LIMIT = -1
--     IS_TEMPLATE = False;

CREATE TABLE account
(
    username CHARACTER varying(16) NOT NULL,
    password CHARACTER varying(255) NOT NULL,
    email CHARACTER varying(255) NOT NULL,
    name CHARACTER varying(100),
    locked BOOLEAN NOT NULL,
    disabled BOOLEAN NOT NULL,
    account_expiration TIMESTAMP WITH TIME ZONE,
    password_expiration TIMESTAMP WITH TIME ZONE NOT NULL,
    PRIMARY KEY (username)
);

CREATE TABLE role
(
    role_value CHARACTER varying(32) NOT NULL,
    name CHARACTER varying(32) NOT NULL,
    PRIMARY KEY (role_value)
);

CREATE TABLE account_roles
(
    username CHARACTER varying(16) NOT NULL,
    role_value CHARACTER varying(32) NOT NULL,
    PRIMARY KEY (username, role_value),
    CONSTRAINT fk_username_account_roles
        FOREIGN KEY (username)
        REFERENCES account(username),
    CONSTRAINT fk_role_value_account_roles
        FOREIGN KEY (role_value)
        REFERENCES role(role_value)
);

CREATE TABLE metro_station
(
    station_id SERIAL NOT NULL,
    name CHARACTER varying(75) NOT NULL,
    latitude REAL NOT NULL,
    longitude REAL NOT NULL,
    address_street CHARACTER varying(100) NOT NULL,
    address_city CHARACTER varying(50) NOT NULL,
    address_state CHARACTER varying(2) NOT NULL,
    address_zip CHARACTER varying(5) NOT NULL,
    PRIMARY KEY (station_id)
);

CREATE TABLE metro_line
(
    line_code CHARACTER varying(2) NOT NULL,
    name CHARACTER varying(12) NOT NULL,
    PRIMARY KEY (line_code)
);

CREATE TABLE metro_station_platform
(
    station_id INTEGER NOT NULL,
    line_code CHARACTER varying(2) NOT NULL,
    station_code CHARACTER varying(3) NOT NULL,
    PRIMARY KEY (station_id, line_code),
    CONSTRAINT fk_station_id_metro_station_platform
        FOREIGN KEY (station_id)
        REFERENCES metro_station(station_id),
    CONSTRAINT fk_line_code_metro_station_platform
        FOREIGN KEY (line_code)
        REFERENCES metro_line(line_code)
);

CREATE TABLE restaurant
(
    restaurant_id SERIAL NOT NULL,
    name CHARACTER varying(100) NOT NULL,
    third_party_id CHARACTER varying(50) NOT NULL,
    third_party_source CHARACTER varying(12) NOT NULL,
    open_time TIME,
    close_time TIME,
    star_rating REAL,
    price_rating INTEGER,
    latitude REAL NOT NULL,
    longitude REAL NOT NULL,
    address_street CHARACTER varying(100) NOT NULL,
    address_city CHARACTER varying(50) NOT NULL,
    address_state CHARACTER varying(2) NOT NULL,
    address_zip CHARACTER varying(5) NOT NULL,
    nearby_station_id INTEGER NOT NULL,
    PRIMARY KEY (restaurant_id),
    CONSTRAINT fk_nearby_station_id_nearby_restaurant
        FOREIGN KEY (nearby_station_id)
        REFERENCES metro_station(station_id)
);

CREATE TABLE favorite_restaurant
(
    username CHARACTER varying(16) NOT NULL,
    restaurant_id INTEGER NOT NULL,
    PRIMARY KEY (username, restaurant_id),
    CONSTRAINT fk_username_favorite_restaurant
        FOREIGN KEY (username)
        REFERENCES account(username),
    CONSTRAINT fk_restaurant_id_favorite_restaurant
        FOREIGN KEY (restaurant_id)
        REFERENCES restaurant(restaurant_id)
);

CREATE TABLE review
(
    review_id SERIAL NOT NULL,
    restaurant_id INTEGER NOT NULL,
    source CHARACTER varying(25) NOT NULL,
    name CHARACTER varying(100) NOT NULL,
    comment TEXT NOT NULL,
    post_time TIMESTAMP WITH TIME ZONE NOT NULL,
    star_rating INTEGER,
    PRIMARY KEY (review_id),
    CONSTRAINT fk_restaurant_id_review
        FOREIGN KEY (restaurant_id)
        REFERENCES restaurant(restaurant_id)
);

INSERT INTO role(role_value, name) VALUES ('ROLE_ADMIN', 'Admin Role'), ('ROLE_USER', 'User Role'), ('ROLE_NONE', 'No Role');

INSERT INTO account(username, password, email, name, locked, disabled, account_expiration, password_expiration)
VALUES ('admin', '$2a$10$R2/m2WEChod2c2EQGupQ9OUsE0eIuA00lb2ks11P3rHz/9dl/NSke', 'admin@fake.com', 'Admin Account', false, false, NULL, TIMESTAMP WITH TIME ZONE '2023-12-31 23:59:59 EST');

INSERT INTO account_roles(username, role_value) VALUES ('admin', 'ROLE_ADMIN');