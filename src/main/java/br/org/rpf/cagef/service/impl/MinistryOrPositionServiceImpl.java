package br.org.rpf.cagef.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.org.rpf.cagef.dto.ministryorposition.MinistryOrPositionDTO;
import br.org.rpf.cagef.entity.MinistryOrPosition;
import br.org.rpf.cagef.repository.MinistryOrPositionRepository;
import br.org.rpf.cagef.service.MinistryOrPositionService;

@Service
public class MinistryOrPositionServiceImpl implements MinistryOrPositionService {

	@Autowired
	private MinistryOrPositionRepository ministryOrPositionRepository;
	
	@Override
	public Page<MinistryOrPosition> findAll(Long id, String description, Integer offset, Integer limit, String orderBy,
			String direction) {
		return this.ministryOrPositionRepository.findAll(
				Example.of(new MinistryOrPosition(id, description), getExampleMatcher()),
				PageRequest.of(offset, limit, Direction.fromString(direction), orderBy));
	}
	
	@Override
	public MinistryOrPosition byId(Long id) {
		return this.ministryOrPositionRepository.findById(id).orElseThrow(() -> new org.hibernate.ObjectNotFoundException(id, MinistryOrPosition.class.getName()));
	}
	
	@Override
	public MinistryOrPosition save(MinistryOrPositionDTO ministryOrPositionDTO) {
		return this.ministryOrPositionRepository.save(fromDTO(null, ministryOrPositionDTO));
	}
	
	@Override
	public MinistryOrPosition update(Long id, MinistryOrPositionDTO ministryOrPositionDTO) {
		return this.ministryOrPositionRepository.save(fromDTO(id, ministryOrPositionDTO));
	}
	
	@Override
	public void remove(Long id) {
		this.ministryOrPositionRepository.deleteById(id);
	}

	private MinistryOrPosition fromDTO(Long id, MinistryOrPositionDTO ministryOrPositionDTO) {
		return new MinistryOrPosition(id, ministryOrPositionDTO.getDescription());
	}

	private ExampleMatcher getExampleMatcher() {
		return ExampleMatcher.matching().withIgnoreCase().withMatcher("id", new GenericPropertyMatcher().contains())
				.withMatcher("description", new GenericPropertyMatcher().contains());

	}
}
