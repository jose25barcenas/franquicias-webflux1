package com.franquicias.dto;

public record ProductoRequest(String nombre, Integer stock) {
    public ProductoRequest {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
    }
}
