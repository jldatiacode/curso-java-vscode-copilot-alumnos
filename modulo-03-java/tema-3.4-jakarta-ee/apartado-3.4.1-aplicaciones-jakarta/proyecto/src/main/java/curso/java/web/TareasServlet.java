package curso.java.web;

import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * GET /api/tareas: devuelve el listado de tareas en JSON.
 */
@WebServlet("/api/tareas")
public class TareasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta) throws IOException {
        RepositorioTareas repositorio = (RepositorioTareas) getServletContext()
                .getAttribute(InicioAplicacion.ATRIBUTO_REPOSITORIO);

        respuesta.setContentType("application/json");
        respuesta.setCharacterEncoding("UTF-8");
        respuesta.getWriter().write(Json.tareas(repositorio.listar()));
    }
}
