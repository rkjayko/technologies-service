package com.example.technologies_service.infrastructure.controller;

import com.example.technologies_service.domain.dto.TechnologyDTO;
import com.example.technologies_service.domain.useCase.ITechnologyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/technologies")
@Tag(name = "Technology", description = "Technology management APIs")
public class TechnologyController {

    private final ITechnologyService technologyService;

    public TechnologyController(ITechnologyService technologyService) {
        this.technologyService = technologyService;
    }

    @PostMapping
    @Operation(summary = "Create a new technology", description = "Creates a new technology with the provided details")
    public Mono<ResponseEntity<TechnologyDTO>> createTechnology(@RequestBody @Valid TechnologyDTO technologyDTO) {
        return technologyService.createTechnology(technologyDTO)
                .map(createdTechnology -> new ResponseEntity<>(createdTechnology, HttpStatus.CREATED));
    }

    @GetMapping
    @Operation(summary = "Get all technologies", description = "Retrieves all technologies")
    public Flux<TechnologyDTO> getAllTechnologies(
            @RequestParam(name = "sort", defaultValue = "asc") String sortDirection,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        return technologyService.getAllTechnologies(sortDirection, page, size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a technology by ID", description = "Retrieves a technology by its ID")
    public Mono<ResponseEntity<TechnologyDTO>> getTechnologyById(@PathVariable Long id) {
        return technologyService.getTechnologyById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/capabilities/{id}/technologies")
    @Operation(summary = "Get technologies by capability ID",
            description = "Retrieves all technologies associated with a specific capability")
    public Flux<TechnologyDTO> getTechnologiesByCapability(@PathVariable Long id) {
        return technologyService.getTechnologiesByCapabilityId(id);
    }

    @PostMapping("/capabilities/{id}/technologies")
    @Operation(summary = "Associate technologies with a capability",
            description = "Associates multiple technologies with a specific capability")
    public Mono<Void> saveCapabilityTechnologies(@PathVariable("id") Long capabilityId, @RequestBody List<Long> technologyIds) {
        return technologyService.saveCapabilityTechnologies(capabilityId, technologyIds);
    }
}
