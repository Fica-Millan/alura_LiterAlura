package com.aluracursos.literalura.service;

import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    // METODO PARA BUSCAR LOS AUTORES REGISTRADOS EN LA BASE DE DATOS
    public List<String> listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();

        // Ordenar la lista de autores alfabéticamente y convertir a String
        return autores.stream()
                .sorted((p1, p2) -> p1.getNombre().compareToIgnoreCase(p2.getNombre()))
                .map(Autor::toString)
                .collect(Collectors.toList());
    }

    // METODO PARA DETERMINAR SI UN AUTOR ESTABA VIVO EN UN AÑO DTERMINADO
    public List<Autor> getAutoresVivosEnAnio(int anio) {
        return autorRepository.findAll().stream()
                .filter(autor -> autor.getFechaDeNacimiento() != null && autor.getFechaDeNacimiento() <= anio)
                .filter(autor -> autor.getFechaDeMuerte() == null || autor.getFechaDeMuerte() >= anio)
                .collect(Collectors.toList());
    }

    // METODO PARA BUSCAR AUTORES VIVOS EN UN DETERMINADO AÑO
    public void listarAutoresVivosEnAnio(int anio) {
        List<Autor> autoresVivos = getAutoresVivosEnAnio(anio);

        if (autoresVivos.isEmpty()) {
            System.out.println("\n/*/ EN LA BASE DE DATOS, NO HAY REGISTRO DE AUTORES VIVOS EN EL AÑO " + anio + ". /*/");
        } else {
            System.out.println("\nAUTORES VIVOS EN EL AÑO " + anio + ":\n────────────────────────────");
            autoresVivos.forEach(System.out::println);
        }
    }

    // METODO PARA BUSCAR AUTORES POR SU NOMBRE
    public List<Autor> listarAutoresPorNombre(String nombre) {
        return autorRepository.findByNombreContainingIgnoreCase(nombre);
    }

}
