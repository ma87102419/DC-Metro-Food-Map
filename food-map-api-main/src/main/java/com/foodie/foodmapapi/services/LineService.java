package com.foodie.foodmapapi.services;

import com.foodie.foodmapapi.models.Line;

import java.util.List;

public interface LineService {

    List<Line> getAllLines();

    Line getLine(String lineCode);
}
