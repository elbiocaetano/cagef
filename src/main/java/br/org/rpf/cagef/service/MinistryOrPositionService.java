package br.org.rpf.cagef.service;

import org.springframework.data.domain.Page;

import br.org.rpf.cagef.dto.ministryorposition.MinistryOrPositionDTO;
import br.org.rpf.cagef.entity.MinistryOrPosition;

public interface MinistryOrPositionService {

	public Page<MinistryOrPosition> findAll(Long id, Long[] idIn, String description, Integer offset, Integer limit, String orderBy, String direction);

	public MinistryOrPosition byId(Long id);

	public MinistryOrPosition save(MinistryOrPositionDTO ministryOrPositionDTO);

	public MinistryOrPosition update(Long id, MinistryOrPositionDTO ministryOrPositionDTO);

	public void remove(Long id);

}
