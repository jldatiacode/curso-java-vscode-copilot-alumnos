package curso.docker.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.docker.service.TareaService;

/**
 * Comprobación de disponibilidad de la API: {@code GET /api/estado}.
 *
 * Sirve para saber si el contenedor está atendiendo peticiones y para ver desde
 * dónde se ejecuta:
 * <ul>
 * <li>{@code mensaje}: propiedad {@code app.mensaje}. Se puede cambiar sin
 * recompilar con la variable de entorno {@code APP_MENSAJE}.</li>
 * <li>{@code host}: nombre de la máquina. Dentro de un contenedor es el
 * identificador corto del contenedor.</li>
 * <li>{@code java}: versión de Java que ejecuta la aplicación.</li>
 * </ul>
 */
@RestController
public class EstadoController {

    private final TareaService servicio;
    private final String mensaje;

    // Spring inyecta el valor de la propiedad app.mensaje (application.properties
    // o variable de entorno APP_MENSAJE, que tiene prioridad).
    public EstadoController(TareaService servicio, @Value("${app.mensaje}") String mensaje) {
        this.servicio = servicio;
        this.mensaje = mensaje;
    }

    @GetMapping("/api/estado")
    public Map<String, Object> estado() {
        // LinkedHashMap mantiene el orden de los campos en el JSON.
        Map<String, Object> estado = new LinkedHashMap<>();
        estado.put("estado", "OK");
        estado.put("mensaje", mensaje);
        estado.put("tareas", servicio.listar().size());
        estado.put("host", nombreDelHost());
        estado.put("java", System.getProperty("java.version"));
        return estado;
    }

    private static String nombreDelHost() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "desconocido";
        }
    }
}
