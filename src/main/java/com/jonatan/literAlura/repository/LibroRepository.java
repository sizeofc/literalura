package com.jonatan.literAlura.repository;

import com.jonatan.literAlura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro,Integer> {

    public Optional<Libro> findByTituloContainsIgnoreCase(String titulo);

}
