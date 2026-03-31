package com.franquicias.service;

import com.franquicias.dto.*;
import com.franquicias.model.Franquicia;
import com.franquicias.model.Producto;
import com.franquicias.model.Sucursal;
import com.franquicias.repository.FranquiciaRepository;
import com.franquicias.repository.ProductoRepository;
import com.franquicias.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FranquiciaService {

    private final FranquiciaRepository franquiciaRepository;
    private final SucursalRepository sucursalRepository;
    private final ProductoRepository productoRepository;

    public Flux<Franquicia> listarFranquicias() {
        return franquiciaRepository.findAll();
    }

    public Mono<Franquicia> obtenerFranquicia(Long id) {
        return Mono.defer(() -> franquiciaRepository.findById(id));
    }

    public Mono<Franquicia> crearFranquicia(FranquiciaRequest request) {
        return Mono.just(Franquicia.builder().nombre(request.nombre()).build())
                .flatMap(franquiciaRepository::save);
    }

    public Mono<Franquicia> actualizarFranquicia(Long id, FranquiciaRequest request) {
        return buscarYActualizar(franquiciaRepository.findById(id), f -> f.setNombre(request.nombre()), franquiciaRepository::save);
    }

    public Mono<Void> eliminarFranquicia(Long id) {
        return sucursalRepository.findByFranquiciaId(id)
                .flatMap(sucursal -> 
                    productoRepository.findBySucursalId(sucursal.getId())
                            .flatMap(productoRepository::delete)
                            .then(sucursalRepository.deleteById(sucursal.getId()))
                )
                .then(franquiciaRepository.deleteById(id));
    }

    public Mono<Sucursal> agregarSucursal(Long franquiciaId, SucursalRequest request) {
        return franquiciaRepository.findById(franquiciaId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franquicia no encontrada")))
                .map(f -> Sucursal.builder().nombre(request.nombre()).franquiciaId(franquiciaId).build())
                .flatMap(sucursalRepository::save);
    }

    public Flux<Sucursal> listarSucursales(Long franquiciaId) {
        return sucursalRepository.findByFranquiciaId(franquiciaId);
    }

    public Mono<Sucursal> actualizarSucursal(Long id, SucursalRequest request) {
        return buscarYActualizar(sucursalRepository.findById(id), s -> s.setNombre(request.nombre()), sucursalRepository::save);
    }

    public Mono<Void> eliminarSucursal(Long id) {
        return productoRepository.findBySucursalId(id)
                .flatMap(productoRepository::delete)
                .then(sucursalRepository.deleteById(id));
    }

    public Mono<Producto> agregarProducto(Long sucursalId, ProductoRequest request) {
        return sucursalRepository.findById(sucursalId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Sucursal no encontrada")))
                .map(s -> Producto.builder()
                        .nombre(request.nombre())
                        .stock(request.stock())
                        .sucursalId(sucursalId)
                        .build())
                .flatMap(productoRepository::save);
    }

    public Flux<Producto> listarProductos(Long sucursalId) {
        return productoRepository.findBySucursalId(sucursalId);
    }

    public Mono<Producto> actualizarStock(Long productoId, ProductoStockRequest request) {
        return buscarYActualizar(productoRepository.findById(productoId), p -> p.setStock(request.stock()), productoRepository::save);
    }

    public Mono<Void> eliminarProducto(Long productoId) {
        return productoRepository.deleteById(productoId);
    }

    public Mono<Producto> actualizarNombreProducto(Long productoId, ProductoRequest request) {
        return buscarYActualizar(productoRepository.findById(productoId), p -> p.setNombre(request.nombre()), productoRepository::save);
    }

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

    private <T> Mono<T> buscarYActualizar(Mono<T> monoBusqueda, java.util.function.Consumer<T> updater, java.util.function.Function<T, Mono<T>> saver) {
        return monoBusqueda
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Entidad no encontrada")))
                .map(entity -> {
                    updater.accept(entity);
                    return entity;
                })
                .flatMap(saver);
    }
}
