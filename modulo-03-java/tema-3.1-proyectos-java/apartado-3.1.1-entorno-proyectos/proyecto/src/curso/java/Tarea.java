package curso.java;

/**
 * Tarea con identificador, título, descripción y estado.
 */
public class Tarea {

    private final int id;
    private final String titulo;
    private final String descripcion;
    private EstadoTarea estado;

    public Tarea(int id, String titulo, String descripcion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = EstadoTarea.PENDIENTE;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public EstadoTarea getEstado() {
        return estado;
    }

    public void setEstado(EstadoTarea estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "#" + id + " " + titulo + " [" + estado.descripcion() + "]";
    }
}
