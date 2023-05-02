import psycopg2
import json

# Read data saved from WMATA API requests
def metro_read(data_folder):
    print("\nReading and Processing Metro Data")

    # Read metro data from file
    with open(data_folder + "/wmata/lines.json", "r") as file_lines:
        lines_raw = json.load(file_lines)["Lines"]
    with open(data_folder + "/wmata/stations.json", "r") as file_stations:
        stations_raw = json.load(file_stations)["Stations"]

    lines = []
    stations = []
    platforms = []
    station_ids = {}

    # Create list of metro lines
    lines = [(l["LineCode"], l["DisplayName"]) for l in lines_raw]

    # for i, s in enumerate(stations_raw, 1):
    i = 1
    for s in stations_raw:

        # Add station if it doesn't exist (have an ID)
        if s["Code"] not in station_ids:
            station = (s["Name"], s["Lat"], s["Lon"], s["Address"]["Street"], s["Address"]["City"], s["Address"]["State"], s["Address"]["Zip"])
            stations.append(station)

            # Give station the next id value
            station_ids[s["Code"]] = i
            # Associate other platforms (wmata stations) with current id
            if s["StationTogether1"] != "":
                station_ids[s["StationTogether1"]] = i
            if s["StationTogether1"] != "":
                station_ids[s["StationTogether1"]] = i

            # Increment on new stations
            i += 1

        # Get this platform's (wmata station code's) station_id value
        station_id = station_ids[s["Code"]]

        # Add platform-line mappings for this platform (wmata station code)
        if s["LineCode1"] is not None:
            platforms.append((station_id, s["LineCode1"], s["Code"]))
        if s["LineCode2"] is not None:
            platforms.append((station_id, s["LineCode2"], s["Code"]))
        if s["LineCode3"] is not None:
            platforms.append((station_id, s["LineCode3"], s["Code"]))
        if s["LineCode4"] is not None:
            platforms.append((station_id, s["LineCode4"], s["Code"]))

    return {
        "lines": lines,
        "stations": stations,
        "platforms": platforms
    }

# Write data to database
def metro_write(data, cursor):
    # Insert Metro Station data
    print("\nWriting Metro Stations to Database")
    cursor.executemany("INSERT INTO metro_station VALUES(DEFAULT,%s,%s,%s,%s,%s,%s,%s);", data["stations"])
    # Print data inserted into DB
    cursor.execute("SELECT COUNT(*) FROM metro_station;")
    print(f'Total Metro Station Count: {cursor.fetchone()[0]}') # 97
    cursor.execute("SELECT * FROM metro_station LIMIT 6;")
    for row in cursor.fetchall():
        print(row)

    # Insert Metro Line data
    print("\nWriting Metro Lines to Database")
    cursor.executemany("INSERT INTO metro_line VALUES(%s,%s);", data["lines"])
    # Print data inserted into DB
    cursor.execute("SELECT COUNT(*) FROM metro_line;")
    print(f'Total Metro Line Count: {cursor.fetchone()[0]}') # 152
    cursor.execute("SELECT * FROM metro_line;")
    for row in cursor.fetchall():
        print(row)

    # Insert Metro Station Platform data
    print("\nWriting Metro Stations Platforms to Database")
    cursor.executemany("INSERT INTO metro_station_platform VALUES(%s,%s,%s);", data["platforms"])
    # Print data inserted into DB
    cursor.execute("SELECT COUNT(*) FROM metro_station_platform;")
    print(f'Total Metro Station Platforms Count: {cursor.fetchone()[0]}') # 152
    cursor.execute("SELECT * FROM metro_station_platform LIMIT 6;")
    for row in cursor.fetchall():
        print(row)

def metro_clean(cursor):
    # Clear tables before adding fresh data
    print("\nClearing Metro Data from Database")
    sql_delete_metro_data = '''delete from metro_station_platform;
            delete from metro_line;
            delete from metro_station;
            ALTER SEQUENCE metro_station_station_id_seq RESTART WITH 1;'''
    cursor.execute(sql_delete_metro_data)
