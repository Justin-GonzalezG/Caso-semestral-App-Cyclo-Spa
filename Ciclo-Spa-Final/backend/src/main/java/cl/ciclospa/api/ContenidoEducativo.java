package cl.ciclospa.api;

import jakarta.persistence.*;

@Entity
@Table(name = "contenidos_educativos")
public class ContenidoEducativo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String titulo;
    @Column(nullable = false, length = 60)
    private String categoria;
    @Column(nullable = false, length = 4000)
    private String descripcion;
    @Column(nullable = false)
    private boolean publicado = true;

    protected ContenidoEducativo() {}
    public ContenidoEducativo(String titulo, String categoria, String descripcion, boolean publicado) {
        this.titulo = titulo; this.categoria = categoria; this.descripcion = descripcion; this.publicado = publicado;
    }
    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getCategoria() { return categoria; }
    public String getDescripcion() { return descripcion; }
    public boolean isPublicado() { return publicado; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setPublicado(boolean publicado) { this.publicado = publicado; }
}
