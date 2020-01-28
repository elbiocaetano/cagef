package br.org.rpf.cagef.service;

import java.util.List;

import org.springframework.data.domain.Page;

import br.org.rpf.cagef.dto.volunteer.ReportVolunteerProjection;
import br.org.rpf.cagef.entity.Volunteer;

public interface VolunteerService extends DefaultVolunteerService {
	public Page<Volunteer> findAll(Long id, String name, Long[] cityIds, String cityName,
			String ministryOrPositionDescription, Long[] ministryOrPositionIds, int offset, int limit, String orderBy,
			String direction);

	public List<ReportVolunteerProjection> reportVolunteersByCityAndMinistryOrPosition(List<Long> citiesIds,
			List<Long> ministeriesOrPositionIds);
}
