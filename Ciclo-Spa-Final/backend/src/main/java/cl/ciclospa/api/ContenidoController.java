package cl.ciclospa.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/contenidos")
public class ContenidoController {
    private final ContenidoService service;
    public ContenidoController(ContenidoService service) { this.service = service; }
    @GetMapping public List<ContenidoResponse> listar() { return service.listar(); }
    @GetMapping("/{id}") public ContenidoResponse obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public ContenidoResponse crear(@Valid @RequestBody ContenidoRequest request) { return service.crear(request); }
    @PutMapping("/{id}") public ContenidoResponse actualizar(@PathVariable Long id, @Valid @RequestBody ContenidoRequest request) { return service.actualizar(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
