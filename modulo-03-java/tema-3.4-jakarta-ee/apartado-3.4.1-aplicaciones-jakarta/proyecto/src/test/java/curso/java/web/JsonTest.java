package curso.java.web;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Pruebas de la conversión a JSON. No necesitan servidor.
 */
class JsonTest {

    @Test
    void convierteUnaTarea() {
        assertEquals("{\"id\":1,\"titulo\":\"Probar\",\"estado\":\"PENDIENTE\"}",
                Json.tarea(new Tarea(1, "Probar", EstadoTarea.PENDIENTE)));
    }

    @Test
    void escapaLasComillas() {
        assertEquals("\"Leer \\\"README\\\"\"", Json.texto("Leer \"README\""));
    }

    @Test
    void convierteUnaListaVacia() {
        assertEquals("[]", Json.tareas(List.of()));
    }
}
