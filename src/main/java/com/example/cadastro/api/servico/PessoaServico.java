package com.example.cadastro.api.servico;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cadastro.api.modelo.Pessoa;
import com.example.cadastro.api.repositorio.PessoaRepositorio;
import com.example.cadastro.api.repositorio.filtro.PessoaFiltro;
import com.example.cadastro.api.servico.excecao.PessoaInexistenteOuInativaException;

@Service
public class PessoaServico {
	private final PessoaRepositorio pessoaRepositorio;

    @Autowired
    public PessoaServico(PessoaRepositorio pessoaRepositorio) {
        this.pessoaRepositorio = pessoaRepositorio;
    }

    Optional<Pessoa> buscaPor(String nome) {
        return Optional.ofNullable(pessoaRepositorio.findByNome(nome));
    }

    public Pessoa buscaPor(Long cpf) {
    	return pessoaRepositorio.findById(cpf).orElseThrow(() -> new EmptyResultDataAccessException(1));
    }
    
    public Pessoa buscaPorcpf(Long cpf) {
    	return pessoaRepositorio.findById(cpf).orElseThrow(() -> new PessoaInexistenteOuInativaException());
    }

    @Transactional
    public Pessoa salvar(Pessoa pessoa) {
        return this.pessoaRepositorio.save(pessoa);
    }

    @Transactional(readOnly = true)
    public List<Pessoa> buscaTodos() {
        return this.pessoaRepositorio.findAll();        
    }
    
    public List<Pessoa> pesquisa(PessoaFiltro filtro) {
        return pessoaRepositorio.filtrar(filtro);
    }
    
    public Page<Pessoa> pesquisa(PessoaFiltro filtro, Pageable pageable) {
        return pessoaRepositorio.filtrar(filtro, pageable);
    }
    
    @Transactional
    public void excluir(Long cpf) {
        this.pessoaRepositorio.deleteById(cpf);
    }

    @Transactional
    public Pessoa atualizar(Long cpf, Pessoa pessoa) {
    	Pessoa pessoaSalva = this.buscaPor(cpf);
        BeanUtils.copyProperties(pessoa, pessoaSalva, "cpf" );
        this.salvar(pessoaSalva);
        return pessoaSalva;
    }

	
}
