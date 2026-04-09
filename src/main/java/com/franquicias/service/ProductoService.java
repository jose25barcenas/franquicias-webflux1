package com.franquicias.service;

import com.franquicias.dto.ProductoMaxStockResponse;
import com.franquicias.dto.ProductoRequest;
import com.franquicias.dto.ProductoResponse;
import com.franquicias.dto.ProductoStockRequest;
import com.franquicias.model.Producto;
import com.franquicias.repository.ProductoRepository;
import com.franquicias.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductoService implements IProductoService {

    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;

    @Override
    public Flux<ProductoResponse> listarProductos(Long sucursalId) {
        return productoRepository.findBySucursalId(sucursalId)
                .map(this::toResponse);
    }

    @Override
    public Mono<ProductoResponse> obtenerProducto(Long id) {
        return Mono.defer(() -> productoRepository.findById(id))
                .map(this::toResponse);
    }

    @Override
    public Mono<ProductoResponse> agregarProducto(Long sucursalId, ProductoRequest request) {
        return sucursalRepository.findById(sucursalId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Sucursal no encontrada")))
                .map(s -> new Producto(null, request.nombre(), request.stock(), sucursalId))
                .flatMap(productoRepository::save)
                .map(this::toResponse);
    }

    @Override
    public Mono<ProductoResponse> actualizarStock(Long productoId, ProductoStockRequest request) {
        return productoRepository.findById(productoId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Producto no encontrado")))
                .map(p -> {
                    p.setStock(request.stock());
                    return p;
                })
                .flatMap(productoRepository::save)
                .map(this::toResponse);
    }

    @Override
    public Mono<ProductoResponse> actualizarNombreProducto(Long productoId, ProductoRequest request) {
        return productoRepository.findById(productoId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Producto no encontrado")))
                .map(p -> {
                    p.setNombre(request.nombre());
                    return p;
                })
                .flatMap(productoRepository::save)
                .map(this::toResponse);
    }

    @Override
    public Mono<Void> eliminarProducto(Long productoId) {
        return productoRepository.deleteById(productoId);
    }

    @Override
    public Flux<ProductoMaxStockResponse> productoMaxStockPorFranquicia(Long franquiciaId) {
        return productoRepository.findProductosMaxStockPorSucursalDeFranquicia(franquiciaId)
                .flatMap(producto ->
                    sucursalRepository.findById(producto.getSucursalId())
                            .map(sucursal -> new ProductoMaxStockResponse(
                                    producto.getNombre(),
                                    producto.getStock(),
                                    sucursal.getNombre(),
                                    sucursal.getId()
                            ))
                );
    }

    private ProductoResponse toResponse(Producto p) {
        return new ProductoResponse(p.getId(), p.getNombre(), p.getStock(), p.getSucursalId());
    }
}
