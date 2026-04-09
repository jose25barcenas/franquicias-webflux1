package com.franquicias.service;

import com.franquicias.dto.SucursalRequest;
import com.franquicias.dto.SucursalResponse;
import com.franquicias.model.Sucursal;
import com.franquicias.repository.FranquiciaRepository;
import com.franquicias.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SucursalService implements ISucursalService {

    private final FranquiciaRepository franquiciaRepository;
    private final SucursalRepository sucursalRepository;

    @Override
    public Flux<SucursalResponse> listarSucursales(Long franquiciaId) {
        return sucursalRepository.findByFranquiciaId(franquiciaId)
                .map(this::toResponse);
    }

    @Override
    public Mono<SucursalResponse> obtenerSucursal(Long id) {
        return Mono.defer(() -> sucursalRepository.findById(id))
                .map(this::toResponse);
    }

    @Override
    public Mono<SucursalResponse> agregarSucursal(Long franquiciaId, SucursalRequest request) {
        return franquiciaRepository.findById(franquiciaId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franquicia not found")))
                .map(f -> new Sucursal(null, request.nombre(), franquiciaId))
                .flatMap(sucursalRepository::save)
                .map(this::toResponse);
    }

    @Override
    public Mono<SucursalResponse> actualizarSucursal(Long id, SucursalRequest request) {
        return sucursalRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Sucursal not found")))
                .map(s -> {
                    s.setNombre(request.nombre());
                    return s;
                })
                .flatMap(sucursalRepository::save)
                .map(this::toResponse);
    }

    @Override
    public Mono<Void> eliminarSucursal(Long id) {
        return sucursalRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Sucursal not found")))
                .flatMap(sucursal -> sucursalRepository.deleteById(id));
    }

    private SucursalResponse toResponse(Sucursal s) {
        return new SucursalResponse(s.getId(), s.getNombre(), s.getFranquiciaId());
    }
}
