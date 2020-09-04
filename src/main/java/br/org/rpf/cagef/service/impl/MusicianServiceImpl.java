package br.org.rpf.cagef.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import br.org.rpf.cagef.dto.http.request.city.MusicianRequestParamsDTO;
import br.org.rpf.cagef.dto.musician.MusicianDTO;
import br.org.rpf.cagef.dto.volunteer.BaseVolunteerDTO;
import br.org.rpf.cagef.entity.Musician;
import br.org.rpf.cagef.entity.Volunteer;
import br.org.rpf.cagef.service.MusicianService;
import br.org.rpf.cagef.service.UserService;
import br.org.rpf.cagef.util.MusicianSpecification;

@Service("musicianService")
public class MusicianServiceImpl extends DefaultVolunteerServiceImpl implements MusicianService {

	@Autowired
	private UserService userService;

	@Override
	public Page<Musician> findAll(MusicianRequestParamsDTO requestParams) {
		try {
		if (!this.userService.isAnyAdmin()) {
			requestParams.setCityIds(new Long[] { UserService.authenticated().getCity().getId() });
		}

		return musicianRepository.findAll(new MusicianSpecification(requestParams),
				requestParams.getPageRequest());
		
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public Musician save(BaseVolunteerDTO musicianDTO) {
		return musicianRepository.save(fromDTO((MusicianDTO)musicianDTO));
	}

	@Override
	public void remove(Long id) {
		Volunteer musician = this.volunteerRepository.findById(id)
				.orElseThrow(() -> new org.hibernate.ObjectNotFoundException(id, Musician.class.getName()));
		this.volunteerRepository.delete(musician);
	}
}
