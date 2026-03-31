package com.franquicias.repository;

import com.franquicias.model.Sucursal;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SucursalRepository extends R2dbcRepository<Sucursal, Long> {
    Flux<Sucursal> findByFranquiciaId(Long franquiciaId);
}
