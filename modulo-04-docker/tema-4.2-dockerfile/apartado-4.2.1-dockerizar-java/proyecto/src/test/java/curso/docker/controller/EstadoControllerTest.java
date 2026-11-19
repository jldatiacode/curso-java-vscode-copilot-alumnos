package curso.docker.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Pruebas del endpoint de estado.
 *
 * {@code properties} fija el mensaje para que la prueba no dependa de una
 * variable de entorno APP_MENSAJE que pudiera existir en el equipo.
 */
@SpringBootTest(properties = "app.mensaje=Mensaje de prueba")
@AutoConfigureMockMvc
class EstadoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void informaDelEstadoDeLaApi() throws Exception {
        mvc.perform(get("/api/estado")).andExpect(status().isOk()).andExpect(jsonPath("$.estado").value("OK"))
                .andExpect(jsonPath("$.mensaje").value("Mensaje de prueba"))
                .andExpect(jsonPath("$.java").value(System.getProperty("java.version")));
    }
}
