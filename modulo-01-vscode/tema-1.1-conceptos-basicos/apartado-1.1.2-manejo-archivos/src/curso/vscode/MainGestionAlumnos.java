package curso.vscode;

/**
 * Programa principal del ejercicio 1.1.2.
 *
 * Usa las clases Alumno y GestionAlumnos, de modo que hay varias clases
 * relacionadas entre las que navegar desde VS Code.
 */
public class MainGestionAlumnos {

    public static void main(String[] args) {
        // 1. Crear el gestor de alumnos.
        GestionAlumnos gestion = new GestionAlumnos();

        // 2. Registrar tres alumnos ficticios.
        gestion.registrarAlumno(new Alumno(1, "Laura Martín", "laura.martin@example.com"));
        gestion.registrarAlumno(new Alumno(2, "Carlos Ruiz", "carlos.ruiz@example.com"));
        gestion.registrarAlumno(new Alumno(3, "Elena Gómez", "elena.gomez@example.com"));

        // 3. Mostrar el listado completo.
        System.out.println("Listado de alumnos registrados:");
        for (Alumno alumno : gestion.listarAlumnos()) {
            System.out.println("  " + alumno);
        }

        // 4. Buscar un alumno por su identificador.
        int idBuscado = 2;
        System.out.println();
        System.out.println("Búsqueda del alumno con id " + idBuscado + ":");
        gestion.buscarPorId(idBuscado).ifPresentOrElse(
                alumno -> System.out.println("  Encontrado: " + alumno.getNombre()),
                () -> System.out.println("  No existe ningún alumno con ese id."));

        // 5. Mostrar el número total de alumnos.
        System.out.println();
        System.out.println("Total de alumnos: " + gestion.contarAlumnos());
    }
}
