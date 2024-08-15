package com.example.technologies_service.controller;

import com.example.technologies_service.dto.TechnologyDTO;
import com.example.technologies_service.service.TechnologyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/technologies")
public class TechnologyController {

    private final TechnologyService technologyService;

    public TechnologyController(TechnologyService technologyService) {
        this.technologyService = technologyService;
    }

    @PostMapping
    public Mono<ResponseEntity<TechnologyDTO>> createTechnology(@RequestBody TechnologyDTO technologyDTO) {
        return technologyService.createTechnology(technologyDTO)
                .map(createdTechnology -> new ResponseEntity<>(createdTechnology, HttpStatus.CREATED));
    }
}
