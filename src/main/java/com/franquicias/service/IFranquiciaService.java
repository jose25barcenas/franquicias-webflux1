package com.franquicias.service;

import com.franquicias.dto.FranquiciaRequest;
import com.franquicias.dto.FranquiciaResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IFranquiciaService {
    Flux<FranquiciaResponse> listarFranquicias();
    Mono<FranquiciaResponse> obtenerFranquicia(Long id);
    Mono<FranquiciaResponse> crearFranquicia(FranquiciaRequest request);
    Mono<FranquiciaResponse> actualizarFranquicia(Long id, FranquiciaRequest request);
    Mono<Void> eliminarFranquicia(Long id);
}
