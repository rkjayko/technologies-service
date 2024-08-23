package com.example.technologies_service.application.mapper;

import com.example.technologies_service.domain.entity.Technology;
import com.example.technologies_service.domain.entity.dto.TechnologyDTO;
import org.springframework.stereotype.Component;

@Component
public class TechnologyMapper {

    public TechnologyDTO mapToDTO(Technology technology) {
        return new TechnologyDTO(technology.getId(), technology.getName(), technology.getDescription());
    }
}
