package cl.ciclospa.api;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@Transactional
public class ContenidoService {
    private final ContenidoRepository repository;
    public ContenidoService(ContenidoRepository repository) { this.repository = repository; }
    @Transactional(readOnly = true)
    public List<ContenidoResponse> listar() { return repository.findByPublicadoTrue().stream().map(ContenidoResponse::from).toList(); }
    @Transactional(readOnly = true)
    public ContenidoResponse obtener(Long id) { return ContenidoResponse.from(buscar(id)); }
    public ContenidoResponse crear(ContenidoRequest r) {
        var c = new ContenidoEducativo(r.titulo().trim(), r.categoria().trim(), r.descripcion().trim(), r.publicado());
        return ContenidoResponse.from(repository.save(c));
    }
    public ContenidoResponse actualizar(Long id, ContenidoRequest r) {
        var c = buscar(id); c.setTitulo(r.titulo().trim()); c.setCategoria(r.categoria().trim()); c.setDescripcion(r.descripcion().trim()); c.setPublicado(r.publicado());
        return ContenidoResponse.from(repository.save(c));
    }
    public void eliminar(Long id) { repository.delete(buscar(id)); }
    private ContenidoEducativo buscar(Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contenido no encontrado: " + id)); }
}
