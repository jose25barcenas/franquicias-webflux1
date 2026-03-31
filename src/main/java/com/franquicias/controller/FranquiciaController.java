package com.franquicias.controller;

import com.franquicias.dto.*;
import com.franquicias.model.Franquicia;
import com.franquicias.model.Producto;
import com.franquicias.model.Sucursal;
import com.franquicias.service.FranquiciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franquicias")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FranquiciaController {

    private final FranquiciaService franquiciaService;

    @GetMapping
    public Flux<Franquicia> listarFranquicias() {
        return franquiciaService.listarFranquicias();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Franquicia>> obtenerFranquicia(@PathVariable Long id) {
        return franquiciaService.obtenerFranquicia(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Franquicia> crearFranquicia(@Valid @RequestBody FranquiciaRequest request) {
        return franquiciaService.crearFranquicia(request);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Franquicia>> actualizarFranquicia(
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
    public Flux<Sucursal> listarSucursales(@PathVariable Long id) {
        return franquiciaService.listarSucursales(id);
    }

    @PostMapping("/{id}/sucursales")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Sucursal> agregarSucursal(
            @PathVariable Long id, @Valid @RequestBody SucursalRequest request) {
        return franquiciaService.agregarSucursal(id, request);
    }

    @PutMapping("/sucursales/{id}")
    public Mono<ResponseEntity<Sucursal>> actualizarSucursal(
            @PathVariable Long id, @Valid @RequestBody SucursalRequest request) {
        return franquiciaService.actualizarSucursal(id, request)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/sucursales/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminarSucursal(@PathVariable Long id) {
        return franquiciaService.eliminarSucursal(id);
    }

    @GetMapping("/sucursales/{id}/productos")
    public Flux<Producto> listarProductos(@PathVariable Long id) {
        return franquiciaService.listarProductos(id);
    }

    @PostMapping("/sucursales/{id}/productos")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Producto> agregarProducto(
            @PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        return franquiciaService.agregarProducto(id, request);
    }

    @PutMapping("/productos/{id}/stock")
    public Mono<ResponseEntity<Producto>> actualizarStock(
            @PathVariable Long id, @RequestBody ProductoStockRequest request) {
        return franquiciaService.actualizarStock(id, request)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/productos/{id}")
    public Mono<ResponseEntity<Producto>> actualizarProducto(
            @PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        return franquiciaService.actualizarNombreProducto(id, request)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/productos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminarProducto(@PathVariable Long id) {
        return franquiciaService.eliminarProducto(id);
    }

    @GetMapping("/{id}/productos-max-stock")
    public Flux<ProductoMaxStockResponse> productoMaxStock(@PathVariable Long id) {
        return franquiciaService.productoMaxStockPorFranquicia(id);
    }
}
