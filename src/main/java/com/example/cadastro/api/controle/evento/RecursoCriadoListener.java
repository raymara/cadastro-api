package com.example.cadastro.api.controle.evento;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class RecursoCriadoListener implements ApplicationListener<RecursoCriadoEvent> {
	
	@Override
	public void onApplicationEvent(RecursoCriadoEvent eventoRecursoCriado) {
		this.adicionaHeaderLocation(eventoRecursoCriado.getResponse(), eventoRecursoCriado.getCodigo());
	}

	private void adicionaHeaderLocation(HttpServletResponse response, Long codigo) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
				.path("/{codigo}")
	            .buildAndExpand(codigo)
	            .toUri();

	    response.setHeader("Location", uri.toString());
	}
}
