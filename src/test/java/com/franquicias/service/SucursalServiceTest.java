package com.franquicias.service;

import com.franquicias.dto.SucursalRequest;
import com.franquicias.dto.SucursalResponse;
import com.franquicias.model.Franquicia;
import com.franquicias.model.Sucursal;
import com.franquicias.repository.FranquiciaRepository;
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
class SucursalServiceTest {

    @Mock
    private FranquiciaRepository franquiciaRepository;

    @Mock
    private SucursalRepository sucursalRepository;

    private SucursalService service;

    @BeforeEach
    void setUp() {
        service = new SucursalService(franquiciaRepository, sucursalRepository);
    }

    @Test
    void listarSucursales_returnsSucursalesByFranquiciaId() {
        Sucursal s1 = new Sucursal(1L, "Sucursal 1", 1L);
        Sucursal s2 = new Sucursal(2L, "Sucursal 2", 1L);
        when(sucursalRepository.findByFranquiciaId(1L)).thenReturn(Flux.just(s1, s2));

        StepVerifier.create(service.listarSucursales(1L))
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void obtenerSucursal_returnsSucursal_whenExists() {
        Sucursal sucursal = new Sucursal(1L, "Test", 1L);
        when(sucursalRepository.findById(1L)).thenReturn(Mono.just(sucursal));

        StepVerifier.create(service.obtenerSucursal(1L))
                .expectNext(new SucursalResponse(1L, "Test", 1L))
                .verifyComplete();
    }

    @Test
    void agregarSucursal_savesAndReturnsSucursal() {
        SucursalRequest request = new SucursalRequest("Nueva Sucursal");
        Sucursal savedSucursal = new Sucursal(1L, "Nueva Sucursal", 1L);
        when(franquiciaRepository.findById(1L)).thenReturn(Mono.just(new Franquicia(1L, "Franquicia")));
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(Mono.just(savedSucursal));

        StepVerifier.create(service.agregarSucursal(1L, request))
                .expectNext(new SucursalResponse(1L, "Nueva Sucursal", 1L))
                .verifyComplete();
    }

    @Test
    void agregarSucursal_throwsError_whenFranquiciaNotFound() {
        SucursalRequest request = new SucursalRequest("Nueva Sucursal");
        when(franquiciaRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(service.agregarSucursal(1L, request))
                .expectError(com.franquicias.config.GlobalExceptionHandler.ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void actualizarSucursal_updatesAndReturnsSucursal() {
        SucursalRequest request = new SucursalRequest("Sucursal Actualizada");
        Sucursal sucursal = new Sucursal(1L, "Test", 1L);
        when(sucursalRepository.findById(1L)).thenReturn(Mono.just(sucursal));
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(Mono.just(new Sucursal(1L, "Sucursal Actualizada", 1L)));

        StepVerifier.create(service.actualizarSucursal(1L, request))
                .expectNextMatches(r -> r.nombre().equals("Sucursal Actualizada"))
                .verifyComplete();
    }

    @Test
    void eliminarSucursal_deletesSuccessfully() {
        Sucursal sucursal = new Sucursal(1L, "Test", 1L);
        when(sucursalRepository.findById(1L)).thenReturn(Mono.just(sucursal));
        when(sucursalRepository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(service.eliminarSucursal(1L))
                .verifyComplete();
    }
}