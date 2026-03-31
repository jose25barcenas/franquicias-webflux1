package com.franquicias.repository;

import com.franquicias.model.Producto;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductoRepository extends R2dbcRepository<Producto, Long> {
    Flux<Producto> findBySucursalId(Long sucursalId);
    
    @Query("SELECT * FROM productos WHERE sucursal_id = :sucursalId ORDER BY stock DESC LIMIT 1")
    Flux<Producto> findMaxStockBySucursalId(Long sucursalId);
    
    @Query("""
        SELECT p.* FROM productos p
        INNER JOIN sucursales s ON p.sucursal_id = s.id
        WHERE s.franquicia_id = :franquiciaId
        AND p.id IN (
            SELECT p2.id FROM productos p2
            WHERE p2.sucursal_id = p.sucursal_id
            ORDER BY p2.stock DESC
            LIMIT 1
        )
    """)
    Flux<Producto> findProductosMaxStockPorSucursalDeFranquicia(Long franquiciaId);
}
