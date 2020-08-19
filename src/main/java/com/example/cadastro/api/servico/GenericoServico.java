package com.example.cadastro.api.servico;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cadastro.api.modelo.Pessoa;

import java.util.Optional;

public class GenericoServico<T> {
	private final JpaRepository<T, Long> repositorio;

    public GenericoServico(JpaRepository<T, Long> repositorio) {
        this.repositorio = repositorio;
    }

    T salvar(T entidade) {
        return repositorio.save(entidade);
    }

    List<T> buscaTodos() {
        return repositorio.findAll();
    }

    T atualizar(T entidade, Long codigo) {
        T entidadeDoBanco = this.buscaPor(codigo);
        BeanUtils.copyProperties(entidade, entidadeDoBanco, "codigo" );
        this.salvar(entidadeDoBanco);
        return entidadeDoBanco;
    }

    T buscaPor(Long codigo) {
        return repositorio.findById(codigo).orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    public void excluir(Long codigo) {
        this.repositorio.deleteById(codigo);
    }
}
