package curso.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

/**
 * Servicio que gestiona las tareas en memoria.
 *
 * Usa StringUtils (Apache Commons Lang) para validar y normalizar textos: es la
 * dependencia externa del proyecto.
 */
public class ServicioTareas {

    // Longitud máxima con la que se muestran las descripciones en los listados.
    static final int LONGITUD_RESUMEN = 30;

    private final List<Tarea> tareas = new ArrayList<>();
    private int siguienteId = 1;

    /**
     * Crea una tarea. El título no puede estar vacío y se guarda sin espacios
     * sobrantes y con la primera letra en mayúscula.
     *
     * @throws IllegalArgumentException si el título está vacío o en blanco
     */
    public Tarea crearTarea(String titulo, String descripcion) {
        if (StringUtils.isBlank(titulo)) {
            throw new IllegalArgumentException("El título de la tarea es obligatorio");
        }
        String tituloNormalizado = StringUtils.capitalize(StringUtils.normalizeSpace(titulo));
        Tarea tarea = new Tarea(siguienteId++, tituloNormalizado, StringUtils.defaultString(descripcion));
        tareas.add(tarea);
        return tarea;
    }

    public List<Tarea> listarTareas() {
        return List.copyOf(tareas);
    }

    public Optional<Tarea> buscarPorId(int id) {
        return tareas.stream().filter(tarea -> tarea.getId() == id).findFirst();
    }

    public boolean cambiarEstado(int id, EstadoTarea nuevoEstado) {
        Optional<Tarea> encontrada = buscarPorId(id);
        encontrada.ifPresent(tarea -> tarea.setEstado(nuevoEstado));
        return encontrada.isPresent();
    }

    /**
     * Devuelve una línea con el resumen de la tarea. Las descripciones largas se
     * abrevian con puntos suspensivos.
     */
    public String resumir(Tarea tarea) {
        return String.format("#%-2d %-24s %-11s %s", tarea.getId(), tarea.getTitulo(), tarea.getEstado(),
                StringUtils.abbreviate(tarea.getDescripcion(), LONGITUD_RESUMEN));
    }
}
