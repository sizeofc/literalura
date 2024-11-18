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
                                
                 __       __  .___________. _______ .______              ___       __       __    __  .______          ___     \s
                |  |     |  | |           ||   ____||   _  \\            /   \\     |  |     |  |  |  | |   _  \\        /   \\    \s
                |  |     |  | `---|  |----`|  |__   |  |_)  |          /  ^  \\    |  |     |  |  |  | |  |_)  |      /  ^  \\   \s
                |  |     |  |     |  |     |   __|  |      /          /  /_\\  \\   |  |     |  |  |  | |      /      /  /_\\  \\  \s
                |  `----.|  |     |  |     |  |____ |  |\\  \\----.    /  _____  \\  |  `----.|  `--'  | |  |\\  \\----./  _____  \\ \s
                |_______||__|     |__|     |_______|| _| `._____|   /__/     \\__\\ |_______| \\______/  | _| `._____/__/     \\__\\\s
                                                                                                                               \s
                                
                """;
        String menu = """
                ╔══════════════════════════════════════════════════╗
                ║                     MENU                         ║
                ╚══════════════════════════════════════════════════╝
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
                    case 2-> listarLibrosRegistrados();
                    case 3-> listarAutoresRegistrados();
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

        }else{
            Optional<Libro> libroGuardado= libroRepository.findByTituloContainsIgnoreCase(libroBuscado.titulo());

            if (libroGuardado.isPresent()){
                var libro= libroGuardado.get();
                mostrarLibro(libro);
            }else{
                Libro nuevoLibro= new Libro(libroBuscado);
                Autor nuevoAutor= new Autor(libroBuscado.autores().get(0));
                autorRepository.save(nuevoAutor);
                nuevoLibro.setAutor(nuevoAutor);
                libroRepository.save(nuevoLibro);
            }
        }

    }

    private void mostrarLibro(Libro l){
        String formatoLibro= """
                 ╔══════════════════════════════════════════════════════════════════════╗
                 ║ Titulo: %s║
                 ║ Autor:  %s║
                 ║ N° Descargas: %s║
                 ║ Idioma: %s║
                 ╚══════════════════════════════════════════════════════════════════════╝
                """;
        var lengthIdiomas= l.getIdiomas().stream()
                        .mapToInt(String::length)
                                .sum();
        //formateo la salida para que el cuadro quede siempre bien
        System.out.printf(formatoLibro,
                l.getTitulo()+" ".repeat(61-l.getTitulo().length()),
                l.getAutor().getNombre()+" ".repeat(61-l.getAutor().getNombre().length()),
                l.getNumerosDescaargas()+" ".repeat(55-(l.getNumerosDescaargas()+"").length()),
                l.getIdiomas()+" ".repeat(59-lengthIdiomas));
    }

    private void listarLibrosRegistrados(){
        List<Libro> listaLibros= libroRepository.findAll();
        listaLibros.stream()
                .forEach(l->mostrarLibro(l));
    }

    private void listarAutoresRegistrados(){
        List<Autor> listaAutores= autorRepository.findAll();
        listaAutores.stream()
                .forEach(a->mostrarAutor(a));
    }

    private void mostrarAutor(Autor l){
        String formatoLibro= """
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
}
