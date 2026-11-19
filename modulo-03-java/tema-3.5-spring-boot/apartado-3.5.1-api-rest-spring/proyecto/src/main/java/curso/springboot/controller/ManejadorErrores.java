package curso.springboot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduce las excepciones de negocio a respuestas HTTP.
 *
 * Los errores de validación (400) y las tareas inexistentes (404) los resuelve
 * Spring automáticamente en formato Problem Details (RFC 9457).
 */
@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail transicionNoPermitida(IllegalStateException error) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, error.getMessage());
        problema.setTitle("Cambio de estado no permitido");
        return problema;
    }
}
