package com.aluracursos.literalura.service;
import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.model.Idiomas;
import com.aluracursos.literalura.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EstadisticasService {

    @Autowired
    private LibrosRepository librosRepository;

    @Autowired
    private AutorRepository autorRepository;

    public void mostrarEstadisticas() {
        System.out.println("La base de datos de LiterAlura contiene la siguiente información:\n");
        mostrarRecuentoTotalLibros();
        mostrarRecuentoTotalAutores();
        mostrarRecuentoLibrosPorIdioma();
        mostrarEstadisticasDescargasLibros();
    }

    private void mostrarRecuentoLibrosPorIdioma() {
        System.out.println("    * Recuento de libros por idioma:");

        Map<Idiomas, Long> recuentoPorIdioma = librosRepository.findAll().stream()
                .collect(Collectors.groupingBy(Libro::getIdioma, Collectors.counting()));

        recuentoPorIdioma.forEach((idioma, recuento) ->
                System.out.println("        - " + idioma.toString().toLowerCase()  + ": " + recuento));
    }

    private void mostrarRecuentoTotalLibros() {
        long recuentoTotal = librosRepository.count();
        System.out.println("    * Recuento total de libros cargados: " + recuentoTotal);
    }

    private void mostrarRecuentoTotalAutores() {
        List<Autor> autores = autorRepository.findAllWithoutDuplicates();
        long recuentoTotal = autores.size(); // Obtener el tamaño de la lista de autores únicos

        System.out.println("    * Recuento total de autores cargados: " + recuentoTotal);
    }

    private void mostrarEstadisticasDescargasLibros() {
        System.out.println("    * Datos estadísticos de descargas: ");

        DoubleSummaryStatistics estadisticasDescargas = librosRepository.findAll().stream()
                .mapToDouble(Libro::getNumeroDescargas)
                .filter(descargas -> descargas > 0)
                .summaryStatistics();

        System.out.println("        - Cantidad máxima: " + estadisticasDescargas.getMax());
        System.out.println("        - Cantidad mínima: " + estadisticasDescargas.getMin());
        System.out.println("        - Cantidad media: " + estadisticasDescargas.getAverage());
    }
}

