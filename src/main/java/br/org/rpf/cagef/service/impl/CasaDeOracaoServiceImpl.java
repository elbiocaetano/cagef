package br.org.rpf.cagef.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.org.rpf.cagef.entity.CasaDeOracao;
import br.org.rpf.cagef.entity.Cidade;
import br.org.rpf.cagef.repository.CasaDeOracaoRepository;
import br.org.rpf.cagef.service.CasaDeOracaoService;

@Service
public class CasaDeOracaoServiceImpl implements CasaDeOracaoService {

	@Autowired
	private CasaDeOracaoRepository casaDeOracaoRepository;

	public Page<CasaDeOracao> buscar(String codRelatorio, String cidade, String bairro, int offset, int limit, String orderBy,
			String direction) {

		return casaDeOracaoRepository.findAll(
				Example.of(new CasaDeOracao(codRelatorio, bairro, new Cidade(cidade)), getExampleMatcher()),
				PageRequest.of(offset, limit, Direction.fromString(direction), orderBy));
	}
	
	public CasaDeOracao byId(String id) {
		return casaDeOracaoRepository.findById(id).orElse(null);
	}
	
	@Override
	public CasaDeOracao salvar(CasaDeOracao casaDeOracao) {
		return casaDeOracaoRepository.save(casaDeOracao);
	}

	private ExampleMatcher getExampleMatcher() {
		return ExampleMatcher.matching().withIgnoreCase()
				.withMatcher("codRelatorio", new GenericPropertyMatcher().startsWith())
				.withMatcher("cidade.nome", new GenericPropertyMatcher().startsWith())
				.withMatcher("bairro", new GenericPropertyMatcher().startsWith());
	}
}
