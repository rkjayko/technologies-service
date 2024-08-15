package com.example.technologies_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyDTO {
    private Long id;
    private String name;
    private String description;
}
