
package cl.ciclospa.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContenidoController.class)
class ContenidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private org.springframework.http.converter.json.MappingJackson2HttpMessageConverter converter;

    @MockitoBean
    private ContenidoService service;

    @BeforeEach
    void setUp() {
        System.out.println("\n======================================");
        System.out.println(" INICIANDO PRUEBAS DEL CONTROLLER");
        System.out.println("======================================");
    }

    private ContenidoEducativo crearContenido() {
        return new ContenidoEducativo(
                "Ciclo menstrual",
                "Educacion",
                "Informacion educativa",
                true
        );
    }

    private ContenidoResponse crearRespuesta() {
        return ContenidoResponse.from(crearContenido());
    }

    // PRUEBA 1: Listar contenidos
    @Test
    void deberiaListarContenidos() throws Exception {

        when(service.listar())
                .thenReturn(List.of(crearRespuesta()));

        mockMvc.perform(get("/api/v1/contenidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo")
                        .value("Ciclo menstrual"));

        verify(service).listar();

        System.out.println("PASS [1/5]: GET - Listar contenidos");
        System.out.println("       Respuesta HTTP 200 OK");
    }

    // PRUEBA 2: Obtener contenido por ID
    @Test
    void deberiaObtenerContenidoPorId() throws Exception {

        when(service.obtener(1L))
                .thenReturn(crearRespuesta());

        mockMvc.perform(get("/api/v1/contenidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo")
                        .value("Ciclo menstrual"));

        verify(service).obtener(1L);

        System.out.println("PASS [2/5]: GET - Obtener contenido por ID");
        System.out.println("       Respuesta HTTP 200 OK");
    }

    // PRUEBA 3: Crear contenido
    @Test
    void deberiaCrearContenido() throws Exception {

        String json = """
                {
                    "titulo": "Ciclo menstrual",
                    "categoria": "Educacion",
                    "descripcion": "Informacion educativa",
                    "publicado": true
                }
                """;

        when(service.crear(any(ContenidoRequest.class)))
                .thenReturn(crearRespuesta());

        mockMvc.perform(post("/api/v1/contenidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo")
                        .value("Ciclo menstrual"));

        verify(service).crear(any(ContenidoRequest.class));

        System.out.println("PASS [3/5]: POST - Crear contenido");
        System.out.println("       Respuesta HTTP 201 CREATED");
    }

    // PRUEBA 4: Actualizar contenido
    @Test
    void deberiaActualizarContenido() throws Exception {

        String json = """
                {
                    "titulo": "Fertilidad femenina",
                    "categoria": "Salud",
                    "descripcion": "Informacion actualizada",
                    "publicado": true
                }
                """;

        ContenidoResponse respuestaActualizada =
                ContenidoResponse.from(
                        new ContenidoEducativo(
                                "Fertilidad femenina",
                                "Salud",
                                "Informacion actualizada",
                                true
                        )
                );

        when(service.actualizar(eq(1L), any(ContenidoRequest.class)))
                .thenReturn(respuestaActualizada);

        mockMvc.perform(put("/api/v1/contenidos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo")
                        .value("Fertilidad femenina"));

        verify(service).actualizar(eq(1L), any(ContenidoRequest.class));

        System.out.println("PASS [4/5]: PUT - Actualizar contenido");
        System.out.println("       Respuesta HTTP 200 OK");
    }

    // PRUEBA 5: Eliminar contenido
    @Test
    void deberiaEliminarContenido() throws Exception {

        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/contenidos/1"))
                .andExpect(status().isNoContent());

        verify(service).eliminar(1L);

        System.out.println("PASS [5/5]: DELETE - Eliminar contenido");
        System.out.println("       Respuesta HTTP 204 NO CONTENT");
    }
}
