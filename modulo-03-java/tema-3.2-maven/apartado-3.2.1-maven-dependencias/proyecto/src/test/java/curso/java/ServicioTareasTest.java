package curso.java;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Pruebas del servicio de tareas. Se ejecutan con: mvn test
 */
class ServicioTareasTest {

    private final ServicioTareas servicio = new ServicioTareas();

    @Test
    void normalizaElTituloAlCrearUnaTarea() {
        Tarea tarea = servicio.crearTarea("  instalar   maven ", "");

        assertEquals("Instalar maven", tarea.getTitulo());
        assertEquals(EstadoTarea.PENDIENTE, tarea.getEstado());
    }

    @Test
    void rechazaUnTituloEnBlanco() {
        assertThrows(IllegalArgumentException.class, () -> servicio.crearTarea("   ", "Sin título"));
    }

    @Test
    void abreviaLasDescripcionesLargas() {
        Tarea tarea = servicio.crearTarea("Tarea", "Una descripción bastante larga para el listado");

        assertTrue(servicio.resumir(tarea).endsWith("..."));
    }
}
