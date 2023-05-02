package com.foodie.foodmapapi.services;

import com.foodie.foodmapapi.configs.ErrorMessagesProperties;
import com.foodie.foodmapapi.exceptions.NotFoundException;
import com.foodie.foodmapapi.models.Station;
import com.foodie.foodmapapi.repositories.PlatformRepository;
import com.foodie.foodmapapi.repositories.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;
    private final PlatformRepository platformRepository;
    private final ErrorMessagesProperties errorMessages;

    @Override
    public Station getStation(Integer stationID) {
        return stationRepository.findById(stationID)
                .orElseThrow(() -> new NotFoundException(errorMessages.getNotFoundByStationID() + stationID));
    }

    @Override
    public Station getStation(String stationCode) {
        return platformRepository.findByStationCode(stationCode)
                .orElseThrow(() -> new NotFoundException(errorMessages.getNotFoundByStationCode() + stationCode))
                .getStation();
    }
}
