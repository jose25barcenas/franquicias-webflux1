package com.franquicias.service;

import com.franquicias.dto.ProductoRequest;
import com.franquicias.dto.ProductoResponse;
import com.franquicias.dto.ProductoStockRequest;
import com.franquicias.model.Producto;
import com.franquicias.model.Sucursal;
import com.franquicias.repository.ProductoRepository;
import com.franquicias.repository.SucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private SucursalRepository sucursalRepository;

    private ProductoService service;

    @BeforeEach
    void setUp() {
        service = new ProductoService(productoRepository, sucursalRepository);
    }

    @Test
    void listarProductos_returnsProductosBySucursalId() {
        Producto p1 = new Producto(1L, "Producto 1", 10, 1L);
        Producto p2 = new Producto(2L, "Producto 2", 20, 1L);
        when(productoRepository.findBySucursalId(1L)).thenReturn(Flux.just(p1, p2));

        StepVerifier.create(service.listarProductos(1L))
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void obtenerProducto_returnsProducto_whenExists() {
        Producto producto = new Producto(1L, "Test", 10, 1L);
        when(productoRepository.findById(1L)).thenReturn(Mono.just(producto));

        StepVerifier.create(service.obtenerProducto(1L))
                .expectNext(new ProductoResponse(1L, "Test", 10, 1L))
                .verifyComplete();
    }

    @Test
    void agregarProducto_savesAndReturnsProducto() {
        ProductoRequest request = new ProductoRequest("Nuevo Producto", 50);
        Producto savedProducto = new Producto(1L, "Nuevo Producto", 50, 1L);
        when(sucursalRepository.findById(1L)).thenReturn(Mono.just(new Sucursal(1L, "Sucursal", 1L)));
        when(productoRepository.save(any(Producto.class))).thenReturn(Mono.just(savedProducto));

        StepVerifier.create(service.agregarProducto(1L, request))
                .expectNext(new ProductoResponse(1L, "Nuevo Producto", 50, 1L))
                .verifyComplete();
    }

    @Test
    void agregarProducto_throwsError_whenSucursalNotFound() {
        ProductoRequest request = new ProductoRequest("Nuevo Producto", 50);
        when(sucursalRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(service.agregarProducto(1L, request))
                .expectError(com.franquicias.config.GlobalExceptionHandler.ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void actualizarStock_updatesAndReturnsProducto() {
        ProductoStockRequest request = new ProductoStockRequest(100);
        Producto producto = new Producto(1L, "Test", 10, 1L);
        when(productoRepository.findById(1L)).thenReturn(Mono.just(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(Mono.just(new Producto(1L, "Test", 100, 1L)));

        StepVerifier.create(service.actualizarStock(1L, request))
                .expectNextMatches(r -> r.stock().equals(100))
                .verifyComplete();
    }

    @Test
    void actualizarStock_throwsError_whenProductoNotFound() {
        ProductoStockRequest request = new ProductoStockRequest(100);
        when(productoRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(service.actualizarStock(1L, request))
                .expectError(com.franquicias.config.GlobalExceptionHandler.ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void actualizarNombreProducto_updatesAndReturnsProducto() {
        ProductoRequest request = new ProductoRequest("Producto Actualizado", 10);
        Producto producto = new Producto(1L, "Test", 10, 1L);
        when(productoRepository.findById(1L)).thenReturn(Mono.just(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(Mono.just(new Producto(1L, "Producto Actualizado", 10, 1L)));

        StepVerifier.create(service.actualizarNombreProducto(1L, request))
                .expectNextMatches(r -> r.nombre().equals("Producto Actualizado"))
                .verifyComplete();
    }

    @Test
    void eliminarProducto_deletesSuccessfully() {
        Producto producto = new Producto(1L, "Test", 10, 1L);
        when(productoRepository.findById(1L)).thenReturn(Mono.just(producto));
        when(productoRepository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(service.eliminarProducto(1L))
                .verifyComplete();
    }
}