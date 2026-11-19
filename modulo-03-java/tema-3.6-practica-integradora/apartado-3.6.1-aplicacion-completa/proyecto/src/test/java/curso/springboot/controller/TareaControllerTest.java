package curso.springboot.controller;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.jayway.jsonpath.JsonPath;

/**
 * Pruebas de la API completa (controlador, servicio y validación) con MockMvc,
 * sin arrancar un servidor real.
 *
 * Las tareas 1 a 4 se cargan al arrancar y estas pruebas no las modifican. Las
 * pruebas que cambian datos trabajan sobre tareas que crean ellas mismas.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TareaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void listaLasTareas() throws Exception {
        mvc.perform(get("/api/tareas")).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(4)))
                .andExpect(jsonPath("$[0].titulo").value("Crear el proyecto Spring Boot"));
    }

    @Test
    void consultaUnaTarea() throws Exception {
        mvc.perform(get("/api/tareas/2")).andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_PROGRESO"));
    }

    @Test
    void consultarUnaTareaInexistenteDevuelve404() throws Exception {
        mvc.perform(get("/api/tareas/999")).andExpect(status().isNotFound());
    }

    @Test
    void creaUnaTarea() throws Exception {
        mvc.perform(post("/api/tareas").contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\"Escribir pruebas\",\"descripcion\":\"Con MockMvc\"}"))
                .andExpect(status().isCreated()).andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void rechazaUnaTareaSinTitulo() throws Exception {
        mvc.perform(post("/api/tareas").contentType(MediaType.APPLICATION_JSON).content("{\"titulo\":\"  \"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cambiaElEstadoDeUnaTarea() throws Exception {
        String creada = mvc
                .perform(post("/api/tareas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"Temporal\"}"))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        long id = ((Number) JsonPath.read(creada, "$.id")).longValue();

        mvc.perform(patch("/api/tareas/" + id + "/estado").contentType(MediaType.APPLICATION_JSON)
                .content("{\"estado\":\"EN_PROGRESO\"}")).andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_PROGRESO"));
    }

    @Test
    void noPermiteCambiarUnaTareaCompletada() throws Exception {
        mvc.perform(patch("/api/tareas/1/estado").contentType(MediaType.APPLICATION_JSON)
                .content("{\"estado\":\"PENDIENTE\"}")).andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Cambio de estado no permitido"));
    }

    @Test
    void rechazaUnEstadoDesconocido() throws Exception {
        mvc.perform(patch("/api/tareas/2/estado").contentType(MediaType.APPLICATION_JSON)
                .content("{\"estado\":\"CANCELADA\"}")).andExpect(status().isBadRequest());
    }
}
