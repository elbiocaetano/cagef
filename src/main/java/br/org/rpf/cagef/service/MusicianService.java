package br.org.rpf.cagef.service;

import org.springframework.data.domain.Page;

import br.org.rpf.cagef.entity.Musician;

public interface MusicianService extends DefaultVolunteerService {
	public Page<Musician> findAll(Long id, String name, Long[] cityIds, String cityName,
			String ministryOrPositionDescription, Long[] ministryOrPositionIds, Long[] instrumentIds,
			String instrumentDescription, int offset, int limit, String orderBy, String direction);
}
