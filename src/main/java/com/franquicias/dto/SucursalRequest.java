package com.franquicias.dto;

public record SucursalRequest(String nombre) {
    public SucursalRequest {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
    }
}
