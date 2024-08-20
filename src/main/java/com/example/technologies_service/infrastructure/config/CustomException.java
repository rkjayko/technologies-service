package com.example.technologies_service.infrastructure.config;

public class CustomException {

    public class TechnologyNotFoundException extends RuntimeException {
        public TechnologyNotFoundException(Long id) {
            super("Technology not found with id: " + id);
        }
    }

    public static class TechnologyAlreadyExistsException extends RuntimeException {
        public TechnologyAlreadyExistsException(String name) {
            super("El nombre de la tecnología ya existe: " + name);
        }
    }
}
