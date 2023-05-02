package com.foodie.foodmapapi.mappers;

import com.foodie.foodmapapi.assemblers.StationAssembler;
import com.foodie.foodmapapi.dtos.AddressDTO;
import com.foodie.foodmapapi.dtos.LineDTO;
import com.foodie.foodmapapi.dtos.PlatformDTO;
import com.foodie.foodmapapi.dtos.StationDTO;
import com.foodie.foodmapapi.models.Line;
import com.foodie.foodmapapi.models.Platform;
import com.foodie.foodmapapi.models.Station;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MetroMapper {

    private final StationAssembler stationAssembler;

    public LineDTO mapLineBasics(Line line) {
        LineDTO dto = new LineDTO();
        dto.setLineCode(line.getLineCode());
        dto.setName(line.getName());
        return dto;
    }

    public LineDTO mapLineWithStations(Line line) {
        LineDTO dto = mapLineBasics(line);
        dto.setStations(line.getPlatforms().stream()
                .map(Platform::getStation)
                .map(this::mapStation)
                .map(stationAssembler::toModel)
                .collect(Collectors.toSet()));
        return dto;
    }

    public StationDTO mapStation(Station station) {
        StationDTO dto = new StationDTO();
        dto.setStationID(station.getStationID());
        dto.setName(station.getName());
        dto.setLatitude(station.getLatitude());
        dto.setLongitude(station.getLongitude());
        AddressDTO address = new AddressDTO();
        address.setStreet(station.getAddressStreet());
        address.setCity(station.getAddressCity());
        address.setState(station.getAddressState());
        address.setZip(station.getAddressZip());
        dto.setAddress(address);
        dto.setPlatforms(station.getPlatforms().stream().map(platform -> {
            PlatformDTO platformDTO = new PlatformDTO();
            platformDTO.setStationCode(platform.getStationCode());
            platformDTO.setLine(this.mapLineBasics(platform.getLine()));
            return platformDTO;
        }).collect(Collectors.toSet()));
        return dto;
    }
}
