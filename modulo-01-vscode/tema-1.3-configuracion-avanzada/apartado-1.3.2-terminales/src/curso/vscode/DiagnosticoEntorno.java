package curso.vscode;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Muestra información del entorno en el que se ejecuta el programa: versión de
 * Java, sistema operativo, rutas, algunas variables de entorno y los argumentos
 * recibidos.
 *
 * Sirve para comprobar desde la terminal integrada qué Java se está usando y
 * cómo cambia el entorno entre PowerShell, Bash, Windows, Linux y WSL.
 */
public class DiagnosticoEntorno {

    // Únicas variables de entorno cuyo valor se muestra.
    private static final List<String> VARIABLES_CONSULTADAS = List.of("JAVA_HOME", "TERM_PROGRAM", "SHELL", "ComSpec",
            "WSL_DISTRO_NAME", "CURSO_MODO", "GITHUB_TOKEN");

    // Si el nombre de una variable contiene alguno de estos fragmentos, su valor
    // nunca se muestra: podría ser una credencial.
    private static final List<String> FRAGMENTOS_SENSIBLES = List.of("TOKEN", "SECRET", "PASSWORD", "PASSWD", "KEY",
            "CREDENTIAL");

    public static void main(String[] args) {
        seccion("Java");
        dato("Versión", Runtime.version().toString());
        dato("Fabricante", System.getProperty("java.vendor"));
        dato("Instalación (java.home)", System.getProperty("java.home"));

        seccion("Sistema operativo");
        dato("Nombre", System.getProperty("os.name"));
        dato("Versión", System.getProperty("os.version"));
        dato("Arquitectura", System.getProperty("os.arch"));
        dato("Entorno detectado", detectarEntorno());

        seccion("Rutas");
        dato("Directorio de trabajo", System.getProperty("user.dir"));
        dato("Separador de carpetas", File.separator);
        dato("Separador de rutas (PATH)", File.pathSeparator);

        seccion("Variables de entorno");
        for (String nombre : VARIABLES_CONSULTADAS) {
            dato(nombre, valorSeguro(nombre, System.getenv(nombre)));
        }
        mostrarResumenPath();
        dato("Variables con nombre sensible", contarVariablesSensibles() + " (valores no mostrados)");

        seccion("Argumentos");
        dato("Número de argumentos", String.valueOf(args.length));
        for (int i = 0; i < args.length; i++) {
            dato("args[" + i + "]", args[i]);
        }
    }

    /**
     * Indica, de forma aproximada, dónde se está ejecutando el programa.
     */
    private static String detectarEntorno() {
        String sistema = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        String entorno;
        if (sistema.startsWith("windows")) {
            entorno = "Windows";
        } else if (System.getenv("WSL_DISTRO_NAME") != null) {
            entorno = "Linux en WSL (" + System.getenv("WSL_DISTRO_NAME") + ")";
        } else {
            entorno = "Linux u otro sistema tipo Unix";
        }
        if ("vscode".equals(System.getenv("TERM_PROGRAM"))) {
            entorno += ", terminal integrada de VS Code";
        }
        return entorno;
    }

    /**
     * Devuelve el valor de una variable o un texto alternativo si no debe o no
     * puede mostrarse.
     */
    private static String valorSeguro(String nombre, String valor) {
        if (valor == null) {
            return "(no definida)";
        }
        if (esSensible(nombre)) {
            return "(definida, valor oculto)";
        }
        return valor;
    }

    private static boolean esSensible(String nombre) {
        String mayusculas = nombre.toUpperCase(Locale.ROOT);
        for (String fragmento : FRAGMENTOS_SENSIBLES) {
            if (mayusculas.contains(fragmento)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Muestra cuántas carpetas tiene PATH y cuáles parecen de Java. No se imprime
     * PATH completo porque suele ser muy largo.
     */
    private static void mostrarResumenPath() {
        String path = System.getenv("PATH");
        if (path == null) {
            dato("PATH", "(no definida)");
            return;
        }
        String[] carpetas = path.split(File.pathSeparator);
        dato("PATH", carpetas.length + " carpetas");
        for (String carpeta : carpetas) {
            String minusculas = carpeta.toLowerCase(Locale.ROOT);
            if (minusculas.contains("java") || minusculas.contains("jdk")) {
                dato("  PATH con Java", carpeta);
            }
        }
    }

    private static int contarVariablesSensibles() {
        int total = 0;
        for (Map.Entry<String, String> variable : System.getenv().entrySet()) {
            if (esSensible(variable.getKey())) {
                total++;
            }
        }
        return total;
    }

    private static void seccion(String titulo) {
        System.out.println();
        System.out.println("== " + titulo + " ==");
    }

    private static void dato(String etiqueta, String valor) {
        System.out.printf("%-30s %s%n", etiqueta + ":", valor);
    }
}
