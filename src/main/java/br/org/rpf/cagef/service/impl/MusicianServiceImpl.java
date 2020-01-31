package br.org.rpf.cagef.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.org.rpf.cagef.dto.musician.MusicianDTO;
import br.org.rpf.cagef.dto.volunteer.BaseVolunteerDTO;
import br.org.rpf.cagef.entity.Musician;
import br.org.rpf.cagef.entity.Volunteer;
import br.org.rpf.cagef.repository.MusicianRepository;
import br.org.rpf.cagef.repository.VolunteerRepository;
import br.org.rpf.cagef.service.MusicianService;
import br.org.rpf.cagef.service.UserService;
import br.org.rpf.cagef.util.MusicianSpecification;

@Service("musicianService")
public class MusicianServiceImpl extends DefaultVolunteerServiceImpl implements MusicianService {

	@Autowired
	private MusicianRepository musicianRepository;

	@Autowired
	private VolunteerRepository volunteerRepository;

	@Autowired
	private UserService userService;

	@Override
	public Page<Musician> findAll(Long id, String name, Long[] cityIds, String cityName,
			String ministryOrPositionDescription, Long[] ministryOrPositionIds, Long[] instrumentIds,
			String instrumentDescription, int offset, int limit, String orderBy, String direction) {
		if (!this.userService.isAdmin()) {
			cityIds = new Long[] { UserService.authenticated().getCity().getId() };
		}

		return musicianRepository.findAll(
				new MusicianSpecification(id, name, cityIds, cityName, ministryOrPositionDescription,
						ministryOrPositionIds, instrumentDescription, instrumentIds),
				PageRequest.of(offset, limit, Direction.fromString(direction), orderBy));
	}

	@Override
	public Volunteer save(BaseVolunteerDTO musicianDTO) {
		return musicianRepository.save(fromDTO((MusicianDTO)musicianDTO));
	}

	@Override
	public void remove(Long id) {
		Volunteer musician = this.volunteerRepository.findById(id)
				.orElseThrow(() -> new org.hibernate.ObjectNotFoundException(id, Musician.class.getName()));
		this.volunteerRepository.delete(musician);
	}
}
