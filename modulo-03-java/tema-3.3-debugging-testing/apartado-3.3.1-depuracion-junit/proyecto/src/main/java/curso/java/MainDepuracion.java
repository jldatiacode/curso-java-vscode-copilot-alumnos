package curso.java;

/**
 * Programa para practicar la depuración paso a paso.
 */
public class MainDepuracion {

    public static void main(String[] args) {
        ServicioTareas servicio = new ServicioTareas();
        servicio.cargarDatosDeEjemplo();

        servicio.cambiarEstado(1, EstadoTarea.EN_PROGRESO);
        servicio.cambiarEstado(2, EstadoTarea.COMPLETADA);

        int horas = servicio.calcularHorasPendientes();
        double porcentaje = servicio.porcentajeCompletado();
        Tarea prioritaria = servicio.tareaMasPrioritaria();

        System.out.println("Tareas: " + servicio.listarTareas().size());
        System.out.println("Horas pendientes: " + horas);
        System.out.println("Porcentaje completado: " + porcentaje + " %");
        System.out.println("Tarea más prioritaria: " + prioritaria);
    }
}
