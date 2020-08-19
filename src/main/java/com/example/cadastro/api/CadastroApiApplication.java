package com.example.cadastro.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.cadastro.api.controle.configuracao.propriedade.CadastroApiPropriedade;

@SpringBootApplication
@EnableConfigurationProperties(CadastroApiPropriedade.class)
public class CadastroApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CadastroApiApplication.class, args);
	}

}
