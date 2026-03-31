package com.franquicias.repository;

import com.franquicias.model.Franquicia;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface FranquiciaRepository extends R2dbcRepository<Franquicia, Long> {
    Flux<Franquicia> findByNombreContainingIgnoreCase(String nombre);
}
