package br.org.rpf.cagef.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.org.rpf.cagef.dto.instrument.InstrumentDTO;
import br.org.rpf.cagef.entity.Instrument;
import br.org.rpf.cagef.entity.InstrumentCategory;
import br.org.rpf.cagef.repository.InstrumentRepository;
import br.org.rpf.cagef.service.InstrumentService;
import br.org.rpf.cagef.util.InstrumentSpecification;

@Service
public class InstrumentServiceImpl implements InstrumentService {

	@Autowired
	private InstrumentRepository instrumentRepository;

	public Page<Instrument> findAll(Long id, String name, Long[] categoryIds, String categoryName, int offset, int limit, String orderBy,
			String direction) {

		return instrumentRepository.findAll(new InstrumentSpecification(id, name, categoryIds, categoryName),
				PageRequest.of(offset, limit, Direction.fromString(direction), orderBy));
	}

	public Instrument byId(Long id) {
		return instrumentRepository.findById(id)
				.orElseThrow(() -> new org.hibernate.ObjectNotFoundException(id, Instrument.class.getName()));
	}

	@Override
	public Instrument save(InstrumentDTO instrumentDTO) {
		return instrumentRepository.save(fromDTO(instrumentDTO));
	}

	@Override
	public Instrument update(Long id, InstrumentDTO instrumentDTO) {
		this.instrumentRepository.findById(id)
				.orElseThrow(() -> new org.hibernate.ObjectNotFoundException(id, Instrument.class.getName()));
		instrumentDTO.setId(id);
		return instrumentRepository.save(fromDTO(instrumentDTO));
	}

	@Override
	public void remove(Long id) {
		Instrument volunteer = this.instrumentRepository.findById(id)
				.orElseThrow(() -> new org.hibernate.ObjectNotFoundException(id, Instrument.class.getName()));
		this.instrumentRepository.delete(volunteer);
	}

	private Instrument fromDTO(InstrumentDTO instrumentDTO) {
		return new Instrument(instrumentDTO.getId(), instrumentDTO.getDescription(),
				new InstrumentCategory(instrumentDTO.getCategoryId(), null));
	}
}
