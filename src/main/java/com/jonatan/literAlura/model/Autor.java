package com.jonatan.literAlura.model;

import com.jonatan.literAlura.DTO.AutorDTO;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer anioNacimiento;
    private Integer fechaMuerte;
    private String nombre;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros;

    public Autor() {
    }

    public Autor(AutorDTO d) {
        this.anioNacimiento = d.anioNacimiento();
        this.fechaMuerte = d.fechaMuerte();
        this.nombre = d.nombre();

    }

    public Autor(Autor autor) {
        this.anioNacimiento = autor.getAnioNacimiento();
        this.fechaMuerte = autor.getFechaMuerte();
        this.nombre = autor.getNombre();
    }

    public Integer getAnioNacimiento() {
        return anioNacimiento;
    }

    public void setAnioNacimiento(Integer anioNacimiento) {
        this.anioNacimiento = anioNacimiento;
    }

    public Integer getFechaMuerte() {
        return fechaMuerte;
    }

    public void setFechaMuerte(Integer fechaMuerte) {
        this.fechaMuerte = fechaMuerte;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {

        String salida = """
                  ╔════════════════════════════════════════════════════════════════════════╗
                  ║ Nombre: %s║
                  ║ Año Naci: %s║
                  ║ Año Muerte: %s║
                  ╚════════════════════════════════════════════════════════════════════════╝
                """.formatted(nombre+" ".repeat(63-nombre.length()),
                anioNacimiento+" ".repeat(61-(anioNacimiento+"").length()),
                fechaMuerte+" ".repeat(59-(fechaMuerte+"").length()));
        return salida;

    }
}

