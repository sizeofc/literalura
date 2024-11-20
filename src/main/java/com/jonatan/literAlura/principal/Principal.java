package com.jonatan.literAlura.principal;

import com.jonatan.literAlura.DTO.LibroDTO;
import com.jonatan.literAlura.model.Autor;
import com.jonatan.literAlura.model.DatoLibro;
import com.jonatan.literAlura.model.Libro;
import com.jonatan.literAlura.repository.AutorRepository;
import com.jonatan.literAlura.repository.LibroRepository;
import com.jonatan.literAlura.service.ConsumoApi;
import com.jonatan.literAlura.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private String url = "https://gutendex.com/books/";
    private Scanner teclado = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvierteDatos convertir = new ConvierteDatos();
    private List<Libro> listaLibros = new ArrayList<>();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public Principal() {
    }


    public void mostrarMenu() {
        String logo = """
                \u001B[36m                
                 __       __  .___________. _______ .______              ___       __       __    __  .______          ___     \s
                |  |     |  | |           ||   ____||   _  \\            /   \\     |  |     |  |  |  | |   _  \\        /   \\    \s
                |  |     |  | `---|  |----`|  |__   |  |_)  |          /  ^  \\    |  |     |  |  |  | |  |_)  |      /  ^  \\   \s
                |  |     |  |     |  |     |   __|  |      /          /  /_\\  \\   |  |     |  |  |  | |      /      /  /_\\  \\  \s
                |  `----.|  |     |  |     |  |____ |  |\\  \\----.    /  _____  \\  |  `----.|  `--'  | |  |\\  \\----./  _____  \\ \s
                |_______||__|     |__|     |_______|| _| `._____|   /__/     \\__\\ |_______| \\______/  | _| `._____/__/     \\__\\\s
                                                                                                                               \s
                \u001B[0m                
                """;
        String menu = """
                \u001B[32m
                ╔══════════════════════════════════════════════════╗
                ║                     MENU                         ║
                ╚══════════════════════════════════════════════════╝
                \u001B[0m
                [1] - Buscar Libro por Titulo
                [2] - Listar Libros Registrados
                [3] - Listar Autores Registrados
                [4] - Listar Autores vivos en un determinado año
                [5] - Listar Libros por Idioma
                [6] - Buscar Autor por nombre
                [7] - Listar Autores por rango de años de Nacimiento
                [8] - Top 1[0]Libros mas descargados
                [9] - Estadísticas
                [0] - Salir
                """;
        Integer opcion = -1;
        while (opcion != 0) {

            System.out.println(logo + "\n" + menu);
            try {
                opcion = teclado.nextInt();
                teclado.nextLine();

                switch (opcion) {
                    case 1 -> buscarLibroPorTitulo();
                    case 2 -> listarLibrosRegistrados();
                    case 3 -> listarAutoresRegistrados();
                    case 4 -> listarAutoresVivosPornio();
                    case 5 -> listarLibrosPorIdioma();
                    case 6 -> buscarAutorPorNomnbre();
                    case 7 -> listarAutoresPorRangoAnioNacimiento();
                    case 8 -> top10Libros();
                    case 9 -> estadisticas();
                    case 0 -> System.out.println("Saliendo de la aplicacion");
                }
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }

        }
    }


    public LibroDTO getDatosLibros() {
        System.out.println("Ingrese el nombre del libro a buscar:");
        String nombreLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(url + "?search=" + nombreLibro.replace(" ", "+"));
        DatoLibro datos = convertir.ObtenerDatos(json, DatoLibro.class);

        //optener el primer resultado
        return datos.results().stream()
                .filter(t -> t.titulo().toUpperCase().contains(nombreLibro.toUpperCase()))
                .findFirst()
                .orElse(null);

    }

    private void buscarLibroPorTitulo() {
        LibroDTO libroBuscado = getDatosLibros();

        if (libroBuscado == null) {
            System.out.println("""
                    ╔══════════════════════════════════════════════════╗
                    ║               LIBRO NO ENCONTRADO                ║
                    ╚══════════════════════════════════════════════════╝
                    """);

        } else {
            //consulto si existe en la base de datos
            Optional<Libro> libroGuardado = libroRepository.findByTituloContainsIgnoreCase(libroBuscado.titulo());

            if (libroGuardado.isPresent()) {
                var libro = libroGuardado.get();
                mostrarLibro(libro);
            } else {
                Libro nuevoLibro = new Libro(libroBuscado);
                Autor nuevoAutor = new Autor(libroBuscado.autores().get(0));
                autorRepository.save(nuevoAutor);
                nuevoLibro.setAutor(nuevoAutor);
                libroRepository.save(nuevoLibro);
                mostrarLibro(nuevoLibro);

            }
        }

    }

    private void mostrarLibro(Libro l) {
        String subTitulo;
        String formatoLibro = """
                 ╔════════════════════════════════════════════════════════════════════════════════╗
                 ║ Titulo: %s║
                 ║ Autor:  %s║
                 ║ N° Descargas: %s║
                 ║ Idioma: %s║
                 ╚════════════════════════════════════════════════════════════════════════════════╝
                """;
        var lengthIdiomas = l.getIdiomas().stream()
                .mapToInt(String::length)
                .sum();
        if (l.getIdiomas().size() > 1)
            lengthIdiomas += l.getIdiomas().size();

        if (l.getTitulo().length() > 59) {
            subTitulo = l.getTitulo().substring(0, 59);
        } else {
            subTitulo = l.getTitulo();
        }

        //formateo la salida para que el cuadro quede siempre bien

        System.out.printf(formatoLibro,
                subTitulo + " ".repeat(71 - subTitulo.length()),
                l.getAutor().getNombre() + " ".repeat(71 - l.getAutor().getNombre().length()),
                l.getNumerosDescaargas() + " ".repeat(65 - (l.getNumerosDescaargas() + "").length()),
                l.getIdiomas() + " ".repeat(69 - lengthIdiomas));

    }

    private void listarLibrosRegistrados() {
        List<Libro> listaLibros = libroRepository.findAll();

        mostrarTitulo("         LIBROS REGISTRADOS");
        listaLibros.stream()
                .forEach(l -> mostrarLibro(l));

        System.out.println("presione una tecla para continuar...");
        var a = teclado.nextLine();
    }

    private void listarAutoresRegistrados() {
        List<Autor> listaAutores = autorRepository.findAll();

        mostrarTitulo("         AUTORES REGISTRADOS");
        listaAutores.stream()
                .forEach(a -> mostrarAutor(a));
        System.out.println("presione una tecla para continuar...");
        var a = teclado.nextLine();
    }

    private void mostrarAutor(Autor l) {
        String formatoLibro = """
                 ╔══════════════════════════════════════════════════════════════════════╗
                 ║ Nombre: %s║
                 ║ Nacimiento:  %s║
                 ║ Fallecimiento: %s║
                 ╚══════════════════════════════════════════════════════════════════════╝
                """;

        //formateo la salida para que el cuadro quede siempre bien
        System.out.printf(formatoLibro,
                l.getNombre() + " ".repeat(61 - (l.getNombre().length())),
                l.getAnioNacimiento() + " ".repeat((56 - (l.getAnioNacimiento() + "").length())),
                l.getFechaMuerte() + " ".repeat((54 - (l.getFechaMuerte() + "").length()))

        );

    }

    //[4] - Listar Autores vivos en un determinado año

    /**
     * nacio: 1860
     * murio: 1920
     * anioBuscado: 1890
     * nacio<anioBuscado
     * murio >anioBuscado or murio=null
     */
    public void listarAutoresVivosPornio() {
        mostrarTitulo("AUTORES VIVOS EN UN AÑO DETERMINADO");
        System.out.println("Ingrese el año: ");
        Integer anio = teclado.nextInt();
        List<Autor> listaAutoresVivosPorAnio = autorRepository.autoresVivosPorAnio(anio);
        if (listaAutoresVivosPorAnio != null) {
            System.out.printf("Autores que vivieron en %d \n", anio);
            listaAutoresVivosPorAnio.forEach(System.out::println);
        }
    }

    private void listarLibrosPorIdioma() {
        mostrarTitulo("LIBROS POR IDIOMAS");

        System.out.println("Ingrese las dos primeras iniciales del idioma ejemplo:");
        var idiomas = """
                es [Español]    en [English]
                fr [Frances]    ch [Chino]
                """;
        System.out.println(idiomas);

        var opc = teclado.nextLine();

        if (opc.length() == 0 || opc.length() > 2) {
            System.out.println("Debe ingresar solo dos letras, ejemplo: es");
        } else {
            List<Libro> librosPorIdiomas = libroRepository.findLibroByIdiomasContains(opc.toLowerCase());

            if (!librosPorIdiomas.isEmpty()) {
                librosPorIdiomas.forEach(l -> mostrarLibro(l));
            } else {
                System.out.println("no hay libros guardados en ese idioma!");
            }
        }
    }

    private void buscarAutorPorNomnbre() {
        mostrarTitulo("AUTORES POR NOMBRE");

        System.out.println("Ingrese el nombre del autor: ");
        var nombreAutor = teclado.nextLine();
        List<Autor> autores = autorRepository.findAutorByNombreContainingIgnoreCase(nombreAutor);
        if (!autores.isEmpty()) {
            autores.forEach(a -> mostrarAutor(a));
        } else {
            System.out.println("No se encontro autor!");
        }
    }

    private void listarAutoresPorRangoAnioNacimiento() {
        mostrarTitulo("AUTORES NACIDOS ENTRE UN RANGO DE AÑOS");

        System.out.println("Ingrese año nacimiento inicio:");
        var anioNacimiento = teclado.nextInt();
        System.out.println("Ingrese año nacimiento final");
        var anioNacimientoFinal = teclado.nextInt();
        if (anioNacimientoFinal < anioNacimiento) {
            System.out.println("Error: el segundo año no puede ser menor al primer año de nacimiento");
            return;
        }
        List<Autor> autores = autorRepository.findAutorByAnioNacimientoBetween(anioNacimiento, anioNacimientoFinal);
        if (!autores.isEmpty()) {
            autores.forEach(a -> mostrarAutor(a));
        } else {
            System.out.println("No se encontraron autores nacidos entre esos años");
        }
    }

    private void top10Libros() {
        List<Libro> top10Libro = libroRepository.findTop10ByOrderByNumeroDescargas();
        mostrarTitulo("TOP 10 DE LIBROS");
        top10Libro.forEach(l -> mostrarLibro(l));
    }

    private void estadisticas() {
        List<Libro> libros = libroRepository.findAll();

        IntSummaryStatistics stats = libros.stream()
                .mapToInt(Libro::getNumerosDescaargas)
                .summaryStatistics();

        mostrarTitulo("ESTADISTICAS DE LIBROS");
        // Mostrar estadísticas
        var recuadro = """
                 ╔══════════════════════════════════════════════════╗
                 ║ Total de Descargas   : %s║
                 ║ Promedio de Descargas: %s║
                 ║ Minimo de Descargas  : %s║
                 ║ Maximo de Descargas  : %s║
                 ║ Numeros de Libros    : %s║
                 ╚══════════════════════════════════════════════════╝
                """;
        System.out.printf(recuadro,
                stats.getSum() + " ".repeat(26 - (stats.getSum() + "").length()),
                stats.getAverage() + " ".repeat(26 - (stats.getAverage() + "").length()),
                stats.getMin() + " ".repeat(26 - (stats.getMin() + "").length()),
                stats.getMax() + " ".repeat(26 - (stats.getMax() + "").length()),
                stats.getCount() + " ".repeat(26 - (stats.getCount() + "").length()));

    }

    private void mostrarTitulo(String titulo) {

        System.out.printf("""
                        \u001B[36m
                                ╔══════════════════════════════════════════════════╗
                                ║%s║
                                ╚══════════════════════════════════════════════════╝
                                \u001B[0m
                                """,
                titulo + " ".repeat(50 - titulo.length()));
    }
}
