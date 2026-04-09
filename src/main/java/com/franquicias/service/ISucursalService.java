package com.franquicias.service;

import com.franquicias.dto.SucursalRequest;
import com.franquicias.dto.SucursalResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ISucursalService {
    Flux<SucursalResponse> listarSucursales(Long franquiciaId);
    Mono<SucursalResponse> obtenerSucursal(Long id);
    Mono<SucursalResponse> agregarSucursal(Long franquiciaId, SucursalRequest request);
    Mono<SucursalResponse> actualizarSucursal(Long id, SucursalRequest request);
    Mono<Void> eliminarSucursal(Long id);
}
