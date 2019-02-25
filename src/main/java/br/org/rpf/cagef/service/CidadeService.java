package br.org.rpf.cagef.service;

import org.springframework.data.domain.Page;

import br.org.rpf.cagef.entity.Cidade;

public interface CidadeService {
	public Page<Cidade> buscar(Long id, String nome, String estado, Boolean regional, int offset, int limit, String orderBy, String direction);
	
	public Cidade byId(Long id);

}
