package curso.docker.model;

/**
 * Tarea inmutable. Jackson la convierte a JSON con un campo por componente.
 */
public record Tarea(long id, String titulo, String descripcion, EstadoTarea estado) {

    /**
     * Devuelve una copia de la tarea con otro estado.
     */
    public Tarea conEstado(EstadoTarea nuevoEstado) {
        return new Tarea(id, titulo, descripcion, nuevoEstado);
    }
}
