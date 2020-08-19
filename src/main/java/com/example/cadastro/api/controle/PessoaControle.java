package com.example.cadastro.api.controle;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.cadastro.api.controle.evento.RecursoCriadoEvent;
import com.example.cadastro.api.modelo.Pessoa;
import com.example.cadastro.api.repositorio.filtro.PessoaFiltro;
import com.example.cadastro.api.servico.PessoaServico;


@RestController
@RequestMapping("/pessoas")
public class PessoaControle {
	
	private final PessoaServico pessoaServico;
	
	@Autowired
	private ApplicationEventPublisher publicar;
	
	@Autowired
    public PessoaControle(PessoaServico pessoaServico) {
        this.pessoaServico = pessoaServico;
    }
	
	@PostMapping
    public ResponseEntity<?> salvar(@Validated @RequestBody Pessoa pessoa, HttpServletResponse response) {
		Pessoa pessoaSalva = pessoaServico.salvar(pessoa);
        publicar.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getCpf()));

        return  ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
    }
	
	@GetMapping
    public List<Pessoa> pesquisar(PessoaFiltro filtro) {
        return pessoaServico.pesquisa(filtro);
    }
	
	@GetMapping("/paginacao")
    public Page<Pessoa> pesquisarComPaginacao(PessoaFiltro filtro, Pageable pageable) {
        return pessoaServico.pesquisa(filtro, pageable);
    }
	
	@GetMapping("/{cpf}")
	public ResponseEntity<?> buscarPorCodigo(@PathVariable Long cpf){
		Pessoa pessoa = pessoaServico.buscaPor(cpf);
		return pessoa != null ? ResponseEntity.ok(pessoa) : ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{cpf}")
    public ResponseEntity<Pessoa> atualizar(@PathVariable Long cpf, @Validated @RequestBody Pessoa pessoa) {
        Pessoa pessoaAtualizada = pessoaServico.atualizar(cpf, pessoa);
        return ResponseEntity.ok(pessoaAtualizada);
    }
	

	
	@DeleteMapping("/{cpf}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long codigo) {
		pessoaServico.excluir(codigo);
	}
	
}
