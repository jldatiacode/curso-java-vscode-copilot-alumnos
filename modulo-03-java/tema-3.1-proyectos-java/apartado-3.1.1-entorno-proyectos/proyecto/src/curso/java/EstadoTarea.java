package curso.java;

/**
 * Estados por los que pasa una tarea.
 */
public enum EstadoTarea {
    PENDIENTE, EN_PROGRESO, COMPLETADA;

    /**
     * Indica si el estado es final, es decir, si la tarea ya está terminada.
     */
    public boolean esFinal() {
        return this == COMPLETADA;
    }

    /**
     * Devuelve el texto que se muestra al usuario. Usa una expresión switch: el
     * compilador comprueba que se cubren todos los valores del enum.
     */
    public String descripcion() {
        return switch (this) {
        case PENDIENTE -> "Pendiente";
        case EN_PROGRESO -> "En progreso";
        case COMPLETADA -> "Completada";
        };
    }
}
