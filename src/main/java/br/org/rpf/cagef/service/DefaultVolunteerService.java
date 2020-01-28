package br.org.rpf.cagef.service;

import br.org.rpf.cagef.dto.volunteer.BaseVolunteerDTO;
import br.org.rpf.cagef.entity.Volunteer;

public interface DefaultVolunteerService {
	
	public Volunteer byId(Long id);

	public Volunteer save(BaseVolunteerDTO volunteerDTO);
	
	public Volunteer update(Long id, BaseVolunteerDTO volunteerDTO);

	public void remove(Long id);
}
