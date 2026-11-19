package curso.docker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import curso.docker.dto.CrearTareaRequest;
import curso.docker.model.EstadoTarea;
import curso.docker.model.Tarea;

/**
 * Pruebas unitarias del servicio: sin Spring, sin servidor.
 */
class TareaServiceTest {

    private final TareaService servicio = new TareaService();

    @Test
    void empiezaConTresTareas() {
        assertThat(servicio.listar()).hasSize(3).extracting(Tarea::id).containsExactly(1L, 2L, 3L);
    }

    @Test
    void creaTareasPendientesConTitulosSinEspaciosSobrantes() {
        Tarea creada = servicio.crear(new CrearTareaRequest("  Nueva tarea  ", null));

        assertThat(creada.id()).isEqualTo(4);
        assertThat(creada.titulo()).isEqualTo("Nueva tarea");
        assertThat(creada.estado()).isEqualTo(EstadoTarea.PENDIENTE);
    }

    @Test
    void noPermiteCambiarUnaTareaCompletada() {
        assertThatThrownBy(() -> servicio.cambiarEstado(1, EstadoTarea.PENDIENTE))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void cambiarElEstadoDeUnaTareaInexistenteDevuelveVacio() {
        assertThat(servicio.cambiarEstado(99, EstadoTarea.COMPLETADA)).isEmpty();
    }
}
