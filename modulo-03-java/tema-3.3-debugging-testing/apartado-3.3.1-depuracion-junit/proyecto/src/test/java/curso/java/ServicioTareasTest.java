package curso.java;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Pruebas del servicio de tareas.
 */
class ServicioTareasTest {

    private ServicioTareas servicio;

    @BeforeEach
    void prepararDatos() {
        servicio = new ServicioTareas();
        servicio.cargarDatosDeEjemplo();
    }

    @Test
    @DisplayName("Carga 20 tareas, 6 de ellas completadas")
    void cargaLosDatosDeEjemplo() {
        assertAll(() -> assertEquals(20, servicio.listarTareas().size()),
                () -> assertEquals(30.0, servicio.porcentajeCompletado()));
    }

    @Test
    @DisplayName("Suma las horas de las tareas no completadas")
    void calculaLasHorasPendientes() {
        assertEquals(33, servicio.calcularHorasPendientes());
    }

    @Test
    @DisplayName("La tarea más prioritaria es la primera pendiente con prioridad 1")
    void encuentraLaTareaMasPrioritaria() {
        assertEquals(5, servicio.tareaMasPrioritaria().getId());
    }

    @Test
    @DisplayName("Una tarea completada no se puede reabrir")
    void noPermiteReabrirUnaTareaCompletada() {
        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> servicio.cambiarEstado(3, EstadoTarea.PENDIENTE));
        assertTrue(error.getMessage().contains("COMPLETADA"));
    }

    @Test
    @DisplayName("Buscar una tarea inexistente produce un error")
    void buscarUnaTareaInexistente() {
        assertThrows(NoSuchElementException.class, () -> servicio.buscarPorId(99));
    }

    @ParameterizedTest(name = "{0} -> {1}: {2}")
    @CsvSource({ "PENDIENTE, EN_PROGRESO, true", "PENDIENTE, COMPLETADA, true", "EN_PROGRESO, PENDIENTE, true",
            "PENDIENTE, PENDIENTE, false", "COMPLETADA, PENDIENTE, false", "COMPLETADA, EN_PROGRESO, false" })
    @DisplayName("Transiciones de estado permitidas")
    void transicionesDeEstado(EstadoTarea origen, EstadoTarea destino, boolean permitida) {
        assertEquals(permitida, origen.puedeCambiarA(destino));
    }
}
