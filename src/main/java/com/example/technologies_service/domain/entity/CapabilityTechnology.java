package com.example.technologies_service.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("capability_technologies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapabilityTechnology {

    @Id
    private Long id;

    @Column("id_capability")
    private Long capabilityId;

    @Column("id_technology")
    private Long technologyId;

}