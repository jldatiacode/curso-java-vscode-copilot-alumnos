package curso.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicación.
 *
 * La anotación {@code @SpringBootApplication} activa la configuración
 * automática y busca componentes ({@code @RestController}, {@code @Service}…)
 * en este paquete y sus subpaquetes.
 */
@SpringBootApplication
public class AplicacionTareas {

    public static void main(String[] args) {
        SpringApplication.run(AplicacionTareas.class, args);
    }
}
