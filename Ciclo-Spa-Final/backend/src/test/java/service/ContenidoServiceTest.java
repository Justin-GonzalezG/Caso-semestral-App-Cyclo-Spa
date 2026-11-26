
package cl.ciclospa.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContenidoServiceTest {

    @Mock
    private ContenidoRepository repository;

    @InjectMocks
    private ContenidoService service;

    private ContenidoEducativo contenido;
    private ContenidoRequest request;

    @BeforeEach
    void setUp() {
        contenido = new ContenidoEducativo(
                "Ciclo menstrual",
                "Educacion",
                "Informacion sobre el ciclo menstrual",
                true
        );

        request = new ContenidoRequest(
                "Ciclo menstrual",
                "Educacion",
                "Informacion sobre el ciclo menstrual",
                true
        );

        System.out.println("\n======================================");
        System.out.println(" INICIANDO PRUEBA DE CONTENIDO SERVICE");
        System.out.println("======================================");
    }

    // PRUEBA 1: Listar contenidos publicados
    @Test
    void deberiaListarContenidosPublicados() {

        when(repository.findByPublicadoTrue())
                .thenReturn(List.of(contenido));

        var resultado = service.listar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Ciclo menstrual", resultado.get(0).titulo());

        verify(repository).findByPublicadoTrue();

        System.out.println("PASS [1/7]: Listar contenidos publicados");
    }

    // PRUEBA 2: Obtener contenido por ID
    @Test
    void deberiaObtenerContenidoPorId() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(contenido));

        var resultado = service.obtener(1L);

        assertNotNull(resultado);
        assertEquals("Ciclo menstrual", resultado.titulo());

        verify(repository).findById(1L);

        System.out.println("PASS [2/7]: Obtener contenido por ID");
    }

    // PRUEBA 3: Crear contenido
    @Test
    void deberiaCrearContenido() {

        when(repository.save(any(ContenidoEducativo.class)))
                .thenReturn(contenido);

        var resultado = service.crear(request);

        assertNotNull(resultado);
        assertEquals("Ciclo menstrual", resultado.titulo());
        assertEquals("Educacion", resultado.categoria());

        verify(repository).save(any(ContenidoEducativo.class));

        System.out.println("PASS [3/7]: Crear contenido educativo");
    }

    // PRUEBA 4: Actualizar contenido
    @Test
    void deberiaActualizarContenido() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(contenido));

        ContenidoRequest nuevoRequest = new ContenidoRequest(
                "Fertilidad femenina",
                "Salud",
                "Informacion educativa actualizada",
                true
        );

        when(repository.save(any(ContenidoEducativo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var resultado = service.actualizar(1L, nuevoRequest);

        assertNotNull(resultado);
        assertEquals("Fertilidad femenina", resultado.titulo());
        assertEquals("Salud", resultado.categoria());
        assertEquals(
                "Informacion educativa actualizada",
                resultado.descripcion()
        );

        verify(repository).findById(1L);
        verify(repository).save(contenido);

        System.out.println("PASS [4/7]: Actualizar contenido educativo");
    }

    // PRUEBA 5: Eliminar contenido
    @Test
    void deberiaEliminarContenido() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(contenido));

        service.eliminar(1L);

        verify(repository).findById(1L);
        verify(repository).delete(contenido);

        System.out.println("PASS [5/7]: Eliminar contenido educativo");
    }

    // PRUEBA 6: Error cuando el contenido no existe
    @Test
    void deberiaLanzarErrorCuandoContenidoNoExiste() {

        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        ResponseStatusException excepcion = assertThrows(
                ResponseStatusException.class,
                () -> service.obtener(99L)
        );

        assertEquals(HttpStatus.NOT_FOUND, excepcion.getStatusCode());

        verify(repository).findById(99L);

        System.out.println("PASS [6/7]: Verificar error HTTP 404");
        System.out.println("       Contenido inexistente correctamente detectado");
    }

    // PRUEBA 7: Eliminar espacios sobrantes
    @Test
    void deberiaEliminarEspaciosAlCrearContenido() {

        ContenidoRequest requestConEspacios = new ContenidoRequest(
                "  Ciclo menstrual  ",
                "  Educacion  ",
                "  Informacion educativa  ",
                true
        );

        when(repository.save(any(ContenidoEducativo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var resultado = service.crear(requestConEspacios);

        assertEquals("Ciclo menstrual", resultado.titulo());
        assertEquals("Educacion", resultado.categoria());
        assertEquals(
                "Informacion educativa",
                resultado.descripcion()
        );

        verify(repository).save(any(ContenidoEducativo.class));

        System.out.println("PASS [7/7]: Eliminar espacios sobrantes");
    }
}
