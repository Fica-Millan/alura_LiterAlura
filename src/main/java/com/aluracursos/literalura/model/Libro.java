package com.aluracursos.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name="libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "autor_id")
    private Autor autor;
    @Enumerated (EnumType.STRING)
    private Idiomas idioma;
    private Integer numeroDescargas;

    public Libro(){
    }

    public Libro(DatosLibros datosLibros) {
        this.titulo = datosLibros.titulo();
        this.autor = new Autor(datosLibros.autores().get(0));
        this.idioma = datosLibros.idiomas().get(0);
        this.numeroDescargas = datosLibros.numeroDescargas() != null ? datosLibros.numeroDescargas() : 0;
    }

    @Override
    public String toString() {
        return "\nTitulo: " + titulo + "\n" +
               "Autor: " + autor.toString() + "\n" +
               "Idiomas: " + idioma.toString() + "\n" +
               "Número de descargas: " + numeroDescargas +
               "\n\n────────────────────────────────────────────────────────────────────────────────────────────────────────";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Idiomas getIdioma() {
        return idioma;
    }

    public void setIdioma(Idiomas idioma) {
        this.idioma = idioma;
    }

    public Integer getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Integer numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }
}
