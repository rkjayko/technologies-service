package com.example.technologies_service.domain.repository;

import com.example.technologies_service.domain.entity.Technology;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TechnologyRepository extends ReactiveCrudRepository<Technology, Long> {
    Mono<Technology> findByName(String name);
    Flux<Technology> findAllBy(Pageable pageable);
}
