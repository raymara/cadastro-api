package com.example.cadastro.api.repositorio.pessoa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.example.cadastro.api.modelo.Pessoa;
import com.example.cadastro.api.repositorio.filtro.PessoaFiltro;

public class PessoaRepositorioImpl implements PessoaRepositorioQuery {
	@PersistenceContext
    private EntityManager manager;
	
	@Override
    public List<Pessoa> filtrar(PessoaFiltro filtro) {
        // Select p From Pessoa p
        // 1 - Usamos o CriteriaBuilder(cb) para criar a CriteriaQuery (cq)
        // com a tipagem do tipo a ser selecionado (Pessoa)
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Pessoa> cq = cb.createQuery(Pessoa.class);
        
        // 2 - clausula from e joins
        Root<Pessoa> pessoaRoot = cq.from(Pessoa.class);

        // 3 - adiciona as restrições (os predicados) que serão passadas na clausula where
        Predicate[] restricoes = this.criaRestricoes(filtro, cb, pessoaRoot);

        // 4 - monta a consulta com as restrições
        cq.select(pessoaRoot).where(restricoes).orderBy(cb.asc(pessoaRoot.get("nome")));

        // 5 - cria e executa a consula
        return manager.createQuery(cq).getResultList();
    }
	
	private Predicate[] criaRestricoes(PessoaFiltro filtro, CriteriaBuilder cb, Root<Pessoa> pessoaRoot) {
		List<Predicate> predicates = new ArrayList<>();

	    if (!StringUtils.isEmpty(filtro.getNome())) {
	    	predicates.add(cb.like(pessoaRoot.get("nome"),"%" + filtro.getNome() + "%" ));
	    }

	    return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	@Override
    public Page<Pessoa> filtrar(PessoaFiltro filtro, Pageable pageable) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Pessoa> cq = cb.createQuery(Pessoa.class);
        Root<Pessoa> pessoaRoot = cq.from(Pessoa.class);
        Predicate[] restricoes = this.criaRestricoes(filtro, cb, pessoaRoot);
        cq.where(restricoes);
        
        // Monta a consulta com as restrições de paginação
        TypedQuery<Pessoa> query = manager.createQuery(cq);
        adicionaRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(filtro));
    }
	
	private void adicionaRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
        Integer paginaAtual = pageable.getPageNumber();
        Integer totalObjetosPorPagina = pageable.getPageSize();
        Integer primeiroObjetoDaPagina = paginaAtual * totalObjetosPorPagina;

        // 0 a n-1, n - (2n -1), ...
        query.setFirstResult(primeiroObjetoDaPagina);
        query.setMaxResults(totalObjetosPorPagina);
    }

    private Long total(PessoaFiltro filtro) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Pessoa> pessoaRoot = cq.from(Pessoa.class);
        Predicate[] restricoes = criaRestricoes(filtro, cb, pessoaRoot);
        cq.where(restricoes);
        cq.select(cb.count(pessoaRoot));

        return manager.createQuery(cq).getSingleResult();
    }
}
