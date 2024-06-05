    package com.aluracursos.literalura.service;

    import com.aluracursos.literalura.model.*;
    import com.aluracursos.literalura.repository.LibrosRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.text.Normalizer;
    import java.util.ArrayList;
    import java.util.Comparator;
    import java.util.List;
    import java.util.Optional;
    import java.util.regex.Pattern;

    @Service
    public class LibroService {

        @Autowired
        private LibrosRepository librosRepository;

        @Autowired
        private ConvierteDatos conversor;

        // METODO PARA BUSCAR UN LIBRO EN LA API (POR TITULO O PARTE DE EL)
        public void buscarLibroPorTitulo(String tituloLibro, String json) {
            try {
                // Convierte el JSON recibido en un objeto de la clase Datos
                Datos datos = conversor.obtenerDatos(json, Datos.class);

                // Busca un libro que "contenga" el título buscado, ignorando mayúsculas/minúsculas
                Optional<DatosLibros> libroBuscado = datos.resultados().stream()
                        .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                        .findFirst();

                // Si el libro fue encontrado, guardarlo en la base de datos
                if (libroBuscado.isPresent()) {
                    // Obtiene el objeto DatosLibros del Optional
                    DatosLibros datosLibros = libroBuscado.get();

                    // Crea un objeto Libro usando el constructor que toma un objeto DatosLibros
                    Libro libro = new Libro(datosLibros);

                    // Imprime los datos del libro en la consola
                    System.out.println("\nDATOS DEL LIBRO:\n───────────────\n" + libro.toString());

                    // Guarda el libro en la base de datos utilizando el repositorio
                    librosRepository.save(libro);
                    System.out.println("\n /*/ LIBRO GUARDADO EN LA BASE DE DATOS /*/");
                } else {
                    // Si no se encuentra el libro, imprime un mensaje indicando que no fue encontrado
                    System.out.println("\n /*/ LIBRO NO ENCONTRADO /*/");
                }
            } catch (Exception e) {
                // Manejo de cualquier otro error inesperado
                System.err.println("Ocurrió un error inesperado: " + e.getMessage());
            }
        }

        // METODO PARA ELIMIRAR TILDES CUANDO SE BUSCA POR IDIOMA
        public static String eliminarTildes(String input) {
            String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(normalized).replaceAll("");
        }

        // METODO PARA BUSCAR LIBROS DE ACUERDO AL IDIOMA
        public List<Libro> listarLibrosPorIdioma(Idiomas idioma) {
            List<Libro> libros = librosRepository.findByIdioma(idioma);
            if (libros.isEmpty()) {
                System.out.println("/*/ EN LA BASE DE DATOS NO HAY REGISTRO DE LIBROS DISPONIBLES EN EL IDIOMA " + idioma);
            } else {
                System.out.println("\nLISTADO DE LIBROS EN IDIOMA " + idioma + ":\n──────────────────────────────");
                libros.forEach(System.out::println);
            }
            return libros;
        }

        //METODO PARA BUSCAR LOS 5 LIBROS MAS DESCARGADOS DE GUTENDEX
        public List<Libro> obtenerTop5LibrosMasDescargados(List<Libro> libros) {
            // Ordenar los libros por número de descargas
            libros.sort(Comparator.comparingInt(Libro::getNumeroDescargas).reversed());

            // Limitar a los primeros 5 libros
            return libros.subList(0, Math.min(libros.size(), 5));
        }


    }