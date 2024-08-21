package com.example.technologies_service.application.mapper;

import com.example.technologies_service.domain.entity.Technology;
import com.example.technologies_service.infrastructure.adapter.in.TechnologyDTO;
import org.springframework.stereotype.Component;

@Component
public class TechnologyMapper {

    public TechnologyDTO mapToDTO(Technology technology) {
        TechnologyDTO technologyDTO = new TechnologyDTO();
        technologyDTO.setId(technology.getId());
        technologyDTO.setName(technology.getName());
        technologyDTO.setDescription(technology.getDescription());
        return technologyDTO;
    }
}
