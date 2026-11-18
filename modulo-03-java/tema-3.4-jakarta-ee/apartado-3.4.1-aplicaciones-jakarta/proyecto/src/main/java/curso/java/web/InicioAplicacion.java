package curso.java.web;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Se ejecuta una vez al desplegar la aplicación: crea el repositorio de tareas
 * y lo guarda en el contexto de la aplicación, compartido por todos los
 * Servlets.
 */
@WebListener
public class InicioAplicacion implements ServletContextListener {

    public static final String ATRIBUTO_REPOSITORIO = "repositorioTareas";

    @Override
    public void contextInitialized(ServletContextEvent evento) {
        evento.getServletContext().setAttribute(ATRIBUTO_REPOSITORIO, new RepositorioTareas());
        evento.getServletContext().log("Gestor de tareas web iniciado");
    }
}
