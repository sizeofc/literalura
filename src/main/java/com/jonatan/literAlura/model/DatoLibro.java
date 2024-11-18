package com.jonatan.literAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jonatan.literAlura.DTO.LibroDTO;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatoLibro(
       @JsonAlias("results") List<LibroDTO> results
) {
}
