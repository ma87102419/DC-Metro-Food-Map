import psycopg2
import json

connection = psycopg2.connect(
    host="localhost",  # host where the database is running
    port=5432,
    database="foodmapdb",  # name of the database to connect to
    user="postgres",
    password="postgres"
)
connection.autocommit = True
cursor = connection.cursor()


# Map saved data from station codes to station names so we don't have duplicate data for stations with multiple platforms (station codes)
sql_first_platform_at_each_station = '''
SELECT DISTINCT s.name, s.station_id, p.station_code
FROM metro_station s JOIN metro_station_platform p ON s.station_id = p.station_id
WHERE p.station_code = (SELECT station_code
	FROM metro_station_platform
	WHERE station_id = s.station_id
	ORDER BY station_code ASC
	LIMIT 1
)
ORDER BY s.station_id ASC;
'''
cursor.execute(sql_first_platform_at_each_station)
for row in cursor.fetchall():
    print(row)
    # Replace yelp with googleMap
    with open(f'data/googleMap/{row[2]}.json', "r") as file_read:
        with open(f'data/google_maps/{row[0].replace("/", " ")}.json', "x") as file_write:
            file_write.write(file_read.read())
            file_write.close()
        file_read.close()


connection.commit()
connection.close()
