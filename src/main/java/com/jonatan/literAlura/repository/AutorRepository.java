package com.jonatan.literAlura.repository;

import com.jonatan.literAlura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepository extends JpaRepository<Autor,Integer> {
}
