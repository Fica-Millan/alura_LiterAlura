package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Idiomas;
import com.aluracursos.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibrosRepository extends JpaRepository<Libro,Long>  {
    List<Libro> findByIdioma(Idiomas idioma);
    List<Libro> findByTituloContainingIgnoreCase(String titulo);
}
