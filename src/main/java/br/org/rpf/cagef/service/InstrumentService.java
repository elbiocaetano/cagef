package br.org.rpf.cagef.service;

import org.springframework.data.domain.Page;

import br.org.rpf.cagef.dto.instrument.InstrumentDTO;
import br.org.rpf.cagef.entity.Instrument;
import br.org.rpf.cagef.entity.InstrumentCategory;

public interface InstrumentService {
	public Page<Instrument> findAll(Long id, String description, Long[] categoryIds, String categoryName,  int offset, int limit, String orderBy, String direction);
	
	public Page<InstrumentCategory> findAllCategories(Long id, String description, int offset, int limit, String orderBy, String direction);
	
	public Instrument byId(Long id);

	public Instrument save(InstrumentDTO instrumentDTO);
	
	public Instrument update(Long id, InstrumentDTO instrumentDTO);

	public void remove(Long id);
}
