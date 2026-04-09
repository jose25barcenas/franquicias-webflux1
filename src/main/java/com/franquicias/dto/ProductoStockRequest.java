package com.franquicias.dto;

public record ProductoStockRequest(Integer stock) {
    public ProductoStockRequest {
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
    }
}
