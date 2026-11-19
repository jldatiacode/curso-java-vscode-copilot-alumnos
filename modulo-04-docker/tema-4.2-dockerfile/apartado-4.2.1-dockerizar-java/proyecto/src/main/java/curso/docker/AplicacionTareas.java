package curso.docker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicación.
 *
 * La anotación {@code @SpringBootApplication} activa la configuración
 * automática y busca componentes ({@code @RestController}, {@code @Service}…)
 * en este paquete y sus subpaquetes.
 *
 * Es la misma API de tareas del apartado 3.5, en el paquete
 * {@code curso.docker}, con un endpoint de estado ({@code GET /api/estado})
 * para comprobar el contenedor.
 */
@SpringBootApplication
public class AplicacionTareas {

    public static void main(String[] args) {
        SpringApplication.run(AplicacionTareas.class, args);
    }
}
