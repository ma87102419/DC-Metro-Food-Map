package com.foodie.foodmapapi.services;

import com.foodie.foodmapapi.configs.ErrorMessagesProperties;
import com.foodie.foodmapapi.exceptions.NotFoundException;
import com.foodie.foodmapapi.models.Line;
import com.foodie.foodmapapi.repositories.LineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LineServiceImpl implements LineService {

    private final LineRepository lineRepository;
    private final ErrorMessagesProperties errorMessages;

    @Override
    public List<Line> getAllLines() {
        return lineRepository.findAll();
    }

    @Override
    public Line getLine(String lineCode) {
        return lineRepository.findById(lineCode)
                .orElseThrow(() -> new NotFoundException(errorMessages.getNotFoundByLineCode() + lineCode));
    }
}
