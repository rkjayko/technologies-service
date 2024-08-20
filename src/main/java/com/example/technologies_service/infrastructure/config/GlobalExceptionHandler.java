package com.example.technologies_service.infrastructure.config;

import com.example.technologies_service.domain.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.TechnologyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ResponseEntity<ErrorResponseDTO>> handleTechnologyNotFound(CustomException.TechnologyNotFoundException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                ex.getMessage());
        return Mono.just(new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(CustomException.TechnologyAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ResponseEntity<ErrorResponseDTO>> handleTechnologyAlreadyExists(CustomException.TechnologyAlreadyExistsException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                ex.getMessage());
        return Mono.just(new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST));
    }
}