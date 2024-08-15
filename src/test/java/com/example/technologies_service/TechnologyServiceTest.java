package com.example.technologies_service;

import com.example.technologies_service.dto.TechnologyDTO;
import com.example.technologies_service.entity.Technology;
import com.example.technologies_service.repository.TechnologyRepository;
import com.example.technologies_service.service.TechnologyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
public class TechnologyServiceTest {

    private static final String TECHNOLOGY_NAME = "Technology";

    private static final String TECHNOLOGY_DESCRIPTION = "Programming Language";

    private static final Long TECHNOLOGY_ID = 1L;

    @MockBean
    private TechnologyRepository repository;

    private TechnologyService service;

    @BeforeEach
    void setUp() {
        service = new TechnologyService(repository);
    }

    @Test
    void testCreateTechnologySuccess() {
        Technology savedTechnology = new Technology(1L, TECHNOLOGY_NAME, TECHNOLOGY_DESCRIPTION);

        when(repository.findByName(TECHNOLOGY_NAME)).thenReturn(Mono.empty());
        when(repository.save(any(Technology.class))).thenReturn(Mono.just(savedTechnology));

        TechnologyDTO technologyDTO = new TechnologyDTO(null, TECHNOLOGY_NAME, TECHNOLOGY_DESCRIPTION);

        StepVerifier.create(service.createTechnology(technologyDTO))
                .expectNextMatches(dto ->
                        dto.getId().equals(1L) &&
                                dto.getName().equals(TECHNOLOGY_NAME) &&
                                dto.getDescription().equals(TECHNOLOGY_DESCRIPTION))
                .verifyComplete();
    }

    @Test
    void testCreateTechnologyNameExists() {
        Technology existingTechnology = new Technology(1L, TECHNOLOGY_NAME, TECHNOLOGY_DESCRIPTION);

        when(repository.findByName(TECHNOLOGY_NAME)).thenReturn(Mono.just(existingTechnology));

        TechnologyDTO technologyDTO = new TechnologyDTO(null, TECHNOLOGY_NAME, TECHNOLOGY_DESCRIPTION);

        StepVerifier.create(service.createTechnology(technologyDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("El nombre de la tecnología ya existe"))
                .verify();
    }

    @Test
    void testGetAllTechnologiesAscending() {
        Technology tech1 = new Technology(TECHNOLOGY_ID, "Java", TECHNOLOGY_DESCRIPTION);
        Technology tech2 = new Technology(2L, "Python", TECHNOLOGY_DESCRIPTION);

        when(repository.findAllBy(any(PageRequest.class)))
                .thenReturn(Flux.just(tech1, tech2));

        Flux<TechnologyDTO> result = service.getAllTechnologies("asc", 0, 2);

        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getName().equals("Java"))
                .expectNextMatches(dto -> dto.getName().equals("Python"))
                .verifyComplete();
    }

    @Test
    void testGetAllTechnologiesDescending() {
        Technology tech1 = new Technology(TECHNOLOGY_ID, "Python", TECHNOLOGY_DESCRIPTION);
        Technology tech2 = new Technology(2L, "Java", TECHNOLOGY_DESCRIPTION);

        when(repository.findAllBy(any(PageRequest.class)))
                .thenReturn(Flux.just(tech1, tech2));

        Flux<TechnologyDTO> result = service.getAllTechnologies("desc", 0, 2);

        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getName().equals("Python"))
                .expectNextMatches(dto -> dto.getName().equals("Java"))
                .verifyComplete();
    }
}
