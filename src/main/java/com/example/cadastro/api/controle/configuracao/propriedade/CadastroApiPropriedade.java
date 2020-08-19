package com.example.cadastro.api.controle.configuracao.propriedade;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("cadastro")
public class CadastroApiPropriedade {
	
	private String originPermitida = "http://localhost:2400";
	
	private final Seguranca seguranca = new Seguranca();
		
	public String getOriginPermitida() {
		return originPermitida;
	}

	public void setOriginPermitida(String originPermitida) {
		this.originPermitida = originPermitida;
	}

	public Seguranca getSeguranca() {
		return seguranca;
	}

	public static class Seguranca {
		private boolean enableHttps;

		public boolean isEnableHttps() {
			return enableHttps;
		}

		public void setEnableHttps(boolean enableHttps) {
			this.enableHttps = enableHttps;
		}
		
	}

}
