package curso.docker.dto;

import curso.docker.model.EstadoTarea;
import jakarta.validation.constraints.NotNull;

/**
 * Nuevo estado de una tarea (cuerpo de PATCH /api/tareas/{id}/estado).
 */
public record CambiarEstadoRequest(@NotNull(message = "El estado es obligatorio") EstadoTarea estado) {
}
