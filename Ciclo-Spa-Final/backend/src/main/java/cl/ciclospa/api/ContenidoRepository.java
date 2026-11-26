package cl.ciclospa.api;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContenidoRepository extends JpaRepository<ContenidoEducativo, Long> {
    List<ContenidoEducativo> findByPublicadoTrue();
}
