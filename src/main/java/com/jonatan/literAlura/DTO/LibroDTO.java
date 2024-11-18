package com.jonatan.literAlura.DTO;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LibroDTO(
        @JsonAlias("id") Integer id,
        @JsonAlias("title")          String titulo,
        @JsonAlias("subjects")       List<String> tematica,
        @JsonAlias("authors")        List<AutorDTO> autores,
        @JsonAlias("bookshelves")    List<String> categorias,
        @JsonAlias("languages")      List<String> idiomas,
        @JsonAlias("media_type")     String formato,
        @JsonAlias("download_count")  Integer numerosDescaargas

) {
}
