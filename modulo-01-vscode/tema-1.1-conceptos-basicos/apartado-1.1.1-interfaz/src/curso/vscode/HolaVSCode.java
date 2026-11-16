// Paquete de la clase. Debe coincidir con la ruta de carpetas:
// src/curso/vscode/HolaVSCode.java
package curso.vscode;

/**
 * Primer programa del curso.
 *
 * Sirve como excusa para recorrer la interfaz de VS Code:
 * localizar el archivo en el Explorer, navegar por el editor,
 * ejecutarlo y observar su salida en el panel inferior.
 */
public class HolaVSCode {

    /**
     * Punto de entrada del programa.
     *
     * @param args argumentos de la línea de comandos (no se utilizan)
     */
    public static void main(String[] args) {
        // Datos de ejemplo: el nombre del alumno es ficticio.
        String nombreAlumno = "Laura Martín";
        String nombreCurso = "Migración a VS Code y uso de GitHub Copilot para desarrolladores Java";

        // Llamada al método auxiliar que muestra la bienvenida.
        mostrarBienvenida(nombreAlumno, nombreCurso);

        System.out.println("Programa ejecutado correctamente desde VS Code.");
    }

    /**
     * Muestra un mensaje de bienvenida por la salida estándar.
     *
     * @param alumno nombre del alumno
     * @param curso  nombre del curso
     */
    private static void mostrarBienvenida(String alumno, String curso) {
        System.out.println("Hola, " + alumno + ".");
        System.out.println("Te damos la bienvenida al curso: " + curso);
    }
}
