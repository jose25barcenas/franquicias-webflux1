package com.franquicias.service;

import com.franquicias.dto.FranquiciaRequest;
import com.franquicias.dto.ProductoRequest;
import com.franquicias.dto.ProductoStockRequest;
import com.franquicias.dto.SucursalRequest;
import com.franquicias.model.Franquicia;
import com.franquicias.model.Producto;
import com.franquicias.model.Sucursal;
import com.franquicias.repository.FranquiciaRepository;
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
class FranquiciaServiceTest {

    @Mock
    private FranquiciaRepository franquiciaRepository;

    @Mock
    private SucursalRepository sucursalRepository;

    @Mock
    private ProductoRepository productoRepository;

    private FranquiciaService service;

    @BeforeEach
    void setUp() {
        service = new FranquiciaService(franquiciaRepository, sucursalRepository, productoRepository);
    }

    @Test
    void listarFranquicias_returnsAllFranquicias() {
        Franquicia f1 = Franquicia.builder().id(1L).nombre("Franquicia 1").build();
        Franquicia f2 = Franquicia.builder().id(2L).nombre("Franquicia 2").build();
        when(franquiciaRepository.findAll()).thenReturn(Flux.just(f1, f2));

        StepVerifier.create(service.listarFranquicias())
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void obtenerFranquicia_returnsFranquicia_whenExists() {
        Franquicia franquicia = Franquicia.builder().id(1L).nombre("Test").build();
        when(franquiciaRepository.findById(1L)).thenReturn(Mono.just(franquicia));

        StepVerifier.create(service.obtenerFranquicia(1L))
                .expectNext(franquicia)
                .verifyComplete();
    }

    @Test
    void obtenerFranquicia_returnsEmpty_whenNotExists() {
        when(franquiciaRepository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(service.obtenerFranquicia(99L))
                .verifyComplete();
    }

    @Test
    void crearFranquicia_savesAndReturnsFranquicia() {
        FranquiciaRequest request = new FranquiciaRequest("Nueva Franquicia");
        Franquicia savedFranquicia = Franquicia.builder().id(1L).nombre("Nueva Franquicia").build();
        when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(savedFranquicia));

        StepVerifier.create(service.crearFranquicia(request))
                .expectNext(savedFranquicia)
                .verifyComplete();
    }

    @Test
    void agregarSucursal_createsSucursal_whenFranquiciaExists() {
        FranquiciaRequest franRequest = new FranquiciaRequest("Franquicia");
        SucursalRequest request = new SucursalRequest("Sucursal Centro");
        Franquicia franquicia = Franquicia.builder().id(1L).nombre("Franquicia").build();
        Sucursal savedSucursal = Sucursal.builder().id(1L).nombre("Sucursal Centro").franquiciaId(1L).build();
        
        when(franquiciaRepository.findById(1L)).thenReturn(Mono.just(franquicia));
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(Mono.just(savedSucursal));

        StepVerifier.create(service.agregarSucursal(1L, request))
                .expectNext(savedSucursal)
                .verifyComplete();
    }

    @Test
    void agregarSucursal_returnsError_whenFranquiciaNotFound() {
        SucursalRequest request = new SucursalRequest("Sucursal");
        when(franquiciaRepository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(service.agregarSucursal(99L, request))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException && 
                        e.getMessage().equals("Franquicia no encontrada"))
                .verify();
    }

    @Test
    void agregarProducto_createsProducto_whenSucursalExists() {
        ProductoRequest request = new ProductoRequest("Producto A", 100);
        Sucursal sucursal = Sucursal.builder().id(1L).nombre("Sucursal").franquiciaId(1L).build();
        Producto savedProducto = Producto.builder().id(1L).nombre("Producto A").stock(100).sucursalId(1L).build();
        
        when(sucursalRepository.findById(1L)).thenReturn(Mono.just(sucursal));
        when(productoRepository.save(any(Producto.class))).thenReturn(Mono.just(savedProducto));

        StepVerifier.create(service.agregarProducto(1L, request))
                .expectNext(savedProducto)
                .verifyComplete();
    }

    @Test
    void actualizarStock_updatesProducto() {
        ProductoStockRequest request = new ProductoStockRequest(50);
        Producto existingProducto = Producto.builder().id(1L).nombre("Producto").stock(100).sucursalId(1L).build();
        Producto updatedProducto = Producto.builder().id(1L).nombre("Producto").stock(50).sucursalId(1L).build();
        
        when(productoRepository.findById(1L)).thenReturn(Mono.just(existingProducto));
        when(productoRepository.save(any(Producto.class))).thenReturn(Mono.just(updatedProducto));

        StepVerifier.create(service.actualizarStock(1L, request))
                .expectNextMatches(p -> p.getStock() == 50)
                .verifyComplete();
    }

    @Test
    void eliminarProducto_deletesSuccessfully() {
        when(productoRepository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(service.eliminarProducto(1L))
                .verifyComplete();
    }

    @Test
    void listarSucursales_returnsSucursalesByFranquiciaId() {
        Sucursal s1 = Sucursal.builder().id(1L).nombre("Sucursal 1").franquiciaId(1L).build();
        Sucursal s2 = Sucursal.builder().id(2L).nombre("Sucursal 2").franquiciaId(1L).build();
        when(sucursalRepository.findByFranquiciaId(1L)).thenReturn(Flux.just(s1, s2));

        StepVerifier.create(service.listarSucursales(1L))
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void listarProductos_returnsProductosBySucursalId() {
        Producto p1 = Producto.builder().id(1L).nombre("Producto 1").stock(10).sucursalId(1L).build();
        Producto p2 = Producto.builder().id(2L).nombre("Producto 2").stock(20).sucursalId(1L).build();
        when(productoRepository.findBySucursalId(1L)).thenReturn(Flux.just(p1, p2));

        StepVerifier.create(service.listarProductos(1L))
                .expectNextCount(2)
                .verifyComplete();
    }
}
