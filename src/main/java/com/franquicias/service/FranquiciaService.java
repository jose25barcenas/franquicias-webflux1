package com.franquicias.service;

import com.franquicias.dto.FranquiciaRequest;
import com.franquicias.dto.FranquiciaResponse;
import com.franquicias.model.Franquicia;
import com.franquicias.repository.FranquiciaRepository;
import com.franquicias.repository.ProductoRepository;
import com.franquicias.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franquicia not found")))
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

    private FranquiciaResponse toResponse(Franquicia f) {
        return new FranquiciaResponse(f.getId(), f.getNombre());
    }
}
