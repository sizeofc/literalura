package com.jonatan.literAlura.model;

import com.jonatan.literAlura.DTO.LibroDTO;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String titulo;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> tematica;
    @ManyToOne
    private Autor autor;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> categorias;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> idiomas;
    private String formato;
    private Integer numerosDescaargas;

    public Libro() {
    }

    public Libro(LibroDTO libroDTO) {
        this.id= libroDTO.id();
        this.titulo= libroDTO.titulo();
        this.tematica= libroDTO.tematica();
        this.categorias= libroDTO.categorias();
        this.idiomas= libroDTO.idiomas();
        this.formato= libroDTO.formato();
        this.numerosDescaargas= libroDTO.numerosDescaargas();
        var d= libroDTO.autores().get(0);
        Autor nuevoAutor= new Autor(d);
        this.autor=nuevoAutor;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<String> getTematica() {
        return tematica;
    }

    public void setTematica(List<String> tematica) {
        this.tematica = tematica;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }


    public List<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public Integer getNumerosDescaargas() {
        return numerosDescaargas;
    }

    public void setNumerosDescaargas(Integer numerosDescaargas) {
        this.numerosDescaargas = numerosDescaargas;
    }
}
