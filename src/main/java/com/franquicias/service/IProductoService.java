package com.franquicias.service;

import com.franquicias.dto.ProductoMaxStockResponse;
import com.franquicias.dto.ProductoRequest;
import com.franquicias.dto.ProductoResponse;
import com.franquicias.dto.ProductoStockRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProductoService {
    Flux<ProductoResponse> listarProductos(Long sucursalId);
    Mono<ProductoResponse> obtenerProducto(Long id);
    Mono<ProductoResponse> agregarProducto(Long sucursalId, ProductoRequest request);
    Mono<ProductoResponse> actualizarStock(Long productoId, ProductoStockRequest request);
    Mono<ProductoResponse> actualizarNombreProducto(Long productoId, ProductoRequest request);
    Mono<Void> eliminarProducto(Long productoId);
    Flux<ProductoMaxStockResponse> productoMaxStockPorFranquicia(Long franquiciaId);
}
