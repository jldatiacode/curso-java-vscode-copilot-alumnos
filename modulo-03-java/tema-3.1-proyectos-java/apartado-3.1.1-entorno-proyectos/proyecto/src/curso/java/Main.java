package curso.java;

/**
 * Demostración del servicio de tareas desde la consola.
 */
public class Main {

    public static void main(String[] args) {
        var servicio = new ServicioTareas();
        servicio.crearTarea("Instalar JDK 21", "Comprobar java -version");
        servicio.crearTarea("Configurar VS Code", "Instalar el Extension Pack for Java");
        servicio.crearTarea("Crear el proyecto", "Abrir la carpeta en VS Code");
        servicio.crearTarea("Ejecutar la aplicación", "Usar Run sobre el método main");

        servicio.cambiarEstado(1, EstadoTarea.COMPLETADA);
        servicio.cambiarEstado(2, EstadoTarea.EN_PROGRESO);
        boolean reabierta = servicio.cambiarEstado(1, EstadoTarea.PENDIENTE);

        System.out.println("== Tareas ==");
        for (Tarea tarea : servicio.listarTareas()) {
            System.out.printf("%-3s %-25s %s%n", "#" + tarea.getId(), tarea.getTitulo(),
                    tarea.getEstado().descripcion());
        }

        System.out.println();
        System.out.println("Reabrir una tarea completada: " + (reabierta ? "permitido" : "no permitido"));
        System.out.println("Recuento por estado: " + servicio.contarPorEstado());
        System.out.println("Resumen: " + servicio.resumen());
    }
}
