package com.franquicias.dto;

public record ProductoStockRequest(Integer stock) {
    public ProductoStockRequest {
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }
}
