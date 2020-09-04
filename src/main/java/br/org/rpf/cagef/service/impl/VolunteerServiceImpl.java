package br.org.rpf.cagef.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import br.org.rpf.cagef.dto.http.request.city.VolunteerRequestParamsDTO;
import br.org.rpf.cagef.dto.volunteer.BaseVolunteerDTO;
import br.org.rpf.cagef.dto.volunteer.ReportVolunteerProjection;
import br.org.rpf.cagef.dto.volunteer.VolunteerDTO;
import br.org.rpf.cagef.entity.Volunteer;
import br.org.rpf.cagef.service.UserService;
import br.org.rpf.cagef.service.VolunteerService;
import br.org.rpf.cagef.util.VolunteerSpecification;

@Service("volunteerService")
public class VolunteerServiceImpl extends DefaultVolunteerServiceImpl implements VolunteerService {

	@Autowired
	private UserService userService;

	public Page<Volunteer> findAll(VolunteerRequestParamsDTO requestParams) {
		try {
		if (!this.userService.isAdmin()) {
			requestParams.setCityIds(new Long[] { UserService.authenticated().getCity().getId() });
		}

		return volunteerRepository.findAll(
				new VolunteerSpecification(requestParams),
				requestParams.getPageRequest());
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public Volunteer save(BaseVolunteerDTO volunteerDTO) {
		Volunteer v = volunteerRepository.save(fromDTO((VolunteerDTO) volunteerDTO));
		if(hasMusicMinistery(volunteerDTO)) {
			this.musicianRepository.createMusician(null, null, v.getId());
		}
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
