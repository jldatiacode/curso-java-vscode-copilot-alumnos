package curso.git;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Gestiona una lista de tareas en memoria.
 */
public class GestorTareas {

    private final List<Tarea> tareas = new ArrayList<>();

    // Identificador que recibirá la próxima tarea creada.
    private int siguienteId = 1;

    /**
     * Crea una tarea nueva en estado PENDIENTE y la añade a la lista.
     *
     * @param titulo      título breve de la tarea
     * @param descripcion explicación de lo que hay que hacer
     * @return la tarea creada
     */
    public Tarea crearTarea(String titulo, String descripcion) {
        Tarea tarea = new Tarea(siguienteId, titulo, descripcion);
        siguienteId++;
        tareas.add(tarea);
        return tarea;
    }

    /**
     * Devuelve todas las tareas, en el orden en que se crearon.
     *
     * @return lista de solo lectura
     */
    public List<Tarea> listarTareas() {
        return List.copyOf(tareas);
    }

    /**
     * Busca una tarea por su identificador.
     *
     * @param id identificador de la tarea
     * @return la tarea, o un Optional vacío si no existe
     */
    public Optional<Tarea> buscarPorId(int id) {
        for (Tarea tarea : tareas) {
            if (tarea.getId() == id) {
                return Optional.of(tarea);
            }
        }
        return Optional.empty();
    }

    /**
     * Cambia el estado de una tarea.
     *
     * @param id          identificador de la tarea
     * @param nuevoEstado estado que se asigna
     * @return true si la tarea existía y se ha cambiado
     */
    public boolean cambiarEstado(int id, EstadoTarea nuevoEstado) {
        Optional<Tarea> encontrada = buscarPorId(id);
        if (encontrada.isEmpty()) {
            return false;
        }
        encontrada.get().setEstado(nuevoEstado);
        return true;
    }

    /**
     * Devuelve las tareas que todavía no se han empezado.
     *
     * @return tareas en estado PENDIENTE
     */
    public List<Tarea> tareasPendientes() {
        List<Tarea> resultado = new ArrayList<>();
        for (Tarea tarea : tareas) {
            if (tarea.getEstado() == EstadoTarea.PENDIENTE) {
                resultado.add(tarea);
            }
        }
        return resultado;
    }
}
