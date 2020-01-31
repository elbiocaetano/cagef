package br.org.rpf.cagef.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.org.rpf.cagef.dto.volunteer.BaseVolunteerDTO;
import br.org.rpf.cagef.dto.volunteer.ReportVolunteerProjection;
import br.org.rpf.cagef.dto.volunteer.VolunteerDTO;
import br.org.rpf.cagef.entity.Volunteer;
import br.org.rpf.cagef.repository.VolunteerRepository;
import br.org.rpf.cagef.service.UserService;
import br.org.rpf.cagef.service.VolunteerService;
import br.org.rpf.cagef.util.VolunteerSpecification;

@Service("volunteerService")
public class VolunteerServiceImpl extends DefaultVolunteerServiceImpl implements VolunteerService {

	@Autowired
	private VolunteerRepository volunteerRepository;

	@Autowired
	private UserService userService;

	public Page<Volunteer> findAll(Long id, String name, Long[] cityIds, String cityName,
			String ministryOrPositionDescription, Long[] ministryOrPositionIds, int offset, int limit, String orderBy,
			String direction) {
		if (!this.userService.isAdmin()) {
			cityIds = new Long[] { UserService.authenticated().getCity().getId() };
		}

		return volunteerRepository.findAll(
				new VolunteerSpecification(id, name, cityIds, cityName, ministryOrPositionDescription,
						ministryOrPositionIds, null),
				PageRequest.of(offset, limit, Direction.fromString(direction), orderBy));
	}

	@Override
	public Volunteer save(BaseVolunteerDTO volunteerDTO) {
		Volunteer v = volunteerRepository.save(fromDTO((VolunteerDTO) volunteerDTO));
		if(hasMusicMinistery(volunteerDTO)) {
			this.musicianRepository.createMusician(null, null, v.getId());
		};
		return v;
	}

	@Override
	public List<ReportVolunteerProjection> reportVolunteersByCityAndMinistryOrPosition(List<Long> citiesIds,
			List<Long> ministeriesOrPositionIds) {

		if (citiesIds != null || ministeriesOrPositionIds != null) {
			if (citiesIds != null) {
				return this.volunteerRepository.findToReportByCity(citiesIds);
			}
			return this.volunteerRepository.findToReportByMinisteryOrPosition(ministeriesOrPositionIds);
		}

		return this.volunteerRepository.findToReport();

	}
}
