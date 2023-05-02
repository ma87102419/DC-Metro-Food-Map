import os
import time
import psycopg2
from metro import metro_read, metro_write, metro_clean
from restaurant import restaurant_read, restaurant_write, restaurant_clean

def main():
    # Get environment variables
    db_hostname = os.getenv("DB_HOSTNAME")
    print(f"DB Hostname: {db_hostname}")

    # Allow Postgres DB time to open port for connections

    # Create single DB connection
    connected = False
    attempts = 0
    while not connected and attempts < 20:
        try:
            attempts += 1
            connection = psycopg2.connect(
                host=db_hostname,  # host where the database is running
                port=5432,
                database="foodmapdb",  # name of the database to connect to
                user="postgres",
                password="postgres"
            )
            connected = True
        except:
            print(f'Database connection failed, attempt {attempts}, trying again in 1 second')
            time.sleep(1)

    connection.autocommit = True
    cursor = connection.cursor()

    # Process WMATA, Yelp, and Google Maps data
    data_folder = "data"

    restaurant_clean(cursor)
    metro_clean(cursor)

    metro_data = metro_read(data_folder)
    metro_write(metro_data, cursor)

    restaurant_data = restaurant_read(data_folder, cursor)
    restaurant_write(restaurant_data, cursor)

    print("\nData Collector is Complete!")
    print("Everything is in the Database")

    connection.commit()
    connection.close()

if __name__ == "__main__":
    main()
