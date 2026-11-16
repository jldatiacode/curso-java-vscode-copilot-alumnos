package curso.vscode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Gestiona una lista de alumnos en memoria.
 *
 * No hay base de datos: los alumnos se guardan en una lista y se pierden al
 * terminar el programa. Es suficiente para practicar la navegación entre clases
 * en VS Code.
 */
public class GestionAlumnos {

    // Lista interna con los alumnos registrados.
    private final List<Alumno> alumnos = new ArrayList<>();

    /**
     * Añade un alumno a la lista.
     *
     * @param alumno alumno que se quiere registrar
     */
    public void registrarAlumno(Alumno alumno) {
        alumnos.add(alumno);
    }

    /**
     * Busca un alumno por su identificador.
     *
     * Devuelve un Optional vacío si no existe ningún alumno con ese identificador,
     * en lugar de devolver null.
     *
     * @param id identificador que se busca
     * @return el alumno encontrado, o un Optional vacío
     */
    public Optional<Alumno> buscarPorId(int id) {
        for (Alumno alumno : alumnos) {
            if (alumno.getId() == id) {
                return Optional.of(alumno);
            }
        }
        return Optional.empty();
    }

    /**
     * Devuelve todos los alumnos registrados.
     *
     * Se devuelve una copia de solo lectura para que nadie pueda modificar la lista
     * interna desde fuera de esta clase.
     *
     * @return lista de alumnos (no modificable)
     */
    public List<Alumno> listarAlumnos() {
        return List.copyOf(alumnos);
    }

    /**
     * Devuelve el número de alumnos registrados.
     *
     * @return total de alumnos
     */
    public int contarAlumnos() {
        return alumnos.size();
    }
}
