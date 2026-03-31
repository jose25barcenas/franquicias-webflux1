package com.franquicias.dto;

public record ProductoRequest(String nombre, Integer stock) {
    public ProductoRequest {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }
}
