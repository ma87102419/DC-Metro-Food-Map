package com.foodie.foodmapapi.services;

import com.foodie.foodmapapi.models.Station;

public interface StationService {

    Station getStation(Integer stationID);

    Station getStation(String stationCode);
}
