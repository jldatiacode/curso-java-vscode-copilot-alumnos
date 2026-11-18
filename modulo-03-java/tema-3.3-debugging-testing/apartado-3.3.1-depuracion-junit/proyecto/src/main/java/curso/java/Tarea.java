package curso.java;

/**
 * Tarea con prioridad (1 = máxima, 5 = mínima) y horas estimadas.
 */
public class Tarea {

    private final int id;
    private final String titulo;
    private final int prioridad;
    private final int horasEstimadas;
    private EstadoTarea estado = EstadoTarea.PENDIENTE;

    public Tarea(int id, String titulo, int prioridad, int horasEstimadas) {
        this.id = id;
        this.titulo = titulo;
        this.prioridad = prioridad;
        this.horasEstimadas = horasEstimadas;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public int getHorasEstimadas() {
        return horasEstimadas;
    }

    public EstadoTarea getEstado() {
        return estado;
    }

    void setEstado(EstadoTarea estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "#" + id + " " + titulo + " (prioridad " + prioridad + ", " + horasEstimadas + " h, " + estado + ")";
    }
}
