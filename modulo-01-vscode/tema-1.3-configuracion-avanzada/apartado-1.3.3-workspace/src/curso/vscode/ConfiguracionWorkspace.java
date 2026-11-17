package curso.vscode;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Programa de apoyo para el ejercicio 1.3.3.
 *
 * Muestra los valores que puede fijar una configuración de ejecución
 * (launch.json): directorio de trabajo, argumentos, propiedades de la JVM y
 * variables de entorno. Así se comprueba qué configuración se ha aplicado.
 */
public class ConfiguracionWorkspace {

    // Archivo del ejercicio que se busca en el directorio de trabajo. Solo se
    // encuentra si el programa se ejecuta desde la carpeta del ejercicio.
    private static final Path ARCHIVO_REFERENCIA = Path.of("ejemplos", "settings-ejemplo.json");

    public static void main(String[] args) {
        String entorno = System.getProperty("curso.entorno");
        String modo = System.getenv("CURSO_MODO");

        System.out.println("== Configuración de ejecución ==");
        mostrar("Directorio de trabajo", System.getProperty("user.dir"));
        mostrar("Encuentra " + ARCHIVO_REFERENCIA.toString().replace('\\', '/'),
                Files.exists(ARCHIVO_REFERENCIA) ? "sí" : "no");
        mostrar("Argumentos (args)", args.length == 0 ? "(ninguno)" : Arrays.toString(args));
        mostrar("Propiedad curso.entorno (vmArgs)", entorno == null ? "(no definida)" : entorno);
        mostrar("Variable CURSO_MODO (env)", modo == null ? "(no definida)" : modo);
        mostrar("Versión de Java", Runtime.version().toString());

        System.out.println();
        int opciones = contarOpcionesDeLanzamiento(args, entorno, modo);
        System.out.println("Opciones de lanzamiento detectadas: " + opciones + " de 3");
        if (opciones == 3) {
            System.out.println("Se ha aplicado una configuración de lanzamiento completa.");
        } else {
            System.out.println("Ejecución sin configuración de lanzamiento completa.");
        }
    }

    /**
     * Cuenta cuántas de las tres opciones de launch.json (args, vmArgs y env) han
     * llegado al programa.
     */
    private static int contarOpcionesDeLanzamiento(String[] args, String entorno, String modo) {
        int total = 0;
        if (args.length > 0) {
            total++;
        }
        if (entorno != null) {
            total++;
        }
        if (modo != null) {
            total++;
        }
        return total;
    }

    private static void mostrar(String etiqueta, String valor) {
        System.out.printf("%-42s %s%n", etiqueta + ":", valor);
    }
}
