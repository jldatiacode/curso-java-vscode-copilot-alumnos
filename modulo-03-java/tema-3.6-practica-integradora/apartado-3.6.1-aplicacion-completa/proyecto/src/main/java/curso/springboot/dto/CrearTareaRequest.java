package curso.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos para crear una tarea (cuerpo de POST /api/tareas).
 */
public record CrearTareaRequest(@NotBlank(message = "El título es obligatorio") @Size(max = 100) String titulo,
        @Size(max = 500) String descripcion) {
}
