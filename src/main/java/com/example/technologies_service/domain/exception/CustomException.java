package com.example.technologies_service.domain.exception;

public class CustomException {

    public static class TechnologyNotFoundException extends RuntimeException {
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
