package com.example.technologies_service.service;

import com.example.technologies_service.dto.TechnologyDTO;
import com.example.technologies_service.entity.Technology;
import com.example.technologies_service.repository.TechnologyRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TechnologyService implements ITechnologyService{

    private final TechnologyRepository repository;

    public TechnologyService(TechnologyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<TechnologyDTO> createTechnology(TechnologyDTO technologyDTO) {

        return repository.findByName(technologyDTO.getName())
                .flatMap(existingTech -> Mono.error(new RuntimeException("El nombre de la tecnología ya existe")))
                .then(Mono.defer(() -> {
                    Technology technology = new Technology(null, technologyDTO.getName(), technologyDTO.getDescription());
                    return repository.save(technology)
                            .map(this::mapToDTO);
                }));
    }

    @Override
    public Flux<TechnologyDTO> getAllTechnologies(String sortDirection, int page, int size) {
        Sort sort = Sort.by("name");
        if ("desc".equalsIgnoreCase(sortDirection)) {
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findAllBy(pageable)
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