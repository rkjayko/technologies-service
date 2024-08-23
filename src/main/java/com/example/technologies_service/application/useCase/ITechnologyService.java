package com.example.technologies_service.application.useCase;

import com.example.technologies_service.domain.entity.dto.TechnologyDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITechnologyService {

    Mono<TechnologyDTO> createTechnology(TechnologyDTO technologyDTO);
    Flux<TechnologyDTO> getAllTechnologies(String sortDirection, int page, int size);
    Mono<TechnologyDTO> getTechnologyById(Long technologyId);
    Mono<Void> saveCapabilityTechnologies(Long capabilityId, List<Long> technologyIds);
    Flux<TechnologyDTO> getTechnologiesByCapabilityId(Long capabilityId);

}
