package com.example.technologies_service.domain.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.example.technologies_service.infrastructure.utils.ValidationMessages.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyDTO {
    private Long id;

    @NotBlank(message = NAME_NOT_BLANK)
    @Size(max = 50, message = NAME_SIZE)
    private String name;

    @NotBlank(message = DESCRIPTION_NOT_BLANK)
    @Size(max = 90, message = DESCRIPTION_SIZE)
    private String description;
}
