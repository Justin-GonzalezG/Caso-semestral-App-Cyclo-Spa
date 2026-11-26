
package cl.ciclospa.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ContenidoRepositoryTest {

    @Autowired
    private ContenidoRepository repository;

    private ContenidoEducativo contenido;

    @BeforeEach
    void setUp() {

        repository.deleteAll();

        contenido = new ContenidoEducativo(
                "Ciclo menstrual",
                "Educacion",
                "Informacion sobre el ciclo menstrual",
                true
        );

        System.out.println("\n======================================");
        System.out.println(" INICIANDO PRUEBAS DEL REPOSITORY");
        System.out.println("======================================");
    }

    // PRUEBA 1: Guardar contenido
    @Test
    void deberiaGuardarContenido() {

        ContenidoEducativo resultado =
                repository.save(contenido);

        assertNotNull(resultado.getId());
        assertEquals("Ciclo menstrual", resultado.getTitulo());

        System.out.println("PASS [1/6]: Guardar contenido educativo");
        System.out.println("       ID generado: " + resultado.getId());
    }

    // PRUEBA 2: Listar contenidos
    @Test
    void deberiaListarContenidos() {

        repository.save(contenido);

        List<ContenidoEducativo> resultado =
                repository.findAll();

        assertEquals(1, resultado.size());

        System.out.println("PASS [2/6]: Listar todos los contenidos");
        System.out.println("       Total de contenidos: " + resultado.size());
    }

    // PRUEBA 3: Buscar contenido por ID
    @Test
    void deberiaBuscarContenidoPorId() {

        ContenidoEducativo guardado =
                repository.save(contenido);

        Optional<ContenidoEducativo> resultado =
                repository.findById(guardado.getId());

        assertTrue(resultado.isPresent());
        assertEquals(
                "Ciclo menstrual",
                resultado.get().getTitulo()
        );

        System.out.println("PASS [3/6]: Buscar contenido por ID");
        System.out.println("       ID consultado: " + guardado.getId());
    }

    // PRUEBA 4: Listar solo contenidos publicados
    @Test
    void deberiaListarSoloContenidosPublicados() {

        repository.save(contenido);

        ContenidoEducativo noPublicado =
                new ContenidoEducativo(
                        "Contenido privado",
                        "Educacion",
                        "Contenido no publicado",
                        false
                );

        repository.save(noPublicado);

        List<ContenidoEducativo> resultado =
                repository.findByPublicadoTrue();

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).isPublicado());

        System.out.println("PASS [4/6]: Filtrar contenidos publicados");
        System.out.println("       Contenidos publicados: " + resultado.size());
    }

    // PRUEBA 5: Actualizar contenido
    @Test
    void deberiaActualizarContenido() {

        ContenidoEducativo guardado =
                repository.save(contenido);

        guardado.setTitulo("Fertilidad femenina");
        guardado.setCategoria("Salud");

        ContenidoEducativo actualizado =
                repository.save(guardado);

        assertEquals(
                "Fertilidad femenina",
                actualizado.getTitulo()
        );

        assertEquals("Salud", actualizado.getCategoria());

        System.out.println("PASS [5/6]: Actualizar contenido educativo");
        System.out.println("       Nuevo titulo: " + actualizado.getTitulo());
        System.out.println("       Nueva categoria: " + actualizado.getCategoria());
    }

    // PRUEBA 6: Eliminar contenido
    @Test
    void deberiaEliminarContenido() {

        ContenidoEducativo guardado =
                repository.save(contenido);

        Long id = guardado.getId();

        repository.deleteById(id);

        Optional<ContenidoEducativo> resultado =
                repository.findById(id);

        assertFalse(resultado.isPresent());

        System.out.println("PASS [6/6]: Eliminar contenido educativo");
        System.out.println("       Contenido eliminado con ID: " + id);
    }
}
