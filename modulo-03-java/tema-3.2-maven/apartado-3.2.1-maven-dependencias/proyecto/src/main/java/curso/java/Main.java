package curso.java;

/**
 * Demostración del gestor de tareas.
 */
public class Main {

    public static void main(String[] args) {
        ServicioTareas servicio = new ServicioTareas();
        servicio.crearTarea("  instalar   maven ", "Descargar Maven y añadir su carpeta bin al PATH");
        servicio.crearTarea("revisar el pom.xml", "Coordenadas, dependencias y plugins");
        servicio.crearTarea("ejecutar mvn package", "Generar el JAR en target");

        servicio.cambiarEstado(1, EstadoTarea.COMPLETADA);
        servicio.cambiarEstado(2, EstadoTarea.EN_PROGRESO);

        System.out.println("== Gestor de tareas (Maven) ==");
        for (Tarea tarea : servicio.listarTareas()) {
            System.out.println(servicio.resumir(tarea));
        }
    }
}
