package curso.springboot.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import curso.springboot.dto.CrearTareaRequest;
import curso.springboot.model.EstadoTarea;
import curso.springboot.model.Tarea;

/**
 * Lógica de negocio de las tareas, con almacenamiento en memoria.
 *
 * La anotación {@code @Service} registra la clase como componente: Spring crea
 * una única instancia y la inyecta donde se necesite.
 */
@Service
public class TareaService {

    private final Map<Long, Tarea> tareas = new ConcurrentHashMap<>();
    private final AtomicLong siguienteId = new AtomicLong(1);

    public TareaService() {
        guardar("Crear el proyecto Spring Boot", "Plantilla del curso", EstadoTarea.COMPLETADA);
        guardar("Probar los endpoints", "Con curl o con un archivo .http", EstadoTarea.EN_PROGRESO);
        guardar("Depurar una petición", "Breakpoint en el controlador", EstadoTarea.PENDIENTE);
    }

    public List<Tarea> listar() {
        return tareas.values().stream().sorted(Comparator.comparingLong(Tarea::id)).toList();
    }

    public Optional<Tarea> buscar(long id) {
        return Optional.ofNullable(tareas.get(id));
    }

    public Tarea crear(CrearTareaRequest datos) {
        return guardar(datos.titulo().strip(), datos.descripcion() == null ? "" : datos.descripcion().strip(),
                EstadoTarea.PENDIENTE);
    }

    /**
     * Cambia el estado de una tarea. Una tarea completada no puede cambiar de
     * estado.
     *
     * @return la tarea modificada, o vacío si no existe
     * @throws IllegalStateException si la tarea ya está completada
     */
    public Optional<Tarea> cambiarEstado(long id, EstadoTarea nuevoEstado) {
        Tarea actual = tareas.get(id);
        if (actual == null) {
            return Optional.empty();
        }
        if (actual.estado() == EstadoTarea.COMPLETADA) {
            throw new IllegalStateException("La tarea " + id + " está completada y no puede cambiar de estado");
        }
        Tarea modificada = actual.conEstado(nuevoEstado);
        tareas.put(id, modificada);
        return Optional.of(modificada);
    }

    private Tarea guardar(String titulo, String descripcion, EstadoTarea estado) {
        Tarea tarea = new Tarea(siguienteId.getAndIncrement(), titulo, descripcion, estado);
        tareas.put(tarea.id(), tarea);
        return tarea;
    }
}
