package com.example.technologies_service.controller;

import com.example.technologies_service.dto.TechnologyDTO;
import com.example.technologies_service.service.ITechnologyService;
import com.example.technologies_service.service.TechnologyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/technologies")
public class TechnologyController {

    private final ITechnologyService technologyService;

    public TechnologyController(TechnologyService technologyService) {
        this.technologyService = technologyService;
    }

    @PostMapping
    public Mono<ResponseEntity<TechnologyDTO>> createTechnology(@RequestBody @Valid TechnologyDTO technologyDTO) {
        return technologyService.createTechnology(technologyDTO)
                .map(createdTechnology -> new ResponseEntity<>(createdTechnology, HttpStatus.CREATED));
    }

    @GetMapping
    public Flux<TechnologyDTO> getAllTechnologies(
            @RequestParam(name = "sort", defaultValue = "asc") String sortDirection,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        return technologyService.getAllTechnologies(sortDirection, page, size);
    }
}
