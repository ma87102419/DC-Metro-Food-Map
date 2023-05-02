package com.foodie.foodmapapi.repositories;

import com.foodie.foodmapapi.models.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlatformRepository extends JpaRepository<Platform, Platform.PlatformID> {

    Optional<Platform> findByStationCode(String stationCode);
}
