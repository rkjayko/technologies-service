package com.example.technologies_service.repository;

import com.example.technologies_service.entity.Technology;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnologyRepository extends ReactiveCrudRepository<Technology, Long> {
}
