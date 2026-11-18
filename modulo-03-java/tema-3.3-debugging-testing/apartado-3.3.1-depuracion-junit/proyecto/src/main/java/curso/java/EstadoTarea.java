package curso.java;

/**
 * Estados por los que pasa una tarea y transiciones permitidas entre ellos.
 */
public enum EstadoTarea {
    PENDIENTE, EN_PROGRESO, COMPLETADA;

    /**
     * Regla de negocio: una tarea completada no puede cambiar de estado; el resto
     * puede pasar a cualquier otro estado distinto del actual.
     */
    public boolean puedeCambiarA(EstadoTarea destino) {
        if (this == COMPLETADA) {
            return false;
        }
        return this != destino;
    }
}
