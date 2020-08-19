package com.example.cadastro.api.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cadastro.api.modelo.Pessoa;
import com.example.cadastro.api.repositorio.pessoa.PessoaRepositorioQuery;

@Repository
public interface PessoaRepositorio extends JpaRepository<Pessoa, Long>, PessoaRepositorioQuery {
	Pessoa findByNome(String nome);
}
