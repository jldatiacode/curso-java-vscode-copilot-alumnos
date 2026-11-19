package curso.springboot.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import curso.springboot.dto.CambiarEstadoRequest;
import curso.springboot.dto.CrearTareaRequest;
import curso.springboot.model.Tarea;
import curso.springboot.service.TareaService;
import jakarta.validation.Valid;

/**
 * API REST de tareas.
 */
@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final TareaService servicio;

    // Inyección de dependencias por constructor: Spring aporta el TareaService.
    public TareaController(TareaService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Tarea> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public Tarea consultar(@PathVariable long id) {
        return servicio.buscar(id).orElseThrow(() -> noEncontrada(id));
    }

    @PostMapping
    public ResponseEntity<Tarea> crear(@Valid @RequestBody CrearTareaRequest datos) {
        Tarea creada = servicio.crear(datos);
        return ResponseEntity.created(URI.create("/api/tareas/" + creada.id())).body(creada);
    }

    @PatchMapping("/{id}/estado")
    public Tarea cambiarEstado(@PathVariable long id, @Valid @RequestBody CambiarEstadoRequest datos) {
        return servicio.cambiarEstado(id, datos.estado()).orElseThrow(() -> noEncontrada(id));
    }

    private ResponseStatusException noEncontrada(long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la tarea " + id);
    }
}
