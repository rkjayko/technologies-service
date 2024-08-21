package com.example.technologies_service.application.useCase;

import com.example.technologies_service.application.mapper.TechnologyMapper;
import com.example.technologies_service.domain.entity.CapabilityTechnology;
import com.example.technologies_service.domain.entity.Technology;
import com.example.technologies_service.domain.exception.CustomException;
import com.example.technologies_service.domain.repository.CapabilityTechnologyRepository;
import com.example.technologies_service.domain.repository.TechnologyRepository;
import com.example.technologies_service.infrastructure.adapter.in.TechnologyDTO;
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
    private final TechnologyMapper technologyMapper;

    public TechnologyServiceImpl(TechnologyRepository repository, CapabilityTechnologyRepository capabilityTechnologyRepository, TechnologyMapper technologyMapper) {
        this.repository = repository;
        this.capabilityTechnologyRepository = capabilityTechnologyRepository;
        this.technologyMapper = technologyMapper;
    }

    @Override
    public Mono<TechnologyDTO> createTechnology(TechnologyDTO technologyDTO) {
        return repository.findByName(technologyDTO.getName())
                .flatMap(existingTech -> Mono.error(new CustomException.TechnologyAlreadyExistsException(technologyDTO.getName())))
                .then(Mono.defer(() -> {
                    Technology technology = new Technology(null, technologyDTO.getName(), technologyDTO.getDescription());
                    return repository.save(technology)
                            .map(technologyMapper::mapToDTO);
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
                .map(technologyMapper::mapToDTO);
    }

    @Override
    public Mono<TechnologyDTO> getTechnologyById(Long id) {
        return repository.findById(id)
                .map(technologyMapper::mapToDTO)
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
                        .map(technologyMapper::mapToDTO));
    }


}