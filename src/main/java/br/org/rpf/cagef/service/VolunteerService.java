package br.org.rpf.cagef.service;

import org.springframework.data.domain.Page;

import br.org.rpf.cagef.dto.volunteer.VolunteerDTO;
import br.org.rpf.cagef.entity.Volunteer;

public interface VolunteerService {
	public Page<Volunteer> findAll(Long id, String name, Long[] cityIds, String cityName, String ministryOrPositionDescription, Long[] ministryOrPositionIds, int offset, int limit, String orderBy, String direction);
	
	public Volunteer byId(Long id);

	public Volunteer save(VolunteerDTO volunteerDTO);
	
	public Volunteer update(Long id, VolunteerDTO volunteerDTO);

	public void remove(Long id);
}
