package com.franquicias.service;

import com.franquicias.dto.FranquiciaRequest;
import com.franquicias.dto.FranquiciaResponse;
import com.franquicias.model.Franquicia;
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
        Franquicia f1 = new Franquicia(1L, "Franquicia 1");
        Franquicia f2 = new Franquicia(2L, "Franquicia 2");
        when(franquiciaRepository.findAll()).thenReturn(Flux.just(f1, f2));

        StepVerifier.create(service.listarFranquicias())
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void obtenerFranquicia_returnsFranquicia_whenExists() {
        Franquicia franquicia = new Franquicia(1L, "Test");
        when(franquiciaRepository.findById(1L)).thenReturn(Mono.just(franquicia));

        StepVerifier.create(service.obtenerFranquicia(1L))
                .expectNext(new FranquiciaResponse(1L, "Test"))
                .verifyComplete();
    }

    @Test
    void crearFranquicia_savesAndReturnsFranquicia() {
        FranquiciaRequest request = new FranquiciaRequest("Nueva Franquicia");
        Franquicia savedFranquicia = new Franquicia(1L, "Nueva Franquicia");
        when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(savedFranquicia));

        StepVerifier.create(service.crearFranquicia(request))
                .expectNext(new FranquiciaResponse(1L, "Nueva Franquicia"))
                .verifyComplete();
    }

    @Test
    void actualizarFranquicia_updatesAndReturnsFranquicia() {
        FranquiciaRequest request = new FranquiciaRequest("Franquicia Actualizada");
        Franquicia franquicia = new Franquicia(1L, "Test");
        when(franquiciaRepository.findById(1L)).thenReturn(Mono.just(franquicia));
        when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(new Franquicia(1L, "Franquicia Actualizada")));

        StepVerifier.create(service.actualizarFranquicia(1L, request))
                .expectNextMatches(r -> r.nombre().equals("Franquicia Actualizada"))
                .verifyComplete();
    }

    @Test
    void eliminarFranquicia_deletesSuccessfully() {
        when(sucursalRepository.findByFranquiciaId(1L)).thenReturn(Flux.empty());
        when(franquiciaRepository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(service.eliminarFranquicia(1L))
                .verifyComplete();
    }

    @Test
    void actualizarFranquicia_throwsError_whenNotFound() {
        when(franquiciaRepository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(service.actualizarFranquicia(99L, new FranquiciaRequest("X")))
                .expectError(com.franquicias.config.GlobalExceptionHandler.ResourceNotFoundException.class)
                .verify();
    }
}
