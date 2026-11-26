package cl.ciclospa.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ContenidoRequest(
    @NotBlank @Size(max = 100) String titulo,
    @NotBlank @Size(max = 60) String categoria,
    @NotBlank @Size(max = 4000) String descripcion,
    boolean publicado
) {}
