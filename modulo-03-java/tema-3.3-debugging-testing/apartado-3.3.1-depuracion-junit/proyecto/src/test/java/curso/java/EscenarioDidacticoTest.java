package curso.java;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Escenario didáctico: un test que FALLA a propósito.
 *
 * Supone una regla de negocio distinta de la real (que una tarea completada se
 * pueda reabrir). Está desactivado con @Disabled para que "mvn test" termine
 * correctamente. En la práctica se activa quitando @Disabled para ver cómo se
 * informa un fallo, y después se vuelve a desactivar.
 */
class EscenarioDidacticoTest {

    @Test
    @Disabled("Escenario didáctico: falla a propósito. Ver el README del ejercicio.")
    void reabrirUnaTareaCompletada() {
        ServicioTareas servicio = new ServicioTareas();
        servicio.cargarDatosDeEjemplo();

        servicio.cambiarEstado(3, EstadoTarea.PENDIENTE);

        assertEquals(EstadoTarea.PENDIENTE, servicio.buscarPorId(3).getEstado());
    }
}
