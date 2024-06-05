package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.repository.*;
import com.aluracursos.literalura.service.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class Principal {
    private Scanner teclado;
    private ConsumoAPI consumoAPI;
    private ConvierteDatos conversor;
    private static final String URL_BASE = "https://gutendex.com/books/";
    private List<Libro> libro;

    //inyección de dependencias
    @Autowired
    private LibrosRepository librosRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LibroService libroService;

    @Autowired
    private AutorService autorService;

    @Autowired
    private EstadisticasService estadisticasService;

    // Inicialización de componentes necesarios
    @PostConstruct
    public void init() {
        teclado = new Scanner(System.in);
        consumoAPI = new ConsumoAPI();
        conversor = new ConvierteDatos();
    }

    // MENU CON EL QUE VA A INTERACTUAR EL USUARIO
    public void muestraMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    \nElija el número de la opción deseada:\n
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    6 - Listar libros por título
                    7 - Listar autores por nombre
                    8 - Buscar los 5 libros más descargados
                    9 - Mostrar estadisticas de la base de datos

                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosEnDeterminadoAnio();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 6:
                    listarLibrosPorTitulo();
                    break;
                case 7:
                    listarAutoresPorNombre();
                    break;
                case 8:
                    buscarTop5LibrosDescargados();
                    break;
                case 9:
                    mostrarEstadisticas();
                    break;
                case 0:
                    System.out.println("\n\nLa aplicación se esta cerrando...\n\n");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    // METODO PARA BUSCAR UN LIBRO EN LA API (POR TITULO O PARTE DE EL)
    public void buscarLibroPorTitulo() {
        System.out.println("Escribe el nombre del libro que deseas buscar: ");
        String tituloLibro = teclado.nextLine();
        String json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+").toLowerCase());

        libroService.buscarLibroPorTitulo(tituloLibro, json);
    }

    //METODO PARA LISTAR LOS LIBROS REGISTRADOS EN LA BASE DE DATOS
    private void listarLibrosRegistrados() {
        // Recuperar los libros almacenados en la base de datos y almacenarlos en la lista libros
        List<Libro> libros = librosRepository.findAll();
        libros.forEach(System.out::println);
    }

    //METODO PARA LISTAR LOS AUTORES REGISTRADOS EN LA BASE DE DATOS
    private void listarAutoresRegistrados() {
        // Obtener y ordenar la lista de autores a través del servicio
        List<String> sortedAutores = autorService.listarAutoresRegistrados();

        System.out.println("\nLISTADO DE AUTORES REGISTRADOS:\n──────────────────────────────");
        sortedAutores.forEach(System.out::println);
    }

    //METODO PARA LISTAR AUTORES REGISTRADOS EN LA BASE DE DATOS VIVOS EN UN DETERMINADO AÑO
    private void listarAutoresVivosEnDeterminadoAnio() {
        System.out.println("En esta opción podrá buscar autores vivos en un determinado año." +
                "\n¿Autores vivos en qué año desea encontrar?");

        int anio = teclado.nextInt();
        // Llama al servicio para listar los autores vivos en el año especificado
        autorService.listarAutoresVivosEnAnio(anio);
    }

    //METODO PARA LISTAR LIBROS DE ACUERDO AL IDIOMA
    private void listarLibrosPorIdioma() {
        System.out.println("En esta opción podrá buscar libros escritos en un determinado idioma. \n" +
                "¿En qué idioma desea buscar?");
        String idiomaStr = teclado.nextLine().toLowerCase(); // Convertir a minúsculas

        // Eliminar tildes
        idiomaStr = LibroService.eliminarTildes(idiomaStr);

        // Manejar el caso especial para "español"
        if ("español".equalsIgnoreCase(idiomaStr)) {
            idiomaStr = "CASTELLANO";
        }

        try {
            Idiomas idioma = Idiomas.valueOf(idiomaStr.toUpperCase()); // Convertir a objeto Idiomas
            libroService.listarLibrosPorIdioma(idioma);
        } catch (IllegalArgumentException e) {
            System.out.println("El idioma ingresado no es válido.");
        }
    }

    //METODO PARA LISTAR LIBROS DE ACUERDO A SU TITULO
    private void listarLibrosPorTitulo() {
        System.out.println("En esta opción podrá buscar libros registrados en la base de datos. \n" +
                "¿Qué titulo desea buscar?");
        String titulo = teclado.nextLine();
        List<Libro> libro = librosRepository.findByTituloContainingIgnoreCase(titulo);

        if (libro.isEmpty()) {
            System.out.println("No se encontraron libros con el titulo: " + titulo);
        } else {
            System.out.println("\nLIBRO ENCONTRADO:\n────────────────");
            libro.forEach(System.out::println);
        }
    }

    //METODO PARA LISTAR AUTORES DE ACUERDO AL NOMBRE
    private void listarAutoresPorNombre() {
        System.out.println("En esta opción podrá buscar autores registrados en la base de datos. \n" +
                "¿El nombre o apellido de qué autor desea buscar?");

        String nombreAutor = teclado.nextLine();

        // Llama al servicio para buscar autores por nombre
        List<Autor> autores = autorService.listarAutoresPorNombre(nombreAutor);

        // Verifica si la lista de autores está vacía
        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores con el nombre: " + nombreAutor);
        } else {
            System.out.println("\nAUTOR ENCONTRADO:\n────────────────");
            autores.forEach(System.out::println);
        }
    }

    //METODO PARA BUSCAR LOS 5 LIBRO MAS DESCARGADOS DE LA BASE DE DATOS
    private void buscarTop5LibrosDescargados() {
        // Obtener la lista de libros del repositorio
        List<Libro> libros = librosRepository.findAll();

        // Obtener los 5 libros más descargados del servicio
        List<Libro> top5Libros = libroService.obtenerTop5LibrosMasDescargados(libros);

        System.out.println("\nTop 5, libros más descargados.\nCANTIDAD     TITULOS\nVECES        MAS DESCARGADOS\n────────     ───────────");
        // Mostrar los títulos de los 5 libros más descargados
        top5Libros.forEach(libro -> System.out.println(libro.getNumeroDescargas() + "        " + libro.getTitulo().toUpperCase()));
    }

    //METODO PARA MOSTRAR ESTADISTICAS DE LA BASE DE DATOS
    private void mostrarEstadisticas() {
        // Llama al servicio de estadísticas para mostrar las estadísticas de la base de datos
        estadisticasService.mostrarEstadisticas();
    }
}


