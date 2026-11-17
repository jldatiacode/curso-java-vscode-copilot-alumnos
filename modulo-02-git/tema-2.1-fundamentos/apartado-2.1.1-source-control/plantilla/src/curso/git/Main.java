package curso.git;

import java.util.List;

/**
 * Demostración del gestor de tareas desde la consola.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("== Gestor de tareas ==");

        GestorTareas gestor = new GestorTareas();
        gestor.crearTarea("Instalar JDK 21", "Comprobar java -version");
        gestor.crearTarea("Configurar VS Code", "Instalar el Extension Pack for Java");
        gestor.crearTarea("Clonar el repositorio", "Usar git clone con HTTPS");
        gestor.crearTarea("Primer commit", "Confirmar la versión inicial del proyecto");

        mostrarTareas("Todas las tareas", gestor.listarTareas());

        gestor.cambiarEstado(1, EstadoTarea.COMPLETADA);
        gestor.cambiarEstado(2, EstadoTarea.EN_PROGRESO);
        boolean cambiada = gestor.cambiarEstado(99, EstadoTarea.COMPLETADA);
        System.out.println();
        System.out.println("Cambio de estado de la tarea 99: " + (cambiada ? "realizado" : "no existe"));

        mostrarTareas("Tareas después de los cambios", gestor.listarTareas());
        mostrarTareas("Tareas pendientes", gestor.tareasPendientes());
    }

    /**
     * Muestra un título y, debajo, una línea por cada tarea.
     */
    private static void mostrarTareas(String titulo, List<Tarea> tareas) {
        System.out.println();
        System.out.println(titulo + ":");
        for (Tarea tarea : tareas) {
            System.out.println("  " + tarea.resumen());
        }
    }
}
