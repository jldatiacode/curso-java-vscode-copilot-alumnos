package curso.java.web;

import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * GET /api/estado: comprueba que la aplicación está desplegada y en qué
 * servidor se ejecuta.
 */
@WebServlet("/api/estado")
public class EstadoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta) throws IOException {
        RepositorioTareas repositorio = (RepositorioTareas) getServletContext()
                .getAttribute(InicioAplicacion.ATRIBUTO_REPOSITORIO);

        String servidor = getServletContext().getServerInfo();
        String versionServlet = getServletContext().getMajorVersion() + "." + getServletContext().getMinorVersion();

        String json = "{\"estado\":\"OK\",\"servidor\":" + Json.texto(servidor) + ",\"servlet\":"
                + Json.texto(versionServlet) + ",\"tareas\":" + repositorio.contar() + "}";

        respuesta.setContentType("application/json");
        respuesta.setCharacterEncoding("UTF-8");
        respuesta.getWriter().write(json);
    }
}
