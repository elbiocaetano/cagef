package br.org.rpf.cagef.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.org.rpf.cagef.entity.Cidade;
import br.org.rpf.cagef.repository.CidadeRepository;
import br.org.rpf.cagef.service.CidadeService;

@Service
public class CidadeServiceImpl implements CidadeService {
	
	@Autowired
	private CidadeRepository cidadeRepository;

	public Page<Cidade> buscar(Long id, String nome, String estado, Boolean regional, int offset, int limit, String orderBy,
			String direction) {

		return cidadeRepository.findAll(
				Example.of(new Cidade(id, nome, estado, regional.booleanValue()), getExampleMatcher()),
				PageRequest.of(offset, limit, Direction.fromString(direction), orderBy));
	}
	
	public Cidade byId(Long id) {
		return cidadeRepository.findById(id).orElse(null);
	}

	private ExampleMatcher getExampleMatcher() {
		return ExampleMatcher.matching().withIgnoreCase()
                .withMatcher("idCidade", new GenericPropertyMatcher().contains())
				.withMatcher("nome", new GenericPropertyMatcher().contains())
				.withMatcher("estado", new GenericPropertyMatcher().contains());
				
	}
}
