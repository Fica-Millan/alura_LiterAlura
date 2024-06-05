

<p align="center">
  <img src="/img/LogoInicio.png" alt="logo literalura"/>
</p>

LiterAlura es un catálogo de libros interactivo que permite a los 
usuarios buscar libros y autores a través de una API específica y guardar
los resultados en una base de datos relacional.

## Descripción

Este proyecto tiene como objetivo interactuar con los usuarios a través 
de la consola, proporcionando 9 opciones de interacción. 
Los libros se buscan a través de la API de [Gutendex](https://gutendex.com/?ref=public_apis).

## Estructura del Proyecto LiterAlura

El proyecto se divide en varios paquetes para mantenerlo modular:

- `model`: Contiene los registros Datos, DatosAutor y DatosLibros, así como la clase Libro y Autor, y un enum de Idiomas.
- `principal`: Contiene la clase Principal, que maneja la interacción con el usuario y llama a los métodos correspondientes para cada opción del menú. 
- `repository`: Contiene las interfaces de los repositorios de Autor y Libro para interactuar con la base de datos.
- `service`: Contiene las clases de servicio que contienen la lógica de negocio.

## Funcionalidades

1. Buscar libro por título.
2. Listar libros registrados.
3. Listar autores registrados.
4. Listar autores vivos en un determinado año.
5. Listar libros por idioma.
6. Listar libros por título.
7. Listar autores por nombre.
8. Buscar los 10 libros más descargados de Gutendex.
9. Mostrar estadísticas de la base de datos.

## Tecnologías utilizadas

* Java
* Spring Boot
* Jakarta Annotations
* API de [Gutendex](https://gutendex.com/?ref=public_apis)

## Cómo ejecutar el proyecto

Para ejecutar este proyecto, necesitarás tener instalado Java y Spring Boot en tu equipo. Te recomiendo usar un IDE como IntelliJ IDEA o Eclipse.

1. Clona el repositorio a tu equipo local.
2. Importa el proyecto a tu IDE.
3. Ejecuta el proyecto.
4. Sigue las instrucciones en la consola para interactuar con la aplicación.

## Código principal

En esta sección, se aborda la clase `Principal`, que desempeña un papel fundamental en la interacción entre el usuario y el sistema. A continuación, se detalla cómo esta clase interactúa con otras partes del proyecto:

La clase `Principal` se encuentra en el paquete `principal` del proyecto y actúa como el punto de entrada principal para la interacción del usuario con la aplicación. Aquí hay una descripción detallada de algunas características clave de esta clase:

Componente principal: La clase `Principal` está anotada con `@Component`, lo que la convierte 
en un componente de Spring y la administra automáticamente el contenedor de Spring.

Inicialización de objetos: El método `init()` se anota con `@PostConstruct`, lo que significa que se 
ejecuta después de que se haya completado la inicialización de la clase y sus dependencias. 
En este método, se inicializan objetos importantes como `Scanner`, `ConsumoAPI` y `ConvierteDatos`.

Interfaz de usuario: El método `muestraMenu()` presenta un menú interactivo que permite al usuario seleccionar diferentes opciones para interactuar con la aplicación. Este método maneja la entrada del usuario y llama a los métodos correspondientes para cada opción del menú.

Llamadas a otros servicios: La clase `Principal` interactúa con otros servicios del proyecto, como
`libroService`, `autorService`, y `estadisticasService`, para implementar la lógica de negocio 
correspondiente a cada opción del menú. Por ejemplo, al seleccionar la opción para listar libros 
registrados, se llama al método `listarLibrosRegistrados()` del servicio de libros (`libroService`), 
que a su vez interactúa con el repositorio de libros (`librosRepository`) para recuperar los libros de la base de datos.

### Algunos ejemplos del codigo utilizado es el siguiente:

#### Listar libros registrados
Muestra todos los libros que están registrados en la base de datos. Esto proporciona al usuario una visión general de todos los libros disponibles en el catálogo
```java
    //METODO PARA LISTAR LOS LIBROS REGISTRADOS EN LA BASE DE DATOS
    private void listarLibrosRegistrados() {
        List<Libro> libros = librosRepository.findAll();
        libros.forEach(System.out::println);
    }
```
![lista_libros_cargados](/img/LibrosCargados.jpg)

#### Listar Libros según su título
Permite al usuario buscar un libro específico ingresando su título. Esto facilita la búsqueda de libros específicos en el catálogo.
```java
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
```
![listar_libros_por_titulo](/img/LibrosPorTitulo1.jpg)
![listar_libros_por_titulo](/img/LibrosPorTitulo2.jpg)

#### Listar Libros según su idioma
Permite al usuario buscar libros escritos en un idioma específico. Esto facilita la búsqueda de libros en un idioma particular de interés para el usuario.
```java
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
```
![libros_por_idioma](/img/LibrosPorIdioma.jpg)

#### Listar Autores Registrados
Muestra una lista de todos los autores que están registrados en la base de datos. Esto permite al usuario explorar los distintos autores cuyos libros están disponibles en el catálogo.
```java
    //METODO PARA LISTAR LOS AUTORES REGISTRADOS EN LA BASE DE DATOS
    private void listarAutoresRegistrados() {
        // Obtener y ordenar la lista de autores a través del servicio
        List<String> sortedAutores = autorService.listarAutoresRegistrados();

        // Imprimir los autores ordenados
        System.out.println("\nLISTADO DE AUTORES REGISTRADOS:\n──────────────────────────────");
        sortedAutores.forEach(System.out::println);
    }
```
![lista_autores_registrados](/img/AutoresRegistrados.jpg)

#### Listar Autores vivos en un año en particular
Permite al usuario buscar autores que estén vivos en un año específico. Esto puede ser útil para investigar qué autores estaban activos en un determinado período de tiempo.
```java
    //METODO PARA LISTAR AUTORES REGISTRADOS EN LA BASE DE DATOS VIVOS EN UN DETERMINADO AÑO
    private void listarAutoresVivosEnDeterminadoAnio() {
        System.out.println("En esta opción podrá buscar autores vivos en un determinado año." +
                "\n¿Autores vivos en qué año desea encontrar?");
        int anio = teclado.nextInt();
        autorService.listarAutoresVivosEnAnio(anio);
    }
```
![lista_autores_vivos_en_determinado_anio](/img/AutoresVivos.jpg)

#### Listar Autores según su nombre
Permite al usuario buscar autores registrados en la base de datos por su nombre o apellido. Esto facilita la búsqueda de autores específicos en el catálogo.
```java
    //METODO PARA LISTAR AUTORES DE ACUERDO AL NOMBRE
    private void listarAutoresPorNombre() {
        System.out.println("En esta opción podrá buscar autores registrados en la base de datos. \n" +
                "¿El nombre o apellido de qué autor desea buscar?");
        String nombreAutor = teclado.nextLine();
        List<Autor> autores = autorService.listarAutoresPorNombre(nombreAutor);

        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores con el nombre: " + nombreAutor);
        } else {
            System.out.println("\nAUTOR ENCONTRADO:\n────────────────");
            autores.forEach(System.out::println);
        }
    }
```
![autores_por_nombre](/img/AutorPorNombre.jpg)

## Contribución
Si deseas contribuir a este proyecto, ¡eres bienvenido! Puedes enviar pull requests con mejoras o correcciones.

## Agradecimientos
Agradezco al **Programa ONE** de [Alura Latam](https://www.linkedin.com/company/alura-latam/) y [Oracle](https://www.linkedin.com/company/oracle/) por proporcionar el material y el contexto para desarrollar este proyecto.

## Autor
Este proyecto fue creado por [Fica](https://github.com/Fica-Millan).

¡Siéntete libre de contactarme si tienes alguna pregunta o sugerencia!

[LinkedIn](https://www.linkedin.com/in/yesica-fica-millan/)
