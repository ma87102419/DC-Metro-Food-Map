package com.foodie.foodmapapi.controllers;

import com.foodie.foodmapapi.assemblers.LineAssembler;
import com.foodie.foodmapapi.dtos.LineDTO;
import com.foodie.foodmapapi.exceptions.ServiceException;
import com.foodie.foodmapapi.mappers.MetroMapper;
import com.foodie.foodmapapi.models.Line;
import com.foodie.foodmapapi.services.LineService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/lines")
@RequiredArgsConstructor
public class LineController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final LineService lineService;
    private final MetroMapper metroMapper;
    private final LineAssembler lineAssembler;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<CollectionModel<EntityModel<LineDTO>>> getAllLines() {
        try {
            logger.info("LineController.getAllLines()");
            List<Line> lines = lineService.getAllLines();
            List<LineDTO> lineDTOs = lines.stream().map(metroMapper::mapLineBasics).toList();
            CollectionModel<EntityModel<LineDTO>> lineModels = lineAssembler.toCollectionModel(lineDTOs);
            return new ResponseEntity<>(lineModels, HttpStatus.OK);
        }
        catch (ServiceException e) {
            throw e;
        }
        catch (Exception e) {
            logger.error("Some Error: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @GetMapping("/{lineCode}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<EntityModel<LineDTO>> getLine(@PathVariable("lineCode") String lineCode) {
        try {
            logger.info("LineController.getLine({})", lineCode);
            Line line = lineService.getLine(lineCode);
            LineDTO lineDTO = metroMapper.mapLineWithStations(line);
            EntityModel<LineDTO> lineModel = lineAssembler.toModel(lineDTO);
            return new ResponseEntity<>(lineModel, HttpStatus.OK);
        }
        catch (ServiceException e) {
            throw e;
        }
        catch (Exception e) {
            logger.error("Some Error: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }
}
