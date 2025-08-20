package br.com.cbd.gestor_clientes;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Gestor Clientes API", version = "1.0", description = "Api de Gestor de Clientes"))
public class GestorClientesApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestorClientesApplication.class, args);
	}

}
