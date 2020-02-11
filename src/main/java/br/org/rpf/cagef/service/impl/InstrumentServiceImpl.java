package br.org.rpf.cagef.service.impl;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.org.rpf.cagef.dto.instrument.InstrumentDTO;
import br.org.rpf.cagef.entity.Instrument;
import br.org.rpf.cagef.entity.InstrumentCategory;
import br.org.rpf.cagef.repository.InstrumentCategoryRepository;
import br.org.rpf.cagef.repository.InstrumentRepository;
import br.org.rpf.cagef.service.InstrumentService;
import br.org.rpf.cagef.util.InstrumentSpecification;

@Service
public class InstrumentServiceImpl implements InstrumentService {

	@Autowired
	private InstrumentRepository instrumentRepository;

	@Autowired
	private InstrumentCategoryRepository instrumentCategoryRepository;

	public Page<Instrument> findAll(Long id, String description, Long[] categoryIds, String categoryName, int offset,
			int limit, String orderBy, String direction) {

		return instrumentRepository.findAll(new InstrumentSpecification(id, description, categoryIds, categoryName),
				PageRequest.of(offset, limit, Direction.fromString(direction), orderBy));
	}

	public Instrument byId(Long id) {
		return instrumentRepository.findById(id)
				.orElseThrow(() -> new org.hibernate.ObjectNotFoundException(id, Instrument.class.getName()));
	}

	@Override
	public Instrument save(InstrumentDTO instrumentDTO) {
		return instrumentRepository.save(fromDTO(null, instrumentDTO));
	}

	@Override
	public Instrument update(Long id, InstrumentDTO instrumentDTO) {
		this.instrumentRepository.findById(id)
				.orElseThrow(() -> new org.hibernate.ObjectNotFoundException(id, Instrument.class.getName()));
		return instrumentRepository.save(fromDTO(id, instrumentDTO));
	}

	@Override
	public void remove(Long id) {
		Instrument volunteer = this.instrumentRepository.findById(id)
				.orElseThrow(() -> new org.hibernate.ObjectNotFoundException(id, Instrument.class.getName()));
		this.instrumentRepository.delete(volunteer);
	}

	private Instrument fromDTO(Long id, InstrumentDTO instrumentDTO) {
		InstrumentCategory category = this.instrumentCategoryRepository.findById(instrumentDTO.getCategory().getId())
				.orElseThrow(() -> new ObjectNotFoundException(instrumentDTO.getCategory().getId(),
						InstrumentCategory.class.getName()));
		return new Instrument(id, instrumentDTO.getDescription(), category);
	}

	@Override
	public Page<InstrumentCategory> findAllCategories(Long id, String description, int offset, int limit,
			String orderBy, String direction) {
		return instrumentCategoryRepository.findAll(
				Example.of(new InstrumentCategory(id, description), getExampleMatcher()),
				PageRequest.of(offset, limit, Direction.fromString(direction), orderBy));
	}

	private ExampleMatcher getExampleMatcher() {
		return ExampleMatcher.matching().withIgnoreCase().withMatcher("description",
				new GenericPropertyMatcher().contains());
	}
}
