package com.franquicias.dto;

public record ProductoMaxStockResponse(
    String nombreProducto,
    Integer stock,
    String nombreSucursal,
    Long sucursalId
) {}
