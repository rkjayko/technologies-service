package com.example.technologies_service;

import com.example.technologies_service.application.mapper.TechnologyMapper;
import com.example.technologies_service.domain.entity.dto.TechnologyDTO;
import com.example.technologies_service.domain.entity.CapabilityTechnology;
import com.example.technologies_service.domain.entity.Technology;
import com.example.technologies_service.domain.port.CapabilityTechnologyRepository;
import com.example.technologies_service.domain.port.TechnologyRepository;
import com.example.technologies_service.application.useCase.TechnologyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
public class TechnologyServiceTest {

    private static final String TECHNOLOGY_NAME = "Technology";

    private static final String TECHNOLOGY_DESCRIPTION = "Programming Language";

    private static final Long TECHNOLOGY_ID = 1L;

    private static final String TECHNOLOGY_NAME_1 = "Java";

    private static final String TECHNOLOGY_NAME_2 = "AWS";

    @MockBean
    private TechnologyRepository repository;

    @MockBean
    private CapabilityTechnologyRepository capabilityTechnologyRepository;

    @Mock
    private TechnologyMapper technologyMapper;

    private TechnologyServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TechnologyServiceImpl(repository, capabilityTechnologyRepository, technologyMapper);
    }

    @Test
    void testCreateTechnologySuccess() {
        Technology savedTechnology = new Technology(1L, TECHNOLOGY_NAME, TECHNOLOGY_DESCRIPTION);
        TechnologyDTO technologyDTO = new TechnologyDTO(null, TECHNOLOGY_NAME, TECHNOLOGY_DESCRIPTION);
        TechnologyDTO savedTechnologyDTO = new TechnologyDTO(1L, TECHNOLOGY_NAME, TECHNOLOGY_DESCRIPTION);

        when(repository.findByName(TECHNOLOGY_NAME)).thenReturn(Mono.empty());
        when(repository.save(any(Technology.class))).thenReturn(Mono.just(savedTechnology));
        when(technologyMapper.mapToDTO(savedTechnology)).thenReturn(savedTechnologyDTO);

        Mono<TechnologyDTO> result = service.createTechnology(technologyDTO);

        StepVerifier.create(result)
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
                        throwable.getMessage().equals("El nombre de la tecnología ya existe: " + TECHNOLOGY_NAME))
                .verify();
    }

    @Test
    void testGetAllTechnologiesAscending() {
        Technology tech1 = new Technology(TECHNOLOGY_ID, TECHNOLOGY_NAME_1, TECHNOLOGY_DESCRIPTION);
        Technology tech2 = new Technology(2L, TECHNOLOGY_NAME_2, TECHNOLOGY_DESCRIPTION);

        TechnologyDTO techDTO1 = new TechnologyDTO(TECHNOLOGY_ID, TECHNOLOGY_NAME_1, TECHNOLOGY_DESCRIPTION);
        TechnologyDTO techDTO2 = new TechnologyDTO(2L, TECHNOLOGY_NAME_2, TECHNOLOGY_DESCRIPTION);

        when(repository.findAllBy(any(PageRequest.class))).thenReturn(Flux.just(tech1, tech2));
        when(technologyMapper.mapToDTO(tech1)).thenReturn(techDTO1);
        when(technologyMapper.mapToDTO(tech2)).thenReturn(techDTO2);

        Flux<TechnologyDTO> result = service.getAllTechnologies("asc", 0, 2);

        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getId().equals(TECHNOLOGY_ID) &&
                        dto.getName().equals(TECHNOLOGY_NAME_1) &&
                        dto.getDescription().equals(TECHNOLOGY_DESCRIPTION))
                .expectNextMatches(dto -> dto.getId().equals(2L) &&
                        dto.getName().equals(TECHNOLOGY_NAME_2) &&
                        dto.getDescription().equals(TECHNOLOGY_DESCRIPTION))
                .verifyComplete();
    }

    @Test
    void testGetAllTechnologiesDescending() {
        Technology tech1 = new Technology(2L, TECHNOLOGY_NAME_2, TECHNOLOGY_DESCRIPTION);
        Technology tech2 = new Technology(TECHNOLOGY_ID, TECHNOLOGY_NAME, TECHNOLOGY_DESCRIPTION);

        TechnologyDTO techDTO1 = new TechnologyDTO(2L, TECHNOLOGY_NAME_2, TECHNOLOGY_DESCRIPTION);
        TechnologyDTO techDTO2 = new TechnologyDTO(TECHNOLOGY_ID, TECHNOLOGY_NAME, TECHNOLOGY_DESCRIPTION);

        when(repository.findAllBy(any(PageRequest.class)))
                .thenReturn(Flux.just(tech1, tech2));
        when(technologyMapper.mapToDTO(tech1)).thenReturn(techDTO1);
        when(technologyMapper.mapToDTO(tech2)).thenReturn(techDTO2);

        Flux<TechnologyDTO> result = service.getAllTechnologies("desc", 0, 2);

        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getId().equals(2L) && dto.getName().equals(TECHNOLOGY_NAME_2))
                .expectNextMatches(dto -> dto.getId().equals(TECHNOLOGY_ID) && dto.getName().equals(TECHNOLOGY_NAME))
                .verifyComplete();
    }

    @Test
    public void testGetTechnologyByIdSuccess() {
        Technology technology = new Technology(TECHNOLOGY_ID, TECHNOLOGY_NAME_1, TECHNOLOGY_DESCRIPTION);
        TechnologyDTO expectedDTO = new TechnologyDTO(TECHNOLOGY_ID, TECHNOLOGY_NAME_1, TECHNOLOGY_DESCRIPTION);

        when(repository.findById(TECHNOLOGY_ID)).thenReturn(Mono.just(technology));

        Mono<TechnologyDTO> technologyMono = service.getTechnologyById(TECHNOLOGY_ID);

        technologyMono
                .subscribe(actualDTO -> {
                    assertNotNull(actualDTO);
                    assertEquals(expectedDTO, actualDTO);
                });
    }

    @Test
    void getTechnologyByIdNotFound() {
        when(repository.findById(TECHNOLOGY_ID)).thenReturn(Mono.empty());

        StepVerifier.create(service.getTechnologyById(TECHNOLOGY_ID))
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("Technology not found with id: 1")
                )
                .verify();
    }

    @Test
    void saveCapabilityTechnologies() {
        Long capabilityId = 1L;
        List<Long> technologyIds = Arrays.asList(TECHNOLOGY_ID, 2L, 3L);

        when(capabilityTechnologyRepository.saveAll(anyList())).thenReturn(Flux.empty());

        StepVerifier.create(service.saveCapabilityTechnologies(capabilityId, technologyIds))
                .verifyComplete();

        verify(capabilityTechnologyRepository, times(1)).saveAll(anyList());
    }

    @Test
    void getTechnologiesByCapabilityId() {
        Long capabilityId = 1L;
        CapabilityTechnology ct1 = new CapabilityTechnology();
        ct1.setTechnologyId(TECHNOLOGY_ID);
        CapabilityTechnology ct2 = new CapabilityTechnology();
        ct2.setTechnologyId(2L);

        Technology tech1 = new Technology(TECHNOLOGY_ID, TECHNOLOGY_NAME_1, TECHNOLOGY_DESCRIPTION);
        Technology tech2 = new Technology(2L, TECHNOLOGY_NAME_2, TECHNOLOGY_DESCRIPTION);

        TechnologyDTO techDTO1 = new TechnologyDTO(TECHNOLOGY_ID, TECHNOLOGY_NAME_1, TECHNOLOGY_DESCRIPTION);
        TechnologyDTO techDTO2 = new TechnologyDTO(2L, TECHNOLOGY_NAME_2, TECHNOLOGY_DESCRIPTION);

        when(capabilityTechnologyRepository.findByCapabilityId(capabilityId)).thenReturn(Flux.just(ct1, ct2));
        when(repository.findById(TECHNOLOGY_ID)).thenReturn(Mono.just(tech1));
        when(repository.findById(2L)).thenReturn(Mono.just(tech2));

        when(technologyMapper.mapToDTO(tech1)).thenReturn(techDTO1);
        when(technologyMapper.mapToDTO(tech2)).thenReturn(techDTO2);

        StepVerifier.create(service.getTechnologiesByCapabilityId(capabilityId))
                .expectNextMatches(dto -> dto.getId().equals(TECHNOLOGY_ID) &&
                        dto.getName().equals(TECHNOLOGY_NAME_1) &&
                        dto.getDescription().equals(TECHNOLOGY_DESCRIPTION))
                .expectNextMatches(dto -> dto.getId().equals(2L) &&
                        dto.getName().equals(TECHNOLOGY_NAME_2) &&
                        dto.getDescription().equals(TECHNOLOGY_DESCRIPTION))
                .verifyComplete();
    }

}
