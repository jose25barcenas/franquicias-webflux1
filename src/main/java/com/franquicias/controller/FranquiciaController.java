package com.franquicias.controller;

import com.franquicias.dto.*;
import com.franquicias.dto.FranquiciaCompletaResponse;
import com.franquicias.service.IFranquiciaService;
import com.franquicias.service.ISucursalService;
import com.franquicias.service.IProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * REST controller for managing franchises (franquicias), branches (sucursales), and products (productos).
 * Provides endpoints for CRUD operations on all entities.
 */
@RestController
@RequestMapping("/api/franquicias")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FranquiciaController {

    private final IFranquiciaService franquiciaService;
    private final ISucursalService sucursalService;
    private final IProductoService productoService;

    @GetMapping
    public Flux<FranquiciaResponse> listarFranquicias() {
        return franquiciaService.listarFranquicias();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<FranquiciaResponse>> obtenerFranquicia(@PathVariable Long id) {
        return franquiciaService.obtenerFranquicia(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FranquiciaResponse> crearFranquicia(@Valid @RequestBody FranquiciaRequest request) {
        return franquiciaService.crearFranquicia(request);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<FranquiciaResponse>> actualizarFranquicia(
            @PathVariable Long id, @Valid @RequestBody FranquiciaRequest request) {
        return franquiciaService.actualizarFranquicia(id, request)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminarFranquicia(@PathVariable Long id) {
        return franquiciaService.eliminarFranquicia(id);
    }

    @GetMapping("/{id}/sucursales")
    public Flux<SucursalResponse> listarSucursales(@PathVariable Long id) {
        return sucursalService.listarSucursales(id);
    }

    @PostMapping("/{id}/sucursales")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SucursalResponse> agregarSucursal(
            @PathVariable Long id, @Valid @RequestBody SucursalRequest request) {
        return sucursalService.agregarSucursal(id, request);
    }

    @PutMapping("/sucursales/{id}")
    public Mono<ResponseEntity<SucursalResponse>> actualizarSucursal(
            @PathVariable Long id, @Valid @RequestBody SucursalRequest request) {
        return sucursalService.actualizarSucursal(id, request)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/sucursales/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminarSucursal(@PathVariable Long id) {
        return sucursalService.eliminarSucursal(id);
    }

    @GetMapping("/sucursales/{id}/productos")
    public Flux<ProductoResponse> listarProductos(@PathVariable Long id) {
        return productoService.listarProductos(id);
    }

    @PostMapping("/sucursales/{id}/productos")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductoResponse> agregarProducto(
            @PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        return productoService.agregarProducto(id, request);
    }

    @PutMapping("/productos/{id}/stock")
    public Mono<ResponseEntity<ProductoResponse>> actualizarStock(
            @PathVariable Long id, @RequestBody ProductoStockRequest request) {
        return productoService.actualizarStock(id, request)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/productos/{id}")
    public Mono<ResponseEntity<ProductoResponse>> actualizarProducto(
            @PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        return productoService.actualizarNombreProducto(id, request)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/productos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminarProducto(@PathVariable Long id) {
        return productoService.eliminarProducto(id);
    }

    @GetMapping("/{id}/productos-max-stock")
    public Flux<ProductoMaxStockResponse> productoMaxStock(@PathVariable Long id) {
        return productoService.productoMaxStockPorFranquicia(id);
    }

    @GetMapping("/{id}/completa")
    public Mono<FranquiciaCompletaResponse> obtenerFranquiciaCompleta(@PathVariable Long id) {
        return franquiciaService.obtenerFranquiciaCompleta(id);
    }
}
