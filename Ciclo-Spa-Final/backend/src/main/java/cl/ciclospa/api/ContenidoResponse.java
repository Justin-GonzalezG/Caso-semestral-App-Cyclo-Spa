package cl.ciclospa.api;

public record ContenidoResponse(Long id, String titulo, String categoria, String descripcion, boolean publicado) {
    public static ContenidoResponse from(ContenidoEducativo c) {
        return new ContenidoResponse(c.getId(), c.getTitulo(), c.getCategoria(), c.getDescripcion(), c.isPublicado());
    }
}
