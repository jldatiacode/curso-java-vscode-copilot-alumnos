package curso.java.web;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Almacén de tareas en memoria.
 *
 * Usa CopyOnWriteArrayList porque un contenedor Servlet atiende varias
 * peticiones a la vez desde hilos distintos.
 */
public class RepositorioTareas {

    private final List<Tarea> tareas = new CopyOnWriteArrayList<>();

    public RepositorioTareas() {
        tareas.add(new Tarea(1, "Instalar Apache Tomcat", EstadoTarea.COMPLETADA));
        tareas.add(new Tarea(2, "Desplegar el WAR", EstadoTarea.EN_PROGRESO));
        tareas.add(new Tarea(3, "Probar los endpoints", EstadoTarea.PENDIENTE));
    }

    public List<Tarea> listar() {
        return List.copyOf(tareas);
    }

    public int contar() {
        return tareas.size();
    }
}
