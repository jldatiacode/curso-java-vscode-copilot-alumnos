package curso.java;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Servicio que gestiona las tareas en memoria.
 */
public class ServicioTareas {

    /**
     * Resumen del número de tareas. Un record de Java genera automáticamente el
     * constructor, los métodos de acceso, equals, hashCode y toString.
     */
    public record Resumen(int total, long pendientes, long completadas) {
    }

    private final List<Tarea> tareas = new ArrayList<>();
    private int siguienteId = 1;

    public Tarea crearTarea(String titulo, String descripcion) {
        Tarea tarea = new Tarea(siguienteId++, titulo, descripcion);
        tareas.add(tarea);
        return tarea;
    }

    public List<Tarea> listarTareas() {
        return List.copyOf(tareas);
    }

    public Optional<Tarea> buscarPorId(int id) {
        return tareas.stream().filter(tarea -> tarea.getId() == id).findFirst();
    }

    /**
     * Cambia el estado de una tarea. Una tarea completada no puede cambiar de
     * estado.
     *
     * @return true si el cambio se ha realizado
     */
    public boolean cambiarEstado(int id, EstadoTarea nuevoEstado) {
        Optional<Tarea> encontrada = buscarPorId(id);
        if (encontrada.isEmpty() || encontrada.get().getEstado().esFinal()) {
            return false;
        }
        encontrada.get().setEstado(nuevoEstado);
        return true;
    }

    public List<Tarea> tareasPorEstado(EstadoTarea estado) {
        return tareas.stream().filter(tarea -> tarea.getEstado() == estado).toList();
    }

    public Map<EstadoTarea, Integer> contarPorEstado() {
        Map<EstadoTarea, Integer> recuento = new EnumMap<>(EstadoTarea.class);
        for (EstadoTarea estado : EstadoTarea.values()) {
            recuento.put(estado, tareasPorEstado(estado).size());
        }
        return recuento;
    }

    public Resumen resumen() {
        return new Resumen(tareas.size(), tareasPorEstado(EstadoTarea.PENDIENTE).size(),
                tareasPorEstado(EstadoTarea.COMPLETADA).size());
    }
}
