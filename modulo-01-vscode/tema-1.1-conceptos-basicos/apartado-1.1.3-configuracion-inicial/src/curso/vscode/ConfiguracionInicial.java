package curso.vscode;

import java.util.List;

/**
 * Programa de apoyo para el ejercicio 1.1.3.
 *
 * El código está pensado para observar el efecto de las preferencias del
 * editor: tiene varios niveles de indentación, un recorrido sobre una lista y
 * una línea de texto muy larga para probar el ajuste de línea (Word Wrap).
 */
public class ConfiguracionInicial {

    public static void main(String[] args) {
        String nombreEditor = "Visual Studio Code";
        int tamanioTabulacion = 4;

        // Línea larga a propósito para comprobar el efecto de Word Wrap.
        String descripcionLarga = "VS Code permite ajustar el tamaño de fuente, la tabulación, el ajuste de línea, el minimapa, la visualización de espacios y el guardado automático, tanto para todos los proyectos como para uno en concreto.";

        // Preferencias que se revisan durante la práctica.
        List<String> preferencias = List.of("editor.fontSize", "editor.tabSize", "editor.insertSpaces",
                "editor.wordWrap", "editor.minimap.enabled", "editor.renderWhitespace", "files.autoSave");

        System.out.println("Editor: " + nombreEditor);
        System.out.println("Tamaño de tabulación recomendado: " + tamanioTabulacion);
        System.out.println(descripcionLarga);
        System.out.println();

        // Recorrido sobre la colección con dos niveles de indentación dentro del bucle.
        System.out.println("Preferencias que se revisan en este ejercicio:");
        int numero = 1;
        for (String preferencia : preferencias) {
            if (preferencia.startsWith("editor.")) {
                mostrarPreferencia(numero, preferencia, "editor");
            } else {
                mostrarPreferencia(numero, preferencia, "archivos");
            }
            numero++;
        }

        System.out.println();
        System.out.println("Total de preferencias: " + preferencias.size());
    }

    /**
     * Muestra una preferencia numerada junto a su categoría.
     *
     * @param numero    posición de la preferencia en la lista
     * @param clave     nombre de la preferencia en settings.json
     * @param categoria grupo al que pertenece la preferencia
     */
    private static void mostrarPreferencia(int numero, String clave, String categoria) {
        System.out.println("  " + numero + ". " + clave + " (" + categoria + ")");
    }
}
