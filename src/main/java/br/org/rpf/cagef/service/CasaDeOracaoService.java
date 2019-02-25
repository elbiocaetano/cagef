package br.org.rpf.cagef.service;

import org.springframework.data.domain.Page;

import br.org.rpf.cagef.entity.CasaDeOracao;

public interface CasaDeOracaoService {
	public Page<CasaDeOracao> buscar(String codRelatorio, String cidade, String bairro, int offset, int limit, String orderBy, String direction);
	
	public CasaDeOracao byId(String id);

	public CasaDeOracao salvar(CasaDeOracao casaDeOracao);
}
