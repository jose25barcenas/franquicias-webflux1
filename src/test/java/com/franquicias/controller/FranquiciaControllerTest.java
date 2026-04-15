package com.franquicias.controller;

import com.franquicias.dto.*;
import com.franquicias.service.IFranquiciaService;
import com.franquicias.service.ISucursalService;
import com.franquicias.service.IProductoService;
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
    private IFranquiciaService franquiciaService;

    @MockBean
    private ISucursalService sucursalService;

    @MockBean
    private IProductoService productoService;

    @Test
    void listarFranquicias_returnsAllFranquicias() {
        when(franquiciaService.listarFranquicias())
                .thenReturn(Flux.just(
                        new FranquiciaResponse(1L, "Franquicia 1"),
                        new FranquiciaResponse(2L, "Franquicia 2")
                ));

        webTestClient.get().uri("/api/franquicias")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(FranquiciaResponse.class)
                .hasSize(2);
    }

    @Test
    void obtenerFranquicia_returnsFranquicia_whenExists() {
        when(franquiciaService.obtenerFranquicia(1L))
                .thenReturn(Mono.just(new FranquiciaResponse(1L, "Test")));

        webTestClient.get().uri("/api/franquicias/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(FranquiciaResponse.class);
    }

    @Test
    void crearFranquicia_createsAndReturnsFranquicia() {
        when(franquiciaService.crearFranquicia(any(FranquiciaRequest.class)))
                .thenReturn(Mono.just(new FranquiciaResponse(1L, "Nueva")));

        webTestClient.post().uri("/api/franquicias")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"nombre\":\"Nueva\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody(FranquiciaResponse.class);
    }

    @Test
    void agregarSucursal_createsAndReturnsSucursal() {
        when(sucursalService.agregarSucursal(eq(1L), any(SucursalRequest.class)))
                .thenReturn(Mono.just(new SucursalResponse(1L, "Sucursal", 1L)));

        webTestClient.post().uri("/api/franquicias/1/sucursales")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"nombre\":\"Sucursal\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody(SucursalResponse.class);
    }

    @Test
    void agregarProducto_createsAndReturnsProducto() {
        when(productoService.agregarProducto(eq(1L), any(ProductoRequest.class)))
                .thenReturn(Mono.just(new ProductoResponse(1L, "Producto", 100, 1L)));

        webTestClient.post().uri("/api/franquicias/sucursales/1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"nombre\":\"Producto\",\"stock\":100}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ProductoResponse.class);
    }

    @Test
    void actualizarStock_updatesAndReturnsProducto() {
        when(productoService.actualizarStock(eq(1L), any(ProductoStockRequest.class)))
                .thenReturn(Mono.just(new ProductoResponse(1L, "Producto", 50, 1L)));

        webTestClient.put().uri("/api/franquicias/productos/1/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"stock\":50}")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductoResponse.class);
    }

    @Test
    void eliminarProducto_deletesSuccessfully() {
        when(productoService.eliminarProducto(1L)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/api/franquicias/productos/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void listarSucursales_returnsSucursales() {
        when(sucursalService.listarSucursales(1L))
                .thenReturn(Flux.just(new SucursalResponse(1L, "Sucursal 1", 1L)));

        webTestClient.get().uri("/api/franquicias/1/sucursales")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(SucursalResponse.class)
                .hasSize(1);
    }

    @Test
    void listarProductos_returnsProductos() {
        when(productoService.listarProductos(1L))
                .thenReturn(Flux.just(new ProductoResponse(1L, "Producto 1", 10, 1L)));

        webTestClient.get().uri("/api/franquicias/sucursales/1/productos")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductoResponse.class)
                .hasSize(1);
    }

    @Test
    void productoMaxStock_returnsMaxStockPerSucursal() {
        when(productoService.productoMaxStockPorFranquicia(1L))
                .thenReturn(Flux.just(new ProductoMaxStockResponse("Producto 1", 100, "Sucursal 1", 1L)));

        webTestClient.get().uri("/api/franquicias/1/productos-max-stock")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductoMaxStockResponse.class)
                .hasSize(1);
    }

    @Test
    void obtenerFranquiciaCompleta_returnsFranquiciaConSucursalesYProductos() {
        FranquiciaCompletaResponse response = new FranquiciaCompletaResponse(1L, "Franquicia 1",
                java.util.List.of(new FranquiciaCompletaResponse.SucursalConProductos(1L, "Sucursal 1",
                        java.util.List.of(new ProductoResponse(1L, "Producto 1", 10, 1L)))));
        when(franquiciaService.obtenerFranquiciaCompleta(1L)).thenReturn(Mono.just(response));

        webTestClient.get().uri("/api/franquicias/1/completa")
                .exchange()
                .expectStatus().isOk()
                .expectBody(FranquiciaCompletaResponse.class);
    }
}
