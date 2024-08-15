package com.example.technologies_service;

import com.example.technologies_service.dto.TechnologyDTO;
import com.example.technologies_service.entity.Technology;
import com.example.technologies_service.repository.TechnologyRepository;
import com.example.technologies_service.service.TechnologyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
public class TechnologyServiceTest {

    private static final String TECHNOLOGY_NAME = "Technology";

    private static final String TECHNOLOGY_DESCRIPTION = "Technology Description";

    private static final Long TECHNOLOGY_ID = 1L;

    @MockBean
    private TechnologyRepository repository;

    private TechnologyService service;

    @BeforeEach
    void setUp() {
        service = new TechnologyService(repository);
    }

    @Test
    void testCreateTechnology() {

        Technology tech = new Technology(TECHNOLOGY_ID, TECHNOLOGY_NAME, TECHNOLOGY_DESCRIPTION);

        when(repository.save(any(Technology.class))).thenReturn(Mono.just(tech));

        TechnologyDTO technologyDTO = new TechnologyDTO(null, TECHNOLOGY_NAME, TECHNOLOGY_DESCRIPTION);
        StepVerifier.create(service.createTechnology(technologyDTO))
                .expectNextMatches(savedTech ->
                        savedTech.getId().equals(TECHNOLOGY_ID) &&
                                savedTech.getName().equals(TECHNOLOGY_NAME) &&
                                savedTech.getDescription().equals(TECHNOLOGY_DESCRIPTION))
                .verifyComplete();
    }
}
