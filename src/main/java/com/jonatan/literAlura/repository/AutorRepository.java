package com.jonatan.literAlura.repository;

import com.jonatan.literAlura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor,Integer> {

//    @Query("SELECT a FROM Autor a WHERE :anioBuscado >= a.anioNacimiento AND (:anioBuscado <= a.fechaMuerte OR a.fechaMuerte IS NULL)")

    @Query("SELECT a FROM Autor a WHERE " +
            "(:anioBuscado BETWEEN a.anioNacimiento AND a.fechaMuerte) OR " +
            "(a.fechaMuerte IS NULL AND :anioBuscado >= a.anioNacimiento)")
    List<Autor> autoresVivosPorAnio(@Param("anioBuscado") Integer anioBuscado);

    List<Autor> findAutorByNombreContainingIgnoreCase(String nombre);

    List<Autor> findAutorByAnioNacimientoBetween(Integer inicio,Integer fin);
}
