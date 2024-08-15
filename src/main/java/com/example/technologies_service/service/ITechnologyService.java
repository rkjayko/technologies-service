package com.example.technologies_service.service;

import com.example.technologies_service.dto.TechnologyDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITechnologyService {

    Mono<TechnologyDTO> createTechnology(TechnologyDTO technologyDTO);
    Flux<TechnologyDTO> getAllTechnologies(String sortDirection, int page, int size);
}
