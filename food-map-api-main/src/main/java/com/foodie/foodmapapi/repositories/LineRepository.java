package com.foodie.foodmapapi.repositories;

import com.foodie.foodmapapi.models.Line;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineRepository extends JpaRepository<Line, String> {
}
