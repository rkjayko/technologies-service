package com.example.technologies_service.domain.useCase;

import com.example.technologies_service.domain.dto.TechnologyDTO;
import com.example.technologies_service.domain.entity.CapabilityTechnology;
import com.example.technologies_service.domain.entity.Technology;
import com.example.technologies_service.infrastructure.adapter.CapabilityTechnologyRepository;
import com.example.technologies_service.infrastructure.adapter.TechnologyRepository;
import com.example.technologies_service.infrastructure.config.CustomException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TechnologyServiceImpl implements ITechnologyService {

    private final TechnologyRepository repository;
    private final CapabilityTechnologyRepository capabilityTechnologyRepository;

    public TechnologyServiceImpl(TechnologyRepository repository, CapabilityTechnologyRepository capabilityTechnologyRepository) {
        this.repository = repository;
        this.capabilityTechnologyRepository = capabilityTechnologyRepository;
    }

    @Override
    public Mono<TechnologyDTO> createTechnology(TechnologyDTO technologyDTO) {
        return repository.findByName(technologyDTO.getName())
                .flatMap(existingTech -> Mono.error(new CustomException.TechnologyAlreadyExistsException(technologyDTO.getName())))
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

    @Override
    public Mono<TechnologyDTO> getTechnologyById(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .switchIfEmpty(Mono.error(new CustomException.TechnologyNotFoundException(id)));
    }

    @Override
    public Mono<Void> saveCapabilityTechnologies(Long capabilityId, List<Long> technologyIds) {
        List<CapabilityTechnology> relationships = technologyIds.stream()
                .map(technologyId -> {
                    CapabilityTechnology relationship = new CapabilityTechnology();
                    relationship.setCapabilityId(capabilityId);
                    relationship.setTechnologyId(technologyId);
                    return relationship;
                })
                .collect(Collectors.toList());

        return capabilityTechnologyRepository.saveAll(relationships).then();
    }

    @Override
    public Flux<TechnologyDTO> getTechnologiesByCapabilityId(Long capabilityId) {
        return capabilityTechnologyRepository.findByCapabilityId(capabilityId)
                .map(CapabilityTechnology::getTechnologyId)
                .distinct()
                .flatMap(technologyId -> repository.findById(technologyId)
                        .map(this::mapToDTO));
    }

    private TechnologyDTO mapToDTO(Technology technology) {
        TechnologyDTO technologyDTO = new TechnologyDTO();
        technologyDTO.setId(technology.getId());
        technologyDTO.setName(technology.getName());
        technologyDTO.setDescription(technology.getDescription());
        return technologyDTO;
    }
}