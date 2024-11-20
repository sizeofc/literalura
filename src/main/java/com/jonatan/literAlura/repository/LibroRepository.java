package com.jonatan.literAlura.repository;

import com.jonatan.literAlura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro,Integer> {

    public Optional<Libro> findByTituloContainsIgnoreCase(String titulo);

//        @Query("SELECT l FROM Libro l WHERE :idioma MEMBER OF l.idiomas")
    List<Libro> findLibroByIdiomasContains(String idioma);

    @Query("SELECT l FROM Libro l ORDER BY l.numerosDescaargas DESC limit 10")
        List<Libro> findTop10ByOrderByNumeroDescargas();


}
