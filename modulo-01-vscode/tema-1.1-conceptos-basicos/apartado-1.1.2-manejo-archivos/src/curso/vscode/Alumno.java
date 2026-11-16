package curso.vscode;

/**
 * Representa a un alumno del curso.
 *
 * Es una clase sencilla con tres datos: identificador, nombre y correo
 * electrónico. Los atributos son final porque no cambian una vez creado el
 * objeto.
 */
public class Alumno {

    // Identificador numérico único del alumno.
    private final int id;

    // Nombre completo del alumno.
    private final String nombre;

    // Correo electrónico de contacto (siempre ficticio en los ejemplos).
    private final String correo;

    /**
     * Crea un alumno con todos sus datos.
     *
     * @param id     identificador numérico del alumno
     * @param nombre nombre completo
     * @param correo correo electrónico
     */
    public Alumno(int id, String nombre, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    /**
     * Devuelve una representación legible del alumno. Se usa al imprimirlo con
     * System.out.println.
     */
    @Override
    public String toString() {
        return "Alumno [id=" + id + ", nombre=" + nombre + ", correo=" + correo + "]";
    }
}
