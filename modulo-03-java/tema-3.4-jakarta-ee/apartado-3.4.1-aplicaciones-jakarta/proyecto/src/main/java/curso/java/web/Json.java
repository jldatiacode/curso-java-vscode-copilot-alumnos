package curso.java.web;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Conversión mínima a JSON, sin bibliotecas externas.
 *
 * En un proyecto real se usaría una biblioteca (Jakarta JSON Binding,
 * Jackson…); aquí se evita para que el WAR no tenga dependencias y el ejemplo
 * se centre en los Servlets.
 */
public final class Json {

    private Json() {
    }

    public static String texto(String valor) {
        StringBuilder resultado = new StringBuilder("\"");
        for (char c : valor.toCharArray()) {
            switch (c) {
            case '"' -> resultado.append("\\\"");
            case '\\' -> resultado.append("\\\\");
            case '\n' -> resultado.append("\\n");
            case '\r' -> resultado.append("\\r");
            case '\t' -> resultado.append("\\t");
            default -> resultado.append(c);
            }
        }
        return resultado.append('"').toString();
    }

    public static String tarea(Tarea tarea) {
        return "{\"id\":" + tarea.id() + ",\"titulo\":" + texto(tarea.titulo()) + ",\"estado\":"
                + texto(tarea.estado().name()) + "}";
    }

    public static String tareas(List<Tarea> tareas) {
        return tareas.stream().map(Json::tarea).collect(Collectors.joining(",", "[", "]"));
    }
}
