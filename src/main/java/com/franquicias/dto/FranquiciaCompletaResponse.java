package com.franquicias.dto;

import java.util.List;

public record FranquiciaCompletaResponse(
    Long id,
    String nombre,
    List<SucursalConProductos> sucursales
) {
    public record SucursalConProductos(
        Long id,
        String nombre,
        List<ProductoResponse> productos
    ) {}
}