package com.example.technologies_service.infrastructure.adapter.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.example.technologies_service.infrastructure.utils.ValidationMessages.NAME_NOT_BLANK;
import static com.example.technologies_service.infrastructure.utils.ValidationMessages.NAME_SIZE;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyDTO {
    private Long id;

    @NotBlank(message = NAME_NOT_BLANK)
    @Size(max = 50, message = NAME_SIZE)
    private String name;

    @NotBlank(message = NAME_NOT_BLANK)
    @Size(max = 90, message = NAME_SIZE)
    private String description;
}
