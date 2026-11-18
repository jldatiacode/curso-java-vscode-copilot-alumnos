package curso.java;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Servicio de tareas preparado para practicar depuración y pruebas.
 */
public class ServicioTareas {

    private final List<Tarea> tareas = new ArrayList<>();

    /**
     * Carga 20 tareas de ejemplo, siempre iguales. Las tareas cuyo identificador es
     * múltiplo de 3 empiezan completadas.
     */
    public void cargarDatosDeEjemplo() {
        for (int i = 1; i <= 20; i++) {
            Tarea tarea = new Tarea(i, "Tarea " + i, (i % 5) + 1, (i % 4) + 1);
            if (i % 3 == 0) {
                tarea.setEstado(EstadoTarea.COMPLETADA);
            }
            tareas.add(tarea);
        }
    }

    public List<Tarea> listarTareas() {
        return List.copyOf(tareas);
    }

    public Tarea buscarPorId(int id) {
        for (Tarea tarea : tareas) {
            if (tarea.getId() == id) {
                return tarea;
            }
        }
        throw new NoSuchElementException("No existe la tarea " + id);
    }

    /**
     * Cambia el estado de una tarea aplicando la regla de negocio de EstadoTarea.
     *
     * @throws IllegalStateException si la transición no está permitida
     */
    public void cambiarEstado(int id, EstadoTarea destino) {
        Tarea tarea = buscarPorId(id);
        if (!tarea.getEstado().puedeCambiarA(destino)) {
            throw new IllegalStateException(
                    "La tarea " + id + " no puede pasar de " + tarea.getEstado() + " a " + destino);
        }
        tarea.setEstado(destino);
    }

    /**
     * Suma las horas estimadas de las tareas que no están completadas. Es el método
     * recomendado para practicar breakpoints condicionales y logpoints.
     */
    public int calcularHorasPendientes() {
        int total = 0;
        for (Tarea tarea : tareas) {
            if (tarea.getEstado() != EstadoTarea.COMPLETADA) {
                total += tarea.getHorasEstimadas();
            }
        }
        return total;
    }

    /**
     * Devuelve la tarea no completada con mayor prioridad (número más bajo). En
     * caso de empate, la de menor identificador.
     */
    public Tarea tareaMasPrioritaria() {
        Tarea candidata = null;
        for (Tarea tarea : tareas) {
            boolean pendiente = tarea.getEstado() != EstadoTarea.COMPLETADA;
            if (pendiente && (candidata == null || tarea.getPrioridad() < candidata.getPrioridad())) {
                candidata = tarea;
            }
        }
        return candidata;
    }

    /**
     * Porcentaje de tareas completadas, con un decimal.
     */
    public double porcentajeCompletado() {
        long completadas = tareas.stream().filter(t -> t.getEstado() == EstadoTarea.COMPLETADA).count();
        double porcentaje = completadas * 100.0 / tareas.size();
        return Math.round(porcentaje * 10) / 10.0;
    }
}
