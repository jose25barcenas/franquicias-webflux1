package com.franquicias.service;

import com.franquicias.config.GlobalExceptionHandler.ResourceNotFoundException;
import com.franquicias.dto.FranquiciaCompletaResponse;
import com.franquicias.dto.FranquiciaRequest;
import com.franquicias.dto.FranquiciaResponse;
import com.franquicias.dto.ProductoResponse;
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

import java.util.List;

/**
 * Service implementation for managing franchises (franquicias).
 * Handles business logic for CRUD operations and retrieving complete franchise data.
 */
@Service
@RequiredArgsConstructor
public class FranquiciaService implements IFranquiciaService {

    private final FranquiciaRepository franquiciaRepository;
    private final SucursalRepository sucursalRepository;
    private final ProductoRepository productoRepository;

    @Override
    public Flux<FranquiciaResponse> listarFranquicias() {
        return franquiciaRepository.findAll()
                .map(this::toResponse);
    }

    @Override
    public Mono<FranquiciaResponse> obtenerFranquicia(Long id) {
        return Mono.defer(() -> franquiciaRepository.findById(id))
                .map(this::toResponse);
    }

    @Override
    public Mono<FranquiciaResponse> crearFranquicia(FranquiciaRequest request) {
        Franquicia franquicia = new Franquicia(null, request.nombre());
        return franquiciaRepository.save(franquicia)
                .map(this::toResponse);
    }

    @Override
    public Mono<FranquiciaResponse> actualizarFranquicia(Long id, FranquiciaRequest request) {
        return franquiciaRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franquicia not found with id: " + id)))
                .map(f -> {
                    f.setNombre(request.nombre());
                    return f;
                })
                .flatMap(franquiciaRepository::save)
                .map(this::toResponse);
    }

    @Override
    public Mono<Void> eliminarFranquicia(Long id) {
        return sucursalRepository.findByFranquiciaId(id)
                .flatMap(sucursal ->
                    productoRepository.findBySucursalId(sucursal.getId())
                            .flatMap(productoRepository::delete)
                            .then(sucursalRepository.deleteById(sucursal.getId()))
                )
                .then(franquiciaRepository.deleteById(id));
    }

    @Override
    public Mono<FranquiciaCompletaResponse> obtenerFranquiciaCompleta(Long id) {
        return franquiciaRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franquicia not found with id: " + id)))
                .flatMap(franquicia ->
                    sucursalRepository.findByFranquiciaId(franquicia.getId())
                        .flatMap(sucursal ->
                            productoRepository.findBySucursalId(sucursal.getId())
                                .map(this::toProductoResponse)
                                .collectList()
                                .map(productos -> new FranquiciaCompletaResponse.SucursalConProductos(
                                    sucursal.getId(), sucursal.getNombre(), productos))
                        )
                        .collectList()
                        .map(sucursales -> new FranquiciaCompletaResponse(
                            franquicia.getId(), franquicia.getNombre(), sucursales))
                );
    }

    private FranquiciaResponse toResponse(Franquicia f) {
        return new FranquiciaResponse(f.getId(), f.getNombre());
    }

    private ProductoResponse toProductoResponse(Producto p) {
        return new ProductoResponse(p.getId(), p.getNombre(), p.getStock(), p.getSucursalId());
    }
}
