package com.example.technologies_service.infrastructure.adapter;

import com.example.technologies_service.domain.entity.CapabilityTechnology;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CapabilityTechnologyRepository extends ReactiveCrudRepository<CapabilityTechnology, Long> {
    Flux<CapabilityTechnology> findByCapabilityId(Long capabilityId);
}
