package com.jonatan.literAlura;

import com.jonatan.literAlura.principal.Principal;
import com.jonatan.literAlura.repository.AutorRepository;
import com.jonatan.literAlura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiterAluraApplication implements CommandLineRunner {
	@Autowired
	LibroRepository libroRepository;
	@Autowired
	AutorRepository autorRepository;
	public static void main(String[] args) {
		SpringApplication.run(LiterAluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var principal= new Principal(libroRepository,autorRepository);
		principal.mostrarMenu();
	}
}
