package com.example.technologies_service.service;

import com.example.technologies_service.dto.TechnologyDTO;
import com.example.technologies_service.entity.Technology;
import com.example.technologies_service.repository.TechnologyRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TechnologyService {

    private final TechnologyRepository repository;

    public TechnologyService(TechnologyRepository repository) {
        this.repository = repository;
    }

    public Mono<TechnologyDTO> createTechnology(TechnologyDTO technologyDTO) {
        Technology technology = new Technology(null, technologyDTO.getName(), technologyDTO.getDescription());
        return repository.save(technology)
                .map(this::mapToDTO);
    }

    private TechnologyDTO mapToDTO(Technology technology) {
        TechnologyDTO technologyDTO = new TechnologyDTO();
        technologyDTO.setId(technology.getId());
        technologyDTO.setName(technology.getName());
        technologyDTO.setDescription(technology.getDescription());
        return technologyDTO;
    }
}