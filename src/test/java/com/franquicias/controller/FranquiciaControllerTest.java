package com.franquicias.controller;

import com.franquicias.dto.FranquiciaRequest;
import com.franquicias.dto.ProductoRequest;
import com.franquicias.dto.ProductoStockRequest;
import com.franquicias.dto.SucursalRequest;
import com.franquicias.model.Franquicia;
import com.franquicias.model.Producto;
import com.franquicias.model.Sucursal;
import com.franquicias.service.FranquiciaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(FranquiciaController.class)
class FranquiciaControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private FranquiciaService franquiciaService;

    @Test
    void listarFranquicias_returnsAllFranquicias() {
        Franquicia f1 = Franquicia.builder().id(1L).nombre("Franquicia 1").build();
        Franquicia f2 = Franquicia.builder().id(2L).nombre("Franquicia 2").build();
        when(franquiciaService.listarFranquicias()).thenReturn(Flux.just(f1, f2));

        webTestClient.get().uri("/api/franquicias")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Franquicia.class)
                .hasSize(2);
    }

    @Test
    void obtenerFranquicia_returnsFranquicia_whenExists() {
        Franquicia franquicia = Franquicia.builder().id(1L).nombre("Test").build();
        when(franquiciaService.obtenerFranquicia(1L)).thenReturn(Mono.just(franquicia));

        webTestClient.get().uri("/api/franquicias/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Franquicia.class);
    }

    @Test
    void crearFranquicia_createsAndReturnsFranquicia() {
        Franquicia savedFranquicia = Franquicia.builder().id(1L).nombre("Nueva").build();
        when(franquiciaService.crearFranquicia(any(FranquiciaRequest.class))).thenReturn(Mono.just(savedFranquicia));

        webTestClient.post().uri("/api/franquicias")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"nombre\":\"Nueva\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Franquicia.class);
    }

    @Test
    void agregarSucursal_createsAndReturnsSucursal() {
        Sucursal savedSucursal = Sucursal.builder().id(1L).nombre("Sucursal").franquiciaId(1L).build();
        when(franquiciaService.agregarSucursal(eq(1L), any(SucursalRequest.class))).thenReturn(Mono.just(savedSucursal));

        webTestClient.post().uri("/api/franquicias/1/sucursales")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"nombre\":\"Sucursal\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Sucursal.class);
    }

    @Test
    void agregarProducto_createsAndReturnsProducto() {
        Producto savedProducto = Producto.builder().id(1L).nombre("Producto").stock(100).sucursalId(1L).build();
        when(franquiciaService.agregarProducto(eq(1L), any(ProductoRequest.class))).thenReturn(Mono.just(savedProducto));

        webTestClient.post().uri("/api/franquicias/sucursales/1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"nombre\":\"Producto\",\"stock\":100}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Producto.class);
    }

    @Test
    void actualizarStock_updatesAndReturnsProducto() {
        Producto updatedProducto = Producto.builder().id(1L).nombre("Producto").stock(50).sucursalId(1L).build();
        when(franquiciaService.actualizarStock(eq(1L), any(ProductoStockRequest.class))).thenReturn(Mono.just(updatedProducto));

        webTestClient.put().uri("/api/franquicias/productos/1/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"stock\":50}")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Producto.class);
    }

    @Test
    void eliminarProducto_deletesSuccessfully() {
        when(franquiciaService.eliminarProducto(1L)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/api/franquicias/productos/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void listarSucursales_returnsSucursales() {
        Sucursal s1 = Sucursal.builder().id(1L).nombre("Sucursal 1").franquiciaId(1L).build();
        when(franquiciaService.listarSucursales(1L)).thenReturn(Flux.just(s1));

        webTestClient.get().uri("/api/franquicias/1/sucursales")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Sucursal.class)
                .hasSize(1);
    }

    @Test
    void listarProductos_returnsProductos() {
        Producto p1 = Producto.builder().id(1L).nombre("Producto 1").stock(10).sucursalId(1L).build();
        when(franquiciaService.listarProductos(1L)).thenReturn(Flux.just(p1));

        webTestClient.get().uri("/api/franquicias/sucursales/1/productos")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Producto.class)
                .hasSize(1);
    }
}
