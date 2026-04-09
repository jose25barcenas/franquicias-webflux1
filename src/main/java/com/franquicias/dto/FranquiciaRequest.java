package com.franquicias.dto;

public record FranquiciaRequest(String nombre) {
    public FranquiciaRequest {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
    }
}
