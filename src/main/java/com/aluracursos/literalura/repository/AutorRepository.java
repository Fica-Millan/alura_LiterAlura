package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor,Long>{
    List<Autor> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT DISTINCT a FROM Autor a")
    List<Autor> findAllWithoutDuplicates();
}
