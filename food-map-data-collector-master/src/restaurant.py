import psycopg2
import json
from datetime import datetime
import collections
import pytz

timezone = pytz.timezone('US/Eastern')
timeformat = '%Y-%m-%d %H:%M:%S %Z'

def find_restaurant(restaurant_list, restaurant):
    for i, r in restaurant_list.items():
        # Check street, city, state
        if r[9] == restaurant[9] and r[10] == restaurant[10] and r[11] == restaurant[11]:
            return i
        # Check latitude and longitude
        if abs(r[7] - restaurant[7]) < 0.0001 and abs(r[8] - restaurant[8]) < 0.0001:
            return i
        # Check names
        if r[0].find(restaurant[0]) != -1 or restaurant[0].find(r[0]) != -1:
            return i
    return -1

# Read data saved from Yelp and Google Maps API requests
def restaurant_read(data_folder, cursor):
    print("\nReading and Processing Yelp and Google Maps Data")
    restaurants = []
    reviews = []
    next_restaurant_id = 1

    # Loop through restaurants nearby each station
    cursor.execute("SELECT name, station_id FROM metro_station;")
    for row in cursor.fetchall():
        station = { "name": row[0], "station_id": row[1] }

        with open(f'{data_folder}/yelp/{station["name"].replace("/", " ")}.json') as file_yelp:
            yelp_raw = json.load(file_yelp)
        with open(f'{data_folder}/google/{station["name"].replace("/", " ")}.json') as file_google:
            google_raw = json.load(file_google)
        
        # Loop through all the Yelp restaurants
        restaurants_yelp = {}
        for r in yelp_raw:
            restaurant = (
                r["name"],
                r["id"],
                "Yelp",
                datetime.strptime(r["open"], '%H%M').time(),
                datetime.strptime(r["close"], '%H%M').time(),
                r["rating"],
                len(r["price"]) if "price" in r else -1,
                r["coordinates"]["latitude"],
                r["coordinates"]["longitude"],
                r["location"]["address1"] if r["location"]["address1"] is not None else "N/A",
                r["location"]["city"],
                r["location"]["state"],
                r["location"]["zip_code"][:5],
                station["station_id"]
            )

            # Add restaurant to list of restaurants
            restaurants.append(restaurant)
            current_restaurant_id = next_restaurant_id
            next_restaurant_id += 1

            # Track Yelp restaurants in case Google has duplicate
            restaurants_yelp[current_restaurant_id] = restaurant

            # Add each Yelp review to the list of reviews
            if "reviews" in r:
                for rv in r["reviews"]:
                    review = (
                        current_restaurant_id,
                        "Yelp",
                        rv["user"]["name"],
                        rv["text"],
                        datetime.strptime(rv["time_created"], '%Y-%m-%d %H:%M:%S').astimezone(timezone).strftime(timeformat),
                        rv["rating"]
                    )
                    reviews.append(review)
            

        # Loop through all the Google restaurants
        for r in google_raw:
            restaurant = (
                r["name"],
                r["place_id"],
                "Google",
                datetime.strptime(r["open"], '%H%M').time(),
                datetime.strptime(r["close"], '%H%M').time(),
                r["rating"] if "rating" in r else -1,
                r["price_level"] if "price_level" in r else -1,
                r["geometry"]["location"]["lat"],
                r["geometry"]["location"]["lng"],
                r["vicinity"].split(',')[0] if r["vicinity"] != "United States" else "N/A", # Street
                r["plus_code"]["compound_code"].split(',')[0][8:], # City
                r["plus_code"]["compound_code"].split(',')[-2].strip(), # State
                r["zip"] if r["zip"] != "NULL" else "N/A", # Zip
                station["station_id"]
            )

            # Check if this restaurant already exists from Yelp
            current_restaurant_id = find_restaurant(restaurants_yelp, restaurant)

            # Restaurant does not exist from Yelp, use next available restaurant_id
            if current_restaurant_id == -1:
                restaurants.append(restaurant)
                current_restaurant_id = next_restaurant_id
                next_restaurant_id += 1
            # Restaurant does exist, use existing restaurant
            else:
                restaurant = restaurants_yelp[current_restaurant_id]

            # Add each Google review to the list of reviews
            if "reviews" in r:
                for rv in r["reviews"]:
                    review = (
                        current_restaurant_id,
                        "Google",
                        rv["author_name"],
                        rv["text"],
                        datetime.utcfromtimestamp(rv["time"]).astimezone(timezone).strftime(timeformat),
                        rv["rating"]
                    )
                    reviews.append(review)
    return {
        "restaurants": restaurants,
        "reviews": reviews
    }

# Write data to database
def restaurant_write(data, cursor):
    # Insert Restaurant data
    print("\nWriting Restaurants to Database")
    cursor.executemany("INSERT INTO restaurant VALUES(DEFAULT,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s);", data["restaurants"])
    # Print data inserted into DB
    cursor.execute("SELECT COUNT(*) FROM restaurant;")
    print(f'Total Restaurant Count: {cursor.fetchone()[0]}') # 1985, Actual: 3280
    cursor.execute("SELECT * FROM restaurant LIMIT 6;")
    for row in cursor.fetchall():
        print(row)
    
    # Insert Review data
    print("\nWriting Reviews to Database")
    cursor.executemany("INSERT INTO review VALUES(DEFAULT,%s,%s,%s,%s,%s,%s);", data["reviews"])
    # Print data inserted into DB
    cursor.execute("SELECT COUNT(*) FROM review;")
    print(f'Total Review Count: {cursor.fetchone()[0]}') # 15266, Actual: 14636
    cursor.execute("SELECT * FROM review LIMIT 6;")
    for row in cursor.fetchall():
        print(row)

def restaurant_clean(cursor):
    # Clear tables before adding fresh data
    print("\nClearing Restaurant and Review Data from Database")
    sql_delete_review_restaurant_data = '''delete from review;
          ALTER SEQUENCE review_review_id_seq RESTART WITH 1;
          delete from restaurant;
          ALTER SEQUENCE restaurant_restaurant_id_seq RESTART WITH 1;'''
    cursor.execute(sql_delete_review_restaurant_data)
